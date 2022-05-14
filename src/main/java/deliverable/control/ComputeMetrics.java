package deliverable.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;

import deliverable.model.JavaClass;

public class ComputeMetrics {
	
	private RetrieveGitInfo retGitInfo;
	private List<JavaClass> javaClassesList;
	
	public ComputeMetrics(RetrieveGitInfo retGitInfo, List<JavaClass> javaClassesList) {
		this.retGitInfo = retGitInfo;
		this.javaClassesList = javaClassesList;
		
	}
	
	private void computeSize() {
		
		for(JavaClass javaClass : this.javaClassesList) {
			String[] lines = javaClass.getContent().split("\r\n|\r|\n");
			javaClass.setSize(lines.length);			
			
		}
		
	}
	
	private void computeNR() {
		
		for(JavaClass javaClass : this.javaClassesList) {
			javaClass.setNr(javaClass.getCommits().size());
			
		}
		
	}
	
	private void computeNAuth() {
		
		for(JavaClass javaClass : this.javaClassesList) {
			List<String> classAuthors = new ArrayList<>();
			
			for(RevCommit commit : javaClass.getCommits()) {
				if(!classAuthors.contains(commit.getAuthorIdent().getName())) {
					classAuthors.add(commit.getAuthorIdent().getName());
				}
				
			}
			javaClass.setnAuth(classAuthors.size());
			
		}
		
	}
	
	private void computeLocAndChurnMetrics(JavaClass javaClass) {
		
		int sumLOC = 0;
		int maxLOC = 0;
		double avgLOC = 0;
		int churn = 0;
		int maxChurn = 0;
		double avgChurn = 0;
		
		for(int i=0; i<javaClass.getAddedLinesList().size(); i++) {
			
			int currentLOC = javaClass.getAddedLinesList().get(i);
			int currentDiff = Math.abs(javaClass.getAddedLinesList().get(i) - javaClass.getDeletedLinesList().get(i));
			
			sumLOC = sumLOC + currentLOC;
			churn = churn + currentDiff;
			
			if(currentLOC > maxLOC) {
				maxLOC = currentLOC;
			}
			if(currentDiff > maxChurn) {
				maxChurn = currentDiff;
			}
			
		}
		
		//If a class has 0 revisions, its AvgLocAdded and AvgChurn are 0 (see initialization above).
		if(!javaClass.getAddedLinesList().isEmpty()) {		 
			avgLOC = 1.0*sumLOC/javaClass.getAddedLinesList().size();
		}
		if(!javaClass.getAddedLinesList().isEmpty()) {
			avgChurn = 1.0*churn/javaClass.getAddedLinesList().size();
		}
		
		javaClass.setLocAdded(sumLOC);
		javaClass.setMaxLocAdded(maxLOC);
		javaClass.setAvgLocAdded(avgLOC);
		javaClass.setChurn(churn);
		javaClass.setMaxChurn(maxChurn);
		javaClass.setAvgChurn(avgChurn);
		
	}
	
	private void computeLocAndChurn() throws IOException {
		
		for(JavaClass javaClass : this.javaClassesList) {
			
			this.retGitInfo.computeAddedAndDeletedLinesList(javaClass);
			computeLocAndChurnMetrics(javaClass);
			
		}
		
	}
	
	public List<JavaClass> doAllMetricsComputation() throws IOException {		
		//When possible, the following metrics are applied just on one single release (i.e. the release as attribute of JavaClass element)
		
		computeSize();	//Size = lines of code (LOC) in the class
		computeNR();	//NR = number of commits that have modified the class
		computeNAuth();	//NAuth = number of authors of the class
		computeLocAndChurn();
		/* LocAdded = sum of number of added LOC in all the commit of the given release
		 * MaxLocAdded = max number of added LOC in all the commit of the given release
		 * AvgLocAdded = average number of added LOC in all the commit of the given release
		 * Churn = sum of |number of added LOC - number of deleted LOC| in all the commit of the given release
		 * MaxChurn = max |number of added LOC - number of deleted LOC| in all the commit of the given release
		 * Churn = average of |number of added LOC - number of deleted LOC| in all the commit of the given release */
		
		return this.javaClassesList;
		
	}

}
