package deliverable.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;

public class JavaClass {
	
	private String name;
	private Release release;
	private List<RevCommit> commits;	//These are the commits of the specified release that have modified the class
	private boolean isBuggy;
	
	public JavaClass(String name, Release release) {
		this.name = name;
		this.release = release;
		this.commits = new ArrayList<>();
		this.isBuggy = false;
		
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the release
	 */
	public Release getRelease() {
		return release;
	}

	/**
	 * @param release the release to set
	 */
	public void setRelease(Release release) {
		this.release = release;
	}

	/**
	 * @return the commits
	 */
	public List<RevCommit> getCommits() {
		return commits;
	}

	/**
	 * @param commits the commits to set
	 */
	public void setCommits(List<RevCommit> commits) {
		this.commits = commits;
	}

	/**
	 * @return the isBuggy
	 */
	public boolean isBuggy() {
		return isBuggy;
	}

	/**
	 * @param isBuggy the isBuggy to set
	 */
	public void setBuggy(boolean isBuggy) {
		this.isBuggy = isBuggy;
	}

}
