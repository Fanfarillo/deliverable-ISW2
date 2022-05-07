package deliverable.control;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import deliverable.model.Release;
import deliverable.model.Ticket;
import deliverable.utils.JSONUtil;
import deliverable.utils.ReleaseUtil;

public class RetrieveJiraInfo {
	
	private String projKey;
	
	public RetrieveJiraInfo(String projName) {
		this.projKey = projName.toUpperCase();
	}
	
	public ArrayList<Release> retrieveReleases() throws JSONException, IOException, ParseException {
		
		Map<Date, String> unsortedReleasesMap = new HashMap<Date, String>();
		ArrayList<Release> releasesList = new ArrayList<Release>();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		String url = "https://issues.apache.org/jira/rest/api/latest/project/" + this.projKey + "/version";
		JSONObject json = JSONUtil.readJsonFromUrl(url);		
		JSONArray releases = json.getJSONArray("values");
		int total = json.getInt("total");
		
		for(int i=0; i<total; i++) {
			if(releases.getJSONObject(i).get("released").toString() == "true") {
				
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
		Map<Date, String> releasesMap = new TreeMap<Date, String>(unsortedReleasesMap);		//TreeMap sorts unsortedReleasesMap by date
		
		int i=1;
		for(Map.Entry<Date, String> entry : releasesMap.entrySet()) {	//Iteration over releasesMap
			releasesList.add(new Release(i, entry.getValue(), entry.getKey()));
			i++;
		}
		
		return releasesList;
		
	}
	
	private static ArrayList<Ticket> createTicketInstances(Integer i, Integer j, Integer total, JSONArray issues, ArrayList<Release> releasesList) throws ParseException {
		
		ArrayList<Ticket> ticketsList = new ArrayList<Ticket>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		
        for (; i < total && i < j; i++) {
        	//Iterate through each issue
            String key = (issues.getJSONObject(i%1000).get("key").toString()) + ":";
            String resolutionDateStr = issues.getJSONObject(i%1000).get("resolutiondate").toString();
            String creationDateStr = issues.getJSONObject(i%1000).get("created").toString();            
            JSONArray listAV = issues.getJSONObject(i%1000).getJSONArray("versions");
            
            Date resolutionDate = formatter.parse(resolutionDateStr);
            Date creationDate = formatter.parse(creationDateStr);
            ArrayList<Release> affectedVersionsList = new ArrayList<Release>();
            
            for(int k=0; k<listAV.length(); k++) {
            	Release affectedVersion = ReleaseUtil.getReleaseByName(listAV.getJSONObject(k).get("name").toString(), releasesList);
            	affectedVersionsList.add(affectedVersion);            	
            }            
            Release openVersion = ReleaseUtil.getReleaseByDate(creationDate, releasesList);
            Release fixVersion = ReleaseUtil.getReleaseByDate(resolutionDate, releasesList);
            
            if(openVersion != null && fixVersion != null) {
            	ticketsList.add(new Ticket(key, openVersion, fixVersion, affectedVersionsList));
            }
            
        }
        
        return ticketsList;
        
	}
	
	public void retrieveIssues(ArrayList<Release> releasesList) throws JSONException, IOException, ParseException {
		
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
	        
	        ArrayList<Ticket> ticketsList = createTicketInstances(i, j, total, issues, releasesList);
	        
	    } while (i < total);
	    
	}

}
