package deliverable.control;

import java.util.ArrayList;
import java.util.List;

import deliverable.model.ClassifierEvaluation;
import deliverable.utils.ClassifierEvaluationUtil;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.supervised.instance.SpreadSubsample;

public class RetrieveWekaInfo {
	
	private String projName;
	private int numIter;
	
	public RetrieveWekaInfo(String projName, int numIter) {
		this.projName = projName;
		this.numIter = numIter;
		
	}
	
	public List<ClassifierEvaluation> retrieveClassifiersEvaluation() throws Exception {
		
		List<ClassifierEvaluation> simpleRandomForestList = new ArrayList<>();
		List<ClassifierEvaluation> simpleNaiveBayesList = new ArrayList<>();
		List<ClassifierEvaluation> simpleIBkList = new ArrayList<>();
		List<ClassifierEvaluation> featureSelRandomForestList = new ArrayList<>();
		List<ClassifierEvaluation> featureSelNaiveBayesList = new ArrayList<>();
		List<ClassifierEvaluation> featureSelIBkList = new ArrayList<>();
		List<ClassifierEvaluation> samplingRandomForestList = new ArrayList<>();
		List<ClassifierEvaluation> samplingNaiveBayesList = new ArrayList<>();
		List<ClassifierEvaluation> samplingIBkList = new ArrayList<>();
		
		for(int i=1; i<=this.numIter; i++) {
			//VALIDATION WITHOUT FEATURE SELECTION AND WITHOUT SAMPLING
			DataSource source1 = new DataSource(this.projName + "_TR" + i + ".arff");
			DataSource source2 = new DataSource(this.projName + "_TE" + i + ".arff");
			Instances training = source1.getDataSet();
			Instances testing = source2.getDataSet();
			
			RandomForest randomForestClassifier = new RandomForest();
			NaiveBayes naiveBayesClassifier = new NaiveBayes();
			IBk ibkClassifier = new IBk();
			
			Evaluation eval = new Evaluation(testing);	
			
			int numAttr = training.numAttributes();
			training.setClassIndex(numAttr - 1);
			testing.setClassIndex(numAttr - 1);

			randomForestClassifier.buildClassifier(training);		
			eval.evaluateModel(randomForestClassifier, testing);			
			ClassifierEvaluation simpleRandomForest = new ClassifierEvaluation(this.projName, i, "Random Forest", false, false);
			simpleRandomForest.setPrecision(eval.precision(0));
			simpleRandomForest.setRecall(eval.recall(0));
			simpleRandomForest.setAuc(eval.areaUnderROC(0));
			simpleRandomForest.setKappa(eval.kappa());
			simpleRandomForestList.add(simpleRandomForest);
			
			naiveBayesClassifier.buildClassifier(training);		
			eval.evaluateModel(naiveBayesClassifier, testing);		
			ClassifierEvaluation simpleNaiveBayes = new ClassifierEvaluation(this.projName, i, "Naive Bayes", false, false);
			simpleNaiveBayes.setPrecision(eval.precision(0));
			simpleNaiveBayes.setRecall(eval.recall(0));
			simpleNaiveBayes.setAuc(eval.areaUnderROC(0));
			simpleNaiveBayes.setKappa(eval.kappa());
			simpleNaiveBayesList.add(simpleNaiveBayes);
			
			ibkClassifier.buildClassifier(training);		
			eval.evaluateModel(ibkClassifier, testing);
			ClassifierEvaluation simpleIBk = new ClassifierEvaluation(this.projName, i, "IBk", false, false);
			simpleIBk.setPrecision(eval.precision(0));
			simpleIBk.setRecall(eval.recall(0));
			simpleIBk.setAuc(eval.areaUnderROC(0));
			simpleIBk.setKappa(eval.kappa());
			simpleIBkList.add(simpleIBk);
			
			
			//VALIDATION WITH FEATURE SELECTION (GREEDY BACKWARD SEARCH) AND WITHOUT SAMPLING
			CfsSubsetEval subsetEval = new CfsSubsetEval();
			GreedyStepwise search = new GreedyStepwise();
			search.setSearchBackwards(true);
			
			AttributeSelection filter = new AttributeSelection();
			filter.setEvaluator(subsetEval);
			filter.setSearch(search);
			filter.setInputFormat(training);	
			
			Instances filteredTraining = Filter.useFilter(training, filter);
			Instances filteredTesting = Filter.useFilter(testing, filter);
			
			int numAttrFiltered = filteredTraining.numAttributes();
			filteredTraining.setClassIndex(numAttrFiltered - 1);		
			
			randomForestClassifier.buildClassifier(filteredTraining);		
			eval.evaluateModel(randomForestClassifier, filteredTesting);
			ClassifierEvaluation featureSelRandomForest = new ClassifierEvaluation(this.projName, i, "Random Forest", true, false);
			featureSelRandomForest.setPrecision(eval.precision(0));
			featureSelRandomForest.setRecall(eval.recall(0));
			featureSelRandomForest.setAuc(eval.areaUnderROC(0));
			featureSelRandomForest.setKappa(eval.kappa());
			featureSelRandomForestList.add(featureSelRandomForest);
			
			naiveBayesClassifier.buildClassifier(filteredTraining);		
			eval.evaluateModel(naiveBayesClassifier, filteredTesting);
			ClassifierEvaluation featureSelNaiveBayes = new ClassifierEvaluation(this.projName, i, "Naive Bayes", true, false);
			featureSelNaiveBayes.setPrecision(eval.precision(0));
			featureSelNaiveBayes.setRecall(eval.recall(0));
			featureSelNaiveBayes.setAuc(eval.areaUnderROC(0));
			featureSelNaiveBayes.setKappa(eval.kappa());
			featureSelNaiveBayesList.add(featureSelNaiveBayes);
			
			ibkClassifier.buildClassifier(filteredTraining);		
			eval.evaluateModel(ibkClassifier, filteredTesting);
			ClassifierEvaluation featureSelIBk = new ClassifierEvaluation(this.projName, i, "IBk", true, false);
			featureSelIBk.setPrecision(eval.precision(0));
			featureSelIBk.setRecall(eval.recall(0));
			featureSelIBk.setAuc(eval.areaUnderROC(0));
			featureSelIBk.setKappa(eval.kappa());
			featureSelIBkList.add(featureSelIBk);
			
		    
			//VALIDATION WITH FEATURE SELECTION (GREEDY BACKWARD SEARCH) AND WITH SAMPLING (UNDERSAMPLING)
			SpreadSubsample spreadSubsample = new SpreadSubsample();
			spreadSubsample.setInputFormat(filteredTraining);
			spreadSubsample.setOptions(new String[] {"-M", "1.0"});
			
			FilteredClassifier fc = new FilteredClassifier();		
			fc.setFilter(spreadSubsample);
			
			fc.setClassifier(randomForestClassifier);			
			fc.buildClassifier(filteredTraining);
			eval.evaluateModel(fc, filteredTesting);
			ClassifierEvaluation samplingRandomForest = new ClassifierEvaluation(this.projName, i, "Random Forest", true, true);
			samplingRandomForest.setPrecision(eval.precision(0));
			samplingRandomForest.setRecall(eval.recall(0));
			samplingRandomForest.setAuc(eval.areaUnderROC(0));
			samplingRandomForest.setKappa(eval.kappa());
			samplingRandomForestList.add(samplingRandomForest);
			
			fc.setClassifier(naiveBayesClassifier);			
			fc.buildClassifier(filteredTraining);
			eval.evaluateModel(fc, filteredTesting);
			ClassifierEvaluation samplingNaiveBayes = new ClassifierEvaluation(this.projName, i, "Naive Bayes", true, true);
			samplingNaiveBayes.setPrecision(eval.precision(0));
			samplingNaiveBayes.setRecall(eval.recall(0));
			samplingNaiveBayes.setAuc(eval.areaUnderROC(0));
			samplingNaiveBayes.setKappa(eval.kappa());
			samplingNaiveBayesList.add(samplingNaiveBayes);
			
			fc.setClassifier(ibkClassifier);			
			fc.buildClassifier(filteredTraining);
			eval.evaluateModel(fc, filteredTesting);
			ClassifierEvaluation samplingIBk = new ClassifierEvaluation(this.projName, i, "IBk", true, true);
			samplingIBk.setPrecision(eval.precision(0));
			samplingIBk.setRecall(eval.recall(0));
			samplingIBk.setAuc(eval.areaUnderROC(0));
			samplingIBk.setKappa(eval.kappa());
			samplingIBkList.add(samplingIBk);
			
		}
		
		List<ClassifierEvaluation> avgEvaluationsList = new ArrayList<>();
		
		avgEvaluationsList.add(ClassifierEvaluationUtil.getAvgEvaluation(simpleRandomForestList));
		avgEvaluationsList.add(ClassifierEvaluationUtil.getAvgEvaluation(simpleNaiveBayesList));
		avgEvaluationsList.add(ClassifierEvaluationUtil.getAvgEvaluation(simpleIBkList));
		avgEvaluationsList.add(ClassifierEvaluationUtil.getAvgEvaluation(featureSelRandomForestList));
		avgEvaluationsList.add(ClassifierEvaluationUtil.getAvgEvaluation(featureSelNaiveBayesList));
		avgEvaluationsList.add(ClassifierEvaluationUtil.getAvgEvaluation(featureSelIBkList));
		avgEvaluationsList.add(ClassifierEvaluationUtil.getAvgEvaluation(samplingRandomForestList));
		avgEvaluationsList.add(ClassifierEvaluationUtil.getAvgEvaluation(samplingNaiveBayesList));
		avgEvaluationsList.add(ClassifierEvaluationUtil.getAvgEvaluation(samplingIBkList));
		
		return avgEvaluationsList;
		
	}

}
