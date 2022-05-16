package deliverable.control;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import deliverable.model.Release;
import deliverable.model.Ticket;
import deliverable.utils.JSONUtil;
import deliverable.utils.ReleaseUtil;
import deliverable.utils.TicketUtil;


public class RetrieveJiraInfo {
	
	private String projKey;
	
	public RetrieveJiraInfo(String projName) {
		this.projKey = projName.toUpperCase();
	}
	
	/*This method retrieves all the versions of the project (Avro or Bookkeeper) that are released and with a release date*/
	public List<Release> retrieveReleases() throws JSONException, IOException, ParseException {
		
		//The HashMap is a support to instantiate Release objects
		Map<Date, String> unsortedReleasesMap = new HashMap<>();	//Date = release date; String = release name (e.g. 1.4.2)
		List<Release> releasesList = new ArrayList<>();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		String url = "https://issues.apache.org/jira/rest/api/latest/project/" + this.projKey + "/version";
		JSONObject json = JSONUtil.readJsonFromUrl(url);		
		JSONArray releases = json.getJSONArray("values");
		int total = json.getInt("total");
		
		for(int i=0; i<total; i++) {
			if(releases.getJSONObject(i).get("released").toString().equals("true")) {
				
				try {
					String releaseDateString = releases.getJSONObject(i).get("releaseDate").toString();
					Date releaseDate = formatter.parse(releaseDateString);					
					String releaseName = releases.getJSONObject(i).get("name").toString();
					
					unsortedReleasesMap.put(releaseDate, releaseName);
					
				} catch(JSONException e) {
					//There is no release date: skip this release and go on
					
				}
				
			}
			
		}		
		Map<Date, String> releasesMap = new TreeMap<>(unsortedReleasesMap);		//TreeMap sorts unsortedReleasesMap by date
		
		int i=1;
		for(Map.Entry<Date, String> entry : releasesMap.entrySet()) {	//Iteration over releasesMap
			releasesList.add(new Release(i, entry.getValue(), entry.getKey()));
			i++;
		}
		
		return releasesList;
		
	}
	
	/*This function collects key, opening version, fixed version and registered affected versions of a specific issue; then it uses this information
	 * to create a new Ticket instance that will be added into ticketsList*/
	private static Ticket createTicketInstance(Integer i, JSONArray issues, List<Release> releasesList) throws ParseException {
		
		Ticket ticket = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		
        try {
        	String key = (issues.getJSONObject(i%1000).get("key").toString()) + ":";
        	JSONObject fields = issues.getJSONObject(i%1000).getJSONObject("fields");
        	
        	String resolutionDateStr = fields.get("resolutiondate").toString();
        	String creationDateStr = fields.get("created").toString();            
        	JSONArray listAV = fields.getJSONArray("versions");
        		
        	Date resolutionDate = formatter.parse(resolutionDateStr);
       		Date creationDate = formatter.parse(creationDateStr);
       		ArrayList<Release> affectedVersionsList = new ArrayList<>();
           
       		for(int k=0; k<listAV.length(); k++) {
       			Release affectedVersion = ReleaseUtil.getReleaseByName(listAV.getJSONObject(k).get("name").toString(), releasesList);
       			
       			if(affectedVersion != null) {
       				affectedVersionsList.add(affectedVersion); 
       			}
       			
       		}            
       		Release openVersion = ReleaseUtil.getReleaseByDate(creationDate, releasesList);
       		Release fixVersion = ReleaseUtil.getReleaseByDate(resolutionDate, releasesList);
            
       		if(openVersion != null && fixVersion != null) {
       			ticket = new Ticket(key, openVersion, fixVersion, affectedVersionsList);
       		}
        		
       	} catch(JSONException e) {
       		//There is not enough information about the issue: skip this ticket and go on
        		
       	}
                  
        return ticket;
        
	}
	
