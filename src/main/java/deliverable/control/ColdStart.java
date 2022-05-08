package deliverable.control;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import deliverable.enums.ProjectsEnum;
import deliverable.model.Release;
import deliverable.model.Ticket;

public class ColdStart {
	
	public List<Ticket> retrieveOtherConsistentIssues() throws JSONException, IOException, ParseException {
		
		List<Ticket> allConsistentTickets = new ArrayList<>();
		
		for(ProjectsEnum proj : ProjectsEnum.values()) {
			
			RetrieveJiraInfo retJiraInfo = new RetrieveJiraInfo(proj.toString());
			List<Release> coldStartReleases = retJiraInfo.retrieveReleases();
			List<Ticket> coldStartTickets = retJiraInfo.retrieveIssues(coldStartReleases);
			List<Ticket> coldStartConsistentTickets = retJiraInfo.retrieveConsistentIssues(coldStartTickets, coldStartReleases);
			
			allConsistentTickets.addAll(coldStartConsistentTickets);
			
		}
		return allConsistentTickets;
		
	}
	
	public Double computeProportion(List<Ticket> issues) {
		
		List<Double> proportions = new ArrayList<>();
		
		//We are calculating the proportion value P for each ticket in the list
		for(Ticket issue : issues) {
			//P = (FV-IV)/(FV-OV)
			Double prop = (1.0)*(issue.getFv().getId()-issue.getIv().getId())/(issue.getFv().getId()-issue.getOv().getId());
			if(prop >= 1.0) {
				proportions.add(prop);
			}
			
		}		
		//Return the average among all the proportion values
		Double propSum = 0.0;
		for(Double prop : proportions) {
			propSum = propSum + prop;
		}
		return propSum/proportions.size();
		
	}

}
