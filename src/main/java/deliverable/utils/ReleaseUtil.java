package deliverable.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import deliverable.model.Release;

public class ReleaseUtil {
	
	//This private constructor is meant to hide the public one: utility classes do not have to be instantiated.
	private ReleaseUtil() {
		throw new IllegalStateException("This class does not have to be instantiated.");
	}

	/*Callers:
	 * createTicketInstance (RetrieveJiraInfo)*/
	public static Release getReleaseByName(String releaseName, List<Release> releasesList) {
		
		for(Release rel : releasesList) {
			if(rel.getName().equals(releaseName)) {
				return rel;
			}
			
		}
		return null;
		
	}
	
	/*Callers:
	 * createTicketInstance (RetrieveJiraInfo)*/
	public static Release getReleaseByDate(Date date, List<Release> releasesList) {
		
		for(Release rel : releasesList) {
			if(rel.getDate().after(date)) {		//Assumption: releases in releasesList are ordered by date
				return rel;
			}
			
		}
		return null;
		
	}
	
	/*Callers:
	 * cutHalfReleases (JavaClassUtil)
	 * collectData (ExecutionFlow)
	 * getCurrentClasses (RetrieveGitInfo)*/
	public static Release getLastRelease(List<Release> releasesList) {
		
		Release lastRelease = releasesList.get(0);
		for(Release release : releasesList) {
			//if releaseDate > lastReleaseDate then refresh lastRelease
			if(release.getDate().after(lastRelease.getDate())) {
				lastRelease = release;
			}
			
		}
		return lastRelease;
			
	}
	
	/*Callers:
	 * collectData (ExecutionFlow)*/
	public static List<Release> getFirstReleases(List<Release> releasesList, int maxReleaseID) {
		
		List<Release> firstReleases = new ArrayList<>();
		
		for(Release rel : releasesList) {
			if(rel.getId() <= maxReleaseID) {
				firstReleases.add(rel);
				
			}
			
		}
		return firstReleases;
		
	}
	
}
