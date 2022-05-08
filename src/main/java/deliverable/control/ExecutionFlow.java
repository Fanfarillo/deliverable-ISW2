package deliverable.control;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.json.JSONException;

import deliverable.model.Release;
import deliverable.model.Ticket;

public class ExecutionFlow {
	
	//This private constructor is meant to hide the public one: classes with only static methods do not have to be instantiated.
	private ExecutionFlow() {
		throw new IllegalStateException("This class does not have to be instantiated.");
	}
	
	public static void collectData(String projName) throws JSONException, IOException, ParseException {
		
		RetrieveJiraInfo retJiraInfo = new RetrieveJiraInfo(projName);
		List<Release> releasesList = retJiraInfo.retrieveReleases();
		List<Ticket> ticketsList = retJiraInfo.retrieveIssues(releasesList);
		
		ColdStart coldStart = new ColdStart();
		List<Ticket> otherProjConsistentTickets = coldStart.retrieveOtherConsistentIssues();
		Double p = coldStart.computeProportion(otherProjConsistentTickets);
		
		List<Ticket> consistentTicketsList = retJiraInfo.retrieveConsistentIssues(ticketsList, releasesList);
		List<Ticket> adjustedTicketsList = retJiraInfo.adjustTicketsList(ticketsList, consistentTicketsList, releasesList, p);
		
	}

}
