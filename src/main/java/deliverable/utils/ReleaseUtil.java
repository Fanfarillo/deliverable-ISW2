package deliverable.utils;

import java.util.ArrayList;
import java.util.Date;

import deliverable.model.Release;

public class ReleaseUtil {

	public static Release getReleaseByName(String releaseName, ArrayList<Release> releasesList) {
		
		for(Release rel : releasesList) {
			if(rel.getName().equals(releaseName)) {
				return rel;
			}
			
		}
		return null;
		
	}
	
	public static Release getReleaseByDate(Date date, ArrayList<Release> releasesList) {
		
		for(Release rel : releasesList) {
			if(rel.getDate().after(date)) {
				return rel;
			}
			
		}
		return null;
		
	}
	
}
