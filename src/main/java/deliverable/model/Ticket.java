package deliverable.model;

import java.util.ArrayList;

public class Ticket {

	private String key;
	private Release iv;
	private Release ov;
	private Release fv;
	private ArrayList<Release> av;
	
	public Ticket(String key, Release ov, Release fv, ArrayList<Release> av) {
		this.key = key;
		this.iv = null;
		this.ov = ov;
		this.fv = fv;
		this.av = av;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the iv
	 */
	public Release getIv() {
		return iv;
	}

	/**
	 * @param iv the iv to set
	 */
	public void setIv(Release iv) {
		this.iv = iv;
	}

	/**
	 * @return the ov
	 */
	public Release getOv() {
		return ov;
	}

	/**
	 * @param ov the ov to set
	 */
	public void setOv(Release ov) {
		this.ov = ov;
	}

	/**
	 * @return the fv
	 */
	public Release getFv() {
		return fv;
	}

	/**
	 * @param fv the fv to set
	 */
	public void setFv(Release fv) {
		this.fv = fv;
	}

	/**
	 * @return the av
	 */
	public ArrayList<Release> getAv() {
		return av;
	}

	/**
	 * @param av the av to set
	 */
	public void setAv(ArrayList<Release> av) {
		this.av = av;
	}
	
}
