package deliverable.control;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.json.JSONException;

import deliverable.model.Release;

public class ExecutionFlow {
	
	//This private constructor is meant to hide the public one: classes with only static methods do not have to be instantiated.
	private ExecutionFlow() {
		throw new IllegalStateException("This class does not have to be instantiated.");
	}
	
	public static void collectData(String projName) throws JSONException, IOException, ParseException {
		
		RetrieveJiraInfo retJiraInfo = new RetrieveJiraInfo(projName);
		List<Release> releasesList = retJiraInfo.retrieveReleases();
		retJiraInfo.retrieveIssues(releasesList);
		
	}

}
