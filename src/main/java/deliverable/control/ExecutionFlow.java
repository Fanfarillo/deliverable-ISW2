package deliverable.control;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.json.JSONException;

import deliverable.enums.CsvNamesEnum;
import deliverable.files.LabelingFile;
import deliverable.model.JavaClass;
import deliverable.model.Release;
import deliverable.model.ReleaseCommits;
import deliverable.model.Ticket;
import deliverable.utils.JavaClassUtil;
import deliverable.utils.ReleaseUtil;
import deliverable.utils.TicketUtil;

public class ExecutionFlow {
	
	//This private constructor is meant to hide the public one: classes with only static methods do not have to be instantiated.
	private ExecutionFlow() {
		throw new IllegalStateException("This class does not have to be instantiated.");
	}
	
	private static void writeCsvPerRelease(String projName, List<JavaClass> javaClassesList, int lastReleaseID) throws IOException {
		
		for(int i=2; i<=lastReleaseID; i++) {	//In the first iteration of walk forward, testing set is composed of second release classes
			List<JavaClass> iterJavaClassesList = JavaClassUtil.filterJavaClassesByRelease(javaClassesList, i);			
			LabelingFile labelingTesting = new LabelingFile(projName, CsvNamesEnum.TESTING, i-1, iterJavaClassesList);
			labelingTesting.writeOnCsv();
			
		}
		
	}
	
	public static void collectData(String projName) throws JSONException, IOException, ParseException, RevisionSyntaxException, GitAPIException {
		
		RetrieveJiraInfo retJiraInfo = new RetrieveJiraInfo(projName);
		List<Release> releasesList = retJiraInfo.retrieveReleases();
		List<Ticket> ticketsList = retJiraInfo.retrieveIssues(releasesList);
		
		int lastReleaseID = ReleaseUtil.getLastRelease(releasesList).getId();
		//We are retrieving consistent tickets of other projects (cold start style) so that they can be used in case incremental-train-test is useless
		List<Ticket> coldStartProjConsistentTickets = Proportion.coldStartRetrieveConsistentIssues();
		
		for(int i=1; i<=lastReleaseID; i++) {
			//First (numReleases/2)-1 releases: we construct the training sets (with snoring) for all the walk forward iterations
			//i=lastReleaseID: we construct the testing sets (without snoring) for all the walk forward iterations and a csv file with all the info (without snoring)
			if(i<lastReleaseID/2 || i==lastReleaseID) {
				
				List<Release> iterReleasesList = ReleaseUtil.getFirstReleases(releasesList, i);
				List<Ticket> iterTicketsList = TicketUtil.getFirstTickets(ticketsList, i);	//We are cutting out tickets with FV > last release of training set
				List<Ticket> consistentTicketsList = retJiraInfo.retrieveConsistentIssues(iterTicketsList, iterReleasesList);
				
				Double p = null;				
				if(consistentTicketsList.size() >= 5) {		//Check to decide if use cold start or incremental-train-test
					p = Proportion.computeProportion(consistentTicketsList);
				}
				else {
					p = Proportion.computeProportion(coldStartProjConsistentTickets);
				}				
				List<Ticket> adjustedTicketsList = retJiraInfo.adjustTicketsList(iterTicketsList, consistentTicketsList, iterReleasesList, p);
				
				RetrieveGitInfo retGitInfo = new RetrieveGitInfo("C:\\Users\\barba\\OneDrive\\Desktop\\Work in progress\\Progetti ISW2\\" + projName, adjustedTicketsList, iterReleasesList);
				List<RevCommit> allCommitsList = retGitInfo.retrieveAllCommits();
				List<ReleaseCommits> relCommAssociationsList = retGitInfo.getRelCommAssociations(allCommitsList);
				retGitInfo.getRelClassesAssociations(relCommAssociationsList);
				List<JavaClass> javaClassesList = retGitInfo.labelClasses(relCommAssociationsList);
				retGitInfo.assignCommitsToClasses(javaClassesList, allCommitsList, relCommAssociationsList);
				
				ComputeMetrics computeMetrics = new ComputeMetrics(retGitInfo, javaClassesList);
				javaClassesList = computeMetrics.doAllMetricsComputation();
				
				if(i==lastReleaseID) {
					List<JavaClass> javaClassesHalfList = JavaClassUtil.cutHalfReleases(javaClassesList, iterReleasesList);
					writeCsvPerRelease(projName, javaClassesHalfList, lastReleaseID/2);	 //Here we write testing sets for all the iterations of walk forward
					
					LabelingFile labelingBuggy = new LabelingFile(projName, CsvNamesEnum.BUGGY, 0, javaClassesHalfList);
					labelingBuggy.writeOnCsv();
					
					//Here we retrieve classes that are currently in repository and we write them into a csv file for an eventual prediction of
					//current buggyness of classes
					List<JavaClass> currentJavaClassesList = retGitInfo.getCurrentClasses(allCommitsList);
					ComputeMetrics computeCurrentMetrics = new ComputeMetrics(retGitInfo, currentJavaClassesList);
					currentJavaClassesList = computeCurrentMetrics.doAllMetricsComputation();
					
					LabelingFile labelingCurrent = new LabelingFile(projName, CsvNamesEnum.CURRENT, 0, currentJavaClassesList);
					labelingCurrent.writeOnCsv();
					
				}
				else {
					LabelingFile labelingTraining = new LabelingFile(projName, CsvNamesEnum.TRAINING, i, javaClassesList);
					labelingTraining.writeOnCsv();
					
				}
								
			}
			
		}
		
	}

}
