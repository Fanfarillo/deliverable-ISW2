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
				evaluationsList.get(0).isFeatureSelection(), evaluationsList.get(0).isSampling(), evaluationsList.get(0).isCostSensitive());
		
		double precisionSum = 0;
		double recallSum = 0;
		double aucSum = 0;
		double kappaSum = 0;
		
		double tpSum = 0;
		double fpSum = 0;
		double tnSum = 0;
		double fnSum = 0;
		
		int numAucAveraged = 0;
		
		for(ClassifierEvaluation evaluation : evaluationsList) {
			Double currentAuc = evaluation.getAuc();
			
			precisionSum = precisionSum + evaluation.getPrecision();
			recallSum = recallSum + evaluation.getRecall();
			kappaSum = kappaSum + evaluation.getKappa();
			
			tpSum = tpSum + evaluation.getTp();
			fpSum = fpSum + evaluation.getFp();
			tnSum = tnSum + evaluation.getTn();
			fnSum = fnSum + evaluation.getFn();
			
			//There are also AUC equal to NaN (this happens when there are no positive instances in testing set)
			if(!currentAuc.isNaN()) {		
				aucSum = aucSum + evaluation.getAuc();
				numAucAveraged++;
			}
			
		}
		avgEvaluation.setPrecision(precisionSum/evaluationsList.size());
		avgEvaluation.setRecall(recallSum/evaluationsList.size());
		avgEvaluation.setKappa(kappaSum/evaluationsList.size());
		
		avgEvaluation.setTp(tpSum/evaluationsList.size());
		avgEvaluation.setFp(fpSum/evaluationsList.size());
		avgEvaluation.setTn(tnSum/evaluationsList.size());
		avgEvaluation.setFn(fnSum/evaluationsList.size());
		
		if(numAucAveraged != 0) {
			avgEvaluation.setAuc(aucSum/numAucAveraged);
		}
		else {
			avgEvaluation.setAuc(0);
		}		
		
		return avgEvaluation;
		
	} 

}
