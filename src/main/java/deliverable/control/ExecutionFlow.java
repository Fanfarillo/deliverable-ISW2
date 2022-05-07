package deliverable.control;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONException;

import deliverable.model.Release;

public class ExecutionFlow {
	
	public static void collectData(String projName) throws JSONException, IOException, ParseException {
		
		RetrieveJiraInfo retJiraInfo = new RetrieveJiraInfo(projName);
		ArrayList<Release> releasesList = retJiraInfo.retrieveReleases();
		retJiraInfo.retrieveIssues(releasesList);
		
	}

}
