package deliverable.model;

import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;

public class ReleaseCommits {
	
	private Release release;
	private List<RevCommit> commits;
	private RevCommit lastCommit;
	private List<String> javaClasses;
	
	public ReleaseCommits(Release release, List<RevCommit> commits, RevCommit lastCommit) {
		this.release = release;
		this.commits = commits;
		this.lastCommit = lastCommit;
		this.javaClasses = null;
		
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
	 * @return the lastCommit
	 */
	public RevCommit getLastCommit() {
		return lastCommit;
	}

	/**
	 * @param lastCommit the lastCommit to set
	 */
	public void setLastCommit(RevCommit lastCommit) {
		this.lastCommit = lastCommit;
	}

	/**
	 * @return the javaClasses
	 */
	public List<String> getJavaClasses() {
		return javaClasses;
	}

	/**
	 * @param javaClasses the javaClasses to set
	 */
	public void setJavaClasses(List<String> javaClasses) {
		this.javaClasses = javaClasses;
	}

}
