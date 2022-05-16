package deliverable.utils;

import java.util.List;

import deliverable.model.ClassifierEvaluation;

public class ClassifierEvaluationUtil {
	
	//This private constructor is meant to hide the public one: utility classes do not have to be instantiated.
	private ClassifierEvaluationUtil() {
		throw new IllegalStateException("This class does not have to be instantiated.");
	}
	
	/*Callers:
	 * retrieveClassifiersEvaluation (RetrieveWekaInfo)*/
	public static ClassifierEvaluation getAvgEvaluation(List<ClassifierEvaluation> evaluationsList) {
		
		ClassifierEvaluation avgEvaluation = new ClassifierEvaluation(evaluationsList.get(0).getProjName(), 0, evaluationsList.get(0).getClassifier(),
				evaluationsList.get(0).isFeatureSelection(), evaluationsList.get(0).isSampling());
		
		double precisionSum = 0;
		double recallSum = 0;
		double aucSum = 0;
		double kappaSum = 0;
		
		for(ClassifierEvaluation evaluation : evaluationsList) {
			precisionSum = precisionSum + evaluation.getPrecision();
			recallSum = recallSum + evaluation.getRecall();
			aucSum = aucSum + evaluation.getAuc();
			kappaSum = kappaSum + evaluation.getKappa();
			
		}
		avgEvaluation.setPrecision(precisionSum/evaluationsList.size());
		avgEvaluation.setRecall(recallSum/evaluationsList.size());
		avgEvaluation.setAuc(aucSum/evaluationsList.size());
		avgEvaluation.setKappa(kappaSum/evaluationsList.size());
		
		return avgEvaluation;
		
	} 

}
