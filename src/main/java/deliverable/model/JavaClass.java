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
	private int locAdded;
	private int maxLocAdded;
	private double avgLocAdded;
	private int churn;
	private int maxChurn;
	private double avgChurn;
	
	private List<Integer> addedLinesList;
	private List<Integer> deletedLinesList;
	
	public JavaClass(String name, String content, Release release) {
		this.name = name;
		this.content = content;
		this.release = release;
		this.commits = new ArrayList<>();
		this.isBuggy = false;
		
		this.size = 0;
		this.nr = 0;
		this.nAuth = 0;
		this.locAdded = 0;
		this.maxLocAdded = 0;
		this.avgLocAdded = 0;
		this.churn = 0;
		this.maxChurn = 0;
		this.avgChurn = 0;
		
		this.addedLinesList = new ArrayList<>();
		this.deletedLinesList = new ArrayList<>();
		
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

	/**
	 * @return the locAdded
	 */
	public int getLocAdded() {
		return locAdded;
	}

	/**
	 * @param locAdded the locAdded to set
	 */
	public void setLocAdded(int locAdded) {
		this.locAdded = locAdded;
	}

	/**
	 * @return the maxLocAdded
	 */
	public int getMaxLocAdded() {
		return maxLocAdded;
	}

	/**
	 * @param maxLocAdded the maxLocAdded to set
	 */
	public void setMaxLocAdded(int maxLocAdded) {
		this.maxLocAdded = maxLocAdded;
	}

	/**
	 * @return the avgLocAdded
	 */
	public double getAvgLocAdded() {
		return avgLocAdded;
	}

	/**
	 * @param avgLocAdded the avgLocAdded to set
	 */
	public void setAvgLocAdded(double avgLocAdded) {
		this.avgLocAdded = avgLocAdded;
	}

	/**
	 * @return the churn
	 */
	public int getChurn() {
		return churn;
	}

	/**
	 * @param churn the churn to set
	 */
	public void setChurn(int churn) {
		this.churn = churn;
	}

	/**
	 * @return the maxChurn
	 */
	public int getMaxChurn() {
		return maxChurn;
	}

	/**
	 * @param maxChurn the maxChurn to set
	 */
	public void setMaxChurn(int maxChurn) {
		this.maxChurn = maxChurn;
	}

	/**
	 * @return the avgChurn
	 */
	public double getAvgChurn() {
		return avgChurn;
	}

	/**
	 * @param avgChurn the avgChurn to set
	 */
	public void setAvgChurn(double avgChurn) {
		this.avgChurn = avgChurn;
	}

	/**
	 * @return the addedLinesList
	 */
	public List<Integer> getAddedLinesList() {
		return addedLinesList;
	}

	/**
	 * @param addedLinesList the addedLinesList to set
	 */
	public void setAddedLinesList(List<Integer> addedLinesList) {
		this.addedLinesList = addedLinesList;
	}

	/**
	 * @return the deletedLinesList
	 */
	public List<Integer> getDeletedLinesList() {
		return deletedLinesList;
	}

	/**
	 * @param deletedLinesList the deletedLinesList to set
	 */
	public void setDeletedLinesList(List<Integer> deletedLinesList) {
		this.deletedLinesList = deletedLinesList;
	}

}
