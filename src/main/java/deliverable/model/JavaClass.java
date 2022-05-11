package deliverable.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;

public class JavaClass {
	
	private String name;
	private String content;
	private Release release;
	private List<RevCommit> commits;	//These are the commits of the specified release that have modified the class
	private boolean isBuggy;
	
	private int size;
	private int nr;
	private int nAuth;
	
	public JavaClass(String name, String content, Release release) {
		this.name = name;
		this.content = content;
		this.release = release;
		this.commits = new ArrayList<>();
		this.isBuggy = false;
		
		this.size = 0;
		this.nr = 0;
		this.nAuth = 0;
		
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the nr
	 */
	public int getNr() {
		return nr;
	}

	/**
	 * @param nr the nr to set
	 */
	public void setNr(int nr) {
		this.nr = nr;
	}

	/**
	 * @return the nAuth
	 */
	public int getnAuth() {
		return nAuth;
	}

	/**
	 * @param nAuth the nAuth to set
	 */
	public void setnAuth(int nAuth) {
		this.nAuth = nAuth;
	}

}
