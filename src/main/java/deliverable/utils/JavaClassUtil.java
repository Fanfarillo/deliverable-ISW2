package deliverable.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.revwalk.RevCommit;

import deliverable.model.JavaClass;
import deliverable.model.Release;
import deliverable.model.ReleaseCommits;

public class JavaClassUtil {
	
	//This private constructor is meant to hide the public one: utility classes do not have to be instantiated.
	private JavaClassUtil() {
		throw new IllegalStateException("This class does not have to be instantiated.");
	}
	
	/*Callers:
	 * labelClasses (RetrieveGitInfo)
	 * getCurrentClasses (RetrieveGitInfo)*/
	public static List<JavaClass> buildAllJavaClasses(List<ReleaseCommits> relCommAssociations) {
		
		List<JavaClass> javaClasses = new ArrayList<>();
		
		for(ReleaseCommits relComm : relCommAssociations) {
			for(Map.Entry<String, String> entryMap : relComm.getJavaClasses().entrySet()) {
				javaClasses.add(new JavaClass(entryMap.getKey(), entryMap.getValue(), relComm.getRelease()));
				
			}
			
		}
		return javaClasses;
		
	}
	
	/*Callers:
	 * doLabeling (RetrieveGitInfo)*/
	public static void updateJavaClassBuggyness(List<JavaClass> javaClasses, String className, Release iv, Release fv) {
		//fv is related to the single commit, not to the ticket
		
		for(JavaClass javaClass : javaClasses) {
			//if javaClass has been modified by commit (that is className) and is related to a version v such that iv <= v < fv, then javaClass is buggy
			if(javaClass.getName().equals(className) && javaClass.getRelease().getId() >= iv.getId() && javaClass.getRelease().getId() < fv.getId()) {
				javaClass.setBuggy(true);
				
			}
			
		}
		
	}
	
	/*Callers:
	 * assignCommitsToClasses (RetrieveGitInfo)
	 * getCurrentClasses (RetrieveGitInfo)*/
	public static void updateJavaClassCommits(List<JavaClass> javaClasses, String className, Release associatedRelease, RevCommit commit) {
		
		for(JavaClass javaClass : javaClasses) {
			//if javaClass has been modified by commit (that is className) and is related to the same release of commit, then add commit to javaClass.commits
			if(javaClass.getName().equals(className) && javaClass.getRelease().getId() == associatedRelease.getId() && !javaClass.getCommits().contains(commit)) {
				javaClass.getCommits().add(commit);
				
			}
			
		}
		
	}
	
	/*Callers:
	 * collectData (ExecutionFlow)*/
	public static List<JavaClass> cutHalfReleases(List<JavaClass> javaClassesList, List<Release> releasesList) {
		
		List<JavaClass> remainingJavaClasses = new ArrayList<>();
		
		for(JavaClass javaClass : javaClassesList) {
			if(javaClass.getRelease().getId() <= ReleaseUtil.getLastRelease(releasesList).getId()/2) {
				remainingJavaClasses.add(javaClass);
			}
			
		}
		return remainingJavaClasses;
		
	}
	
	/*Callers:
	 * writeCsvPerRelease (ExecutionFlow)*/
	public static List<JavaClass> filterJavaClassesByRelease(List<JavaClass> javaClassesList, int releaseID) {
		
		List<JavaClass> remJavaClasses = new ArrayList<>();
		
		for(JavaClass javaClass : javaClassesList) {
			if(javaClass.getRelease().getId() == releaseID) {
				remJavaClasses.add(javaClass);
				
			}
			
		}
		return remJavaClasses;
		
	}

}
