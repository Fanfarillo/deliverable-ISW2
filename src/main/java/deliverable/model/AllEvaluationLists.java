package deliverable.model;

import java.util.ArrayList;
import java.util.List;

public class AllEvaluationLists {
	
	private List<ClassifierEvaluation> simpleRandomForestList;
	private List<ClassifierEvaluation> simpleNaiveBayesList;
	private List<ClassifierEvaluation> simpleIBkList;
	private List<ClassifierEvaluation> featureSelRandomForestList;
	private List<ClassifierEvaluation> featureSelNaiveBayesList;
	private List<ClassifierEvaluation> featureSelIBkList;
	private List<ClassifierEvaluation> samplingRandomForestList;
	private List<ClassifierEvaluation> samplingNaiveBayesList;
	private List<ClassifierEvaluation> samplingIBkList;
	private List<ClassifierEvaluation> costSensRandomForestList;
	private List<ClassifierEvaluation> costSensNaiveBayesList;
	private List<ClassifierEvaluation> costSensIBkList;
	
	private List<ClassifierEvaluation> avgEvaluationsList;
	private List<ClassifierEvaluation> mergeEvaluationsList = new ArrayList<>();
	
	public void mergeAll() {
		this.mergeEvaluationsList.addAll(simpleRandomForestList);
		this.mergeEvaluationsList.addAll(simpleNaiveBayesList);
		this.mergeEvaluationsList.addAll(simpleIBkList);
		this.mergeEvaluationsList.addAll(featureSelRandomForestList);
		this.mergeEvaluationsList.addAll(featureSelNaiveBayesList);
		this.mergeEvaluationsList.addAll(featureSelIBkList);
		this.mergeEvaluationsList.addAll(samplingRandomForestList);
		this.mergeEvaluationsList.addAll(samplingNaiveBayesList);
		this.mergeEvaluationsList.addAll(samplingIBkList);
		this.mergeEvaluationsList.addAll(costSensRandomForestList);
		this.mergeEvaluationsList.addAll(costSensNaiveBayesList);
		this.mergeEvaluationsList.addAll(costSensIBkList);
		
	}
	
