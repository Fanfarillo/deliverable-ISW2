package deliverable.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.lib.PersonIdent;
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
			List<PersonIdent> classAuthors = new ArrayList<>();
			
			for(RevCommit commit : javaClass.getCommits()) {
				if(!classAuthors.contains(commit.getAuthorIdent())) {
					classAuthors.add(commit.getAuthorIdent());
				}
				
			}
			javaClass.setnAuth(classAuthors.size());
			
		}
		
	}
	
	private void computeLocAndChurn() throws IOException {
		
		for(JavaClass javaClass : this.javaClassesList) {
			
			this.retGitInfo.computeAddedAndDeletedLinesList(javaClass);
			
		}
		
	}
	
	public void doAllMetricsComputation() throws IOException {		
		//When possible, the following metrics are applied just on one single release (i.e. the release as attribute of JavaClass element)
		
		computeSize();	//Size = lines of code (LOC) in the class
		computeNR();	//NR = number of commits that have modified the class
		computeNAuth();	//NAuth = number of authors of the class
		computeLocAndChurn();
		
	}

}
