package deliverable.model;

public class ClassifierEvaluation {
	
	private String projName;
	private int walkForwardIterationIndex;
	private String classifier;
	private boolean featureSelection;
	private boolean sampling;
	private double precision;
	private double recall;
	private double auc;
	private double kappa;
	
	public ClassifierEvaluation(String projName, int index, String classifier, boolean featureSelection, boolean sampling) {
		this.projName = projName;
		this.walkForwardIterationIndex = index;
		this.classifier = classifier;
		this.featureSelection = featureSelection;
		this.sampling = sampling;
		
		this.precision = 0;
		this.recall = 0;
		this.auc = 0;
		this.kappa = 0;
		
	}
	
	/**
	 * @return the projName
	 */
	public String getProjName() {
		return projName;
	}
	/**
	 * @param projName the projName to set
	 */
	public void setProjName(String projName) {
		this.projName = projName;
	}
	/**
	 * @return the walkForwardIterationIndex
	 */
	public int getWalkForwardIterationIndex() {
		return walkForwardIterationIndex;
	}
	/**
	 * @param walkForwardIterationIndex the walkForwardIterationIndex to set
	 */
	public void setWalkForwardIterationIndex(int walkForwardIterationIndex) {
		this.walkForwardIterationIndex = walkForwardIterationIndex;
	}
	/**
	 * @return the classifier
	 */
	public String getClassifier() {
		return classifier;
	}
	/**
	 * @param classifier the classifier to set
	 */
	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}
	/**
	 * @return the featureSelection
	 */
	public boolean isFeatureSelection() {
		return featureSelection;
	}
	/**
	 * @param featureSelection the featureSelection to set
	 */
	public void setFeatureSelection(boolean featureSelection) {
		this.featureSelection = featureSelection;
	}
	/**
	 * @return the sampling
	 */
	public boolean isSampling() {
		return sampling;
	}
	/**
	 * @param sampling the sampling to set
	 */
	public void setSampling(boolean sampling) {
		this.sampling = sampling;
	}
	/**
	 * @return the precision
	 */
	public double getPrecision() {
		return precision;
	}
	/**
	 * @param precision the precision to set
	 */
	public void setPrecision(double precision) {
		this.precision = precision;
	}
	/**
	 * @return the recall
	 */
	public double getRecall() {
		return recall;
	}
	/**
	 * @param recall the recall to set
	 */
	public void setRecall(double recall) {
		this.recall = recall;
	}
	/**
	 * @return the auc
	 */
	public double getAuc() {
		return auc;
	}
	/**
	 * @param auc the auc to set
	 */
	public void setAuc(double auc) {
		this.auc = auc;
	}
	/**
	 * @return the kappa
	 */
	public double getKappa() {
		return kappa;
	}
	/**
	 * @param kappa the kappa to set
	 */
	public void setKappa(double kappa) {
		this.kappa = kappa;
	}

}