	/**
	 * @return the simpleRandomForestList
	 */
	public List<ClassifierEvaluation> getSimpleRandomForestList() {
		return simpleRandomForestList;
	}
	/**
	 * @param simpleRandomForestList the simpleRandomForestList to set
	 */
	public void setSimpleRandomForestList(List<ClassifierEvaluation> simpleRandomForestList) {
		this.simpleRandomForestList = simpleRandomForestList;
	}
	/**
	 * @return the simpleNaiveBayesList
	 */
	public List<ClassifierEvaluation> getSimpleNaiveBayesList() {
		return simpleNaiveBayesList;
	}
	/**
	 * @param simpleNaiveBayesList the simpleNaiveBayesList to set
	 */
	public void setSimpleNaiveBayesList(List<ClassifierEvaluation> simpleNaiveBayesList) {
		this.simpleNaiveBayesList = simpleNaiveBayesList;
	}
	/**
	 * @return the simpleIBkList
	 */
	public List<ClassifierEvaluation> getSimpleIBkList() {
		return simpleIBkList;
	}
	/**
	 * @param simpleIBkList the simpleIBkList to set
	 */
	public void setSimpleIBkList(List<ClassifierEvaluation> simpleIBkList) {
		this.simpleIBkList = simpleIBkList;
	}
	/**
	 * @return the featureSelRandomForestList
	 */
	public List<ClassifierEvaluation> getFeatureSelRandomForestList() {
		return featureSelRandomForestList;
	}
	/**
	 * @param featureSelRandomForestList the featureSelRandomForestList to set
	 */
	public void setFeatureSelRandomForestList(List<ClassifierEvaluation> featureSelRandomForestList) {
		this.featureSelRandomForestList = featureSelRandomForestList;
	}
	/**
	 * @return the featureSelNaiveBayesList
	 */
	public List<ClassifierEvaluation> getFeatureSelNaiveBayesList() {
		return featureSelNaiveBayesList;
	}
	/**
	 * @param featureSelNaiveBayesList the featureSelNaiveBayesList to set
	 */
	public void setFeatureSelNaiveBayesList(List<ClassifierEvaluation> featureSelNaiveBayesList) {
		this.featureSelNaiveBayesList = featureSelNaiveBayesList;
	}
	/**
	 * @return the featureSelIBkList
	 */
	public List<ClassifierEvaluation> getFeatureSelIBkList() {
		return featureSelIBkList;
	}
	/**
	 * @param featureSelIBkList the featureSelIBkList to set
	 */
	public void setFeatureSelIBkList(List<ClassifierEvaluation> featureSelIBkList) {
		this.featureSelIBkList = featureSelIBkList;
	}
	/**
	 * @return the samplingRandomForestList
	 */
	public List<ClassifierEvaluation> getSamplingRandomForestList() {
		return samplingRandomForestList;
	}
	/**
	 * @param samplingRandomForestList the samplingRandomForestList to set
	 */
	public void setSamplingRandomForestList(List<ClassifierEvaluation> samplingRandomForestList) {
		this.samplingRandomForestList = samplingRandomForestList;
	}
	/**
	 * @return the samplingNaiveBayesList
	 */
	public List<ClassifierEvaluation> getSamplingNaiveBayesList() {
		return samplingNaiveBayesList;
	}
	/**
	 * @param samplingNaiveBayesList the samplingNaiveBayesList to set
	 */
	public void setSamplingNaiveBayesList(List<ClassifierEvaluation> samplingNaiveBayesList) {
		this.samplingNaiveBayesList = samplingNaiveBayesList;
	}
	/**
	 * @return the samplingIBkList
	 */
	public List<ClassifierEvaluation> getSamplingIBkList() {
		return samplingIBkList;
	}
	/**
	 * @param samplingIBkList the samplingIBkList to set
	 */
	public void setSamplingIBkList(List<ClassifierEvaluation> samplingIBkList) {
		this.samplingIBkList = samplingIBkList;
	}
	/**
	 * @return the costSensRandomForestList
	 */
	public List<ClassifierEvaluation> getCostSensRandomForestList() {
		return costSensRandomForestList;
	}
	/**
	 * @param costSensRandomForestList the costSensRandomForestList to set
	 */
	public void setCostSensRandomForestList(List<ClassifierEvaluation> costSensRandomForestList) {
		this.costSensRandomForestList = costSensRandomForestList;
	}
	/**
	 * @return the costSensNaiveBayesList
	 */
	public List<ClassifierEvaluation> getCostSensNaiveBayesList() {
		return costSensNaiveBayesList;
	}
	/**
	 * @param costSensNaiveBayesList the costSensNaiveBayesList to set
	 */
	public void setCostSensNaiveBayesList(List<ClassifierEvaluation> costSensNaiveBayesList) {
		this.costSensNaiveBayesList = costSensNaiveBayesList;
	}
	/**
	 * @return the costSensIBkList
	 */
	public List<ClassifierEvaluation> getCostSensIBkList() {
		return costSensIBkList;
	}
	/**
	 * @param costSensIBkList the costSensIBkList to set
	 */
	public void setCostSensIBkList(List<ClassifierEvaluation> costSensIBkList) {
		this.costSensIBkList = costSensIBkList;
	}
	/**
	 * @return the avgEvaluationsList
	 */
	public List<ClassifierEvaluation> getAvgEvaluationsList() {
		return avgEvaluationsList;
	}
	/**
	 * @param avgEvaluationsList the avgEvaluationsList to set
	 */
	public void setAvgEvaluationsList(List<ClassifierEvaluation> avgEvaluationsList) {
		this.avgEvaluationsList = avgEvaluationsList;
	}

	/**
	 * @return the mergeEvaluationsList
	 */
	public List<ClassifierEvaluation> getMergeEvaluationsList() {
		return mergeEvaluationsList;
	}

	/**
	 * @param mergeEvaluationsList the mergeEvaluationsList to set
	 */
	public void setMergeEvaluationsList(List<ClassifierEvaluation> mergeEvaluationsList) {
		this.mergeEvaluationsList = mergeEvaluationsList;
	}
	

}