	/*This method retrieves all the fixed bugs of the project with their key (e.g. AVRO-536:), their opening version, their fixed version
	 * and their registered affected versions*/
	public List<Ticket> retrieveIssues(List<Release> releasesList) throws JSONException, IOException, ParseException {
		
		List<Ticket> ticketsList = new ArrayList<>();
		
		Integer i=0;
		Integer j=0;
		Integer total=1;
		
	    do {
	    	//Only gets a max of 1000 at a time, so must do this multiple times if bugs > 1000
	        j = i + 1000;
	        
	        /* The query in Jira is:
	         * project = <projKey> AND issuetype = Bug AND (status = Closed OR status = Resolved) AND resolution = Fixed */	        
	        String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
	                + this.projKey + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
	                + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
	                + i.toString() + "&maxResults=" + j.toString();
	        
	        JSONObject json = JSONUtil.readJsonFromUrl(url);
	        JSONArray issues = json.getJSONArray("issues");
	        total = json.getInt("total");
	        
	        for (; i < total && i < j; i++) {
	        	//Iterate through each issue	        
	        	Ticket ticket = createTicketInstance(i, issues, releasesList);
	        	if(ticket != null) {
	        		ticketsList.add(ticket);
	        	}
	        
	        }
	        
	    } while (i < total);
	    
	    return ticketsList;
	    
	}
	
	/*This method takes in input all the tickets of a project and selects the ones resulting consistent.
	 * Consistent issues have:
	 * - Available affected versions (i.e. affected versions not null)
	 * - Affected versions that do not succeed fixed version and are not equal to fixed version
	 * - Opening version that is marked as affected
	 * Consecutive affected versions are not considered mandatory. By consequence, this method calls adjustTicket to fill eventual missing
	 * affected version between the initial affected version and the previous version of fixed version*/
	public List<Ticket> retrieveConsistentIssues(List<Ticket> ticketsList, List<Release> releasesList) {
		
		List<Ticket> consistentIssues = new ArrayList<>();
		
		for(int i=0; i<ticketsList.size(); i++) {
			if(TicketUtil.isConsistentTicket(ticketsList.get(i))) {
				consistentIssues.add(TicketUtil.adjustTicket(ticketsList.get(i), releasesList));
				
			}
			
		}
		return consistentIssues;
		
	}
	
	private static List<Ticket> retrieveInconsistentTicketsList(List<Ticket> ticketsList, List<Ticket> consistentTicketsList) {
		
		List<Ticket> inconsistentTicketsList = new ArrayList<>();
		
		for(Ticket ticket : ticketsList) {
			boolean isConsistent = false;
			
			for(Ticket consistentTicket : consistentTicketsList) {				
				if(ticket.getKey().equals(consistentTicket.getKey())) {
					isConsistent = true;
				}
				
			}			
			if(!isConsistent) {
				inconsistentTicketsList.add(ticket);
			}
			
		}
		return inconsistentTicketsList;
		
	}
	
	/*This method retrieves all the issues resulting inconsistent and sets opportunely their AV list using the (cold start) proportion technique
	 * and, in particular, using value P computed in computeProportion function of ColdStart class*/
	public List<Ticket> adjustTicketsList(List<Ticket> ticketsList, List<Ticket> consistentTicketsList, List<Release> releasesList, Double p) {
		
		List<Ticket> inconsistentTicketsList = retrieveInconsistentTicketsList(ticketsList, consistentTicketsList);
		
		for(Ticket incTicket : inconsistentTicketsList) {
			//Setting AV and not IV is important for compatibility with adjustTicket(...) reason
			Ticket incTicketWinitialAV = TicketUtil.setInitialAV(incTicket, releasesList, p);
			Ticket adjustedIncTicket = TicketUtil.adjustTicket(incTicketWinitialAV, releasesList);
			
			consistentTicketsList.add(adjustedIncTicket);	//Now also adjustedIncTicket has consistent AVs
			
		}
		return consistentTicketsList;	//Now this list contains ALL the tickets with consistent / adjusted AVs
		
	}

}
