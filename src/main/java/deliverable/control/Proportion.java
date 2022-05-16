package deliverable.control;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import deliverable.enums.ProjectsEnum;
import deliverable.model.Release;
import deliverable.model.Ticket;

public class Proportion {
	
	//This private constructor is meant to hide the public one: classes with only static methods do not have to be instantiated.
	private Proportion() {
		throw new IllegalStateException("This class does not have to be instantiated.");
	}
	
	/*This method retrieves all consistent issues of the following projects: Falcon, Ivy, Openjpa, Storm, Tajo*/
	public static List<Ticket> coldStartRetrieveConsistentIssues() throws JSONException, IOException, ParseException {
		
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
	
	public static Double computeProportion(List<Ticket> issues) {
		
		List<Double> proportions = new ArrayList<>();
		
		//We are calculating the proportion value P for each ticket in the list
		for(Ticket issue : issues) {
			//P = (FV-IV)/(FV-OV)
			Double prop = (1.0)*(issue.getFv().getId()-issue.getIv().getId())/(issue.getFv().getId()-issue.getOv().getId());
			if(prop >= 1.0) {	//P cannot be less than 1
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
