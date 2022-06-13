package deliverable.control;

import java.util.ArrayList;
import java.util.List;

import deliverable.model.AllEvaluationLists;
import deliverable.model.ClassifierEvaluation;
import deliverable.utils.ClassifierEvaluationUtil;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.CostMatrix;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.supervised.instance.SpreadSubsample;

public class RetrieveWekaInfo {
	
	private static final String RANDOM_FOREST = "Random Forest";
	private static final String NAIVE_BAYES = "Naive Bayes";
	private static final String IBK = "IBk";
	
	private String projName;
	private int numIter;
	
	public RetrieveWekaInfo(String projName, int numIter) {
		this.projName = projName;
		this.numIter = numIter;
		
	}
	
	public AllEvaluationLists retrieveClassifiersEvaluation() throws Exception {
		
		List<ClassifierEvaluation> simpleRandomForestList = new ArrayList<>();
		List<ClassifierEvaluation> simpleNaiveBayesList = new ArrayList<>();
		List<ClassifierEvaluation> simpleIBkList = new ArrayList<>();
		List<ClassifierEvaluation> featureSelRandomForestList = new ArrayList<>();
		List<ClassifierEvaluation> featureSelNaiveBayesList = new ArrayList<>();
		List<ClassifierEvaluation> featureSelIBkList = new ArrayList<>();
		List<ClassifierEvaluation> samplingRandomForestList = new ArrayList<>();
		List<ClassifierEvaluation> samplingNaiveBayesList = new ArrayList<>();
		List<ClassifierEvaluation> samplingIBkList = new ArrayList<>();
		List<ClassifierEvaluation> costSensRandomForestList = new ArrayList<>();
		List<ClassifierEvaluation> costSensNaiveBayesList = new ArrayList<>();
		List<ClassifierEvaluation> costSensIBkList = new ArrayList<>();
		
		for(int i=1; i<=this.numIter; i++) {
			//VALIDATION WITHOUT FEATURE SELECTION AND WITHOUT SAMPLING
			DataSource source1 = new DataSource(this.projName + "_TR" + i + ".arff");
			DataSource source2 = new DataSource(this.projName + "_TE" + i + ".arff");
			Instances training = source1.getDataSet();
			Instances testing = source2.getDataSet();
			
			RandomForest randomForestClassifier = new RandomForest();
			NaiveBayes naiveBayesClassifier = new NaiveBayes();
			IBk ibkClassifier = new IBk();	
			
			int numAttr = training.numAttributes();
			training.setClassIndex(numAttr - 1);
			testing.setClassIndex(numAttr - 1);
			
			Evaluation eval = new Evaluation(testing);

			randomForestClassifier.buildClassifier(training);		
			eval.evaluateModel(randomForestClassifier, testing);			
			ClassifierEvaluation simpleRandomForest = new ClassifierEvaluation(this.projName, i, RANDOM_FOREST, false, false, false);
			simpleRandomForest.setTrainingPercent(100.0*training.numInstances()/(training.numInstances()+testing.numInstances()));
			simpleRandomForest.setPrecision(eval.precision(0));
			simpleRandomForest.setRecall(eval.recall(0));
			simpleRandomForest.setAuc(eval.areaUnderROC(0));
			simpleRandomForest.setKappa(eval.kappa());
			simpleRandomForest.setTp(eval.numTruePositives(0));
			simpleRandomForest.setFp(eval.numFalsePositives(0));
			simpleRandomForest.setTn(eval.numTrueNegatives(0));
			simpleRandomForest.setFn(eval.numFalseNegatives(0));
			simpleRandomForestList.add(simpleRandomForest);
			
			naiveBayesClassifier.buildClassifier(training);		
			eval.evaluateModel(naiveBayesClassifier, testing);		
			ClassifierEvaluation simpleNaiveBayes = new ClassifierEvaluation(this.projName, i, NAIVE_BAYES, false, false, false);
			simpleNaiveBayes.setTrainingPercent(100.0*training.numInstances()/(training.numInstances()+testing.numInstances()));
			simpleNaiveBayes.setPrecision(eval.precision(0));
			simpleNaiveBayes.setRecall(eval.recall(0));
			simpleNaiveBayes.setAuc(eval.areaUnderROC(0));
			simpleNaiveBayes.setKappa(eval.kappa());
			simpleNaiveBayes.setTp(eval.numTruePositives(0));
			simpleNaiveBayes.setFp(eval.numFalsePositives(0));
			simpleNaiveBayes.setTn(eval.numTrueNegatives(0));
			simpleNaiveBayes.setFn(eval.numFalseNegatives(0));
			simpleNaiveBayesList.add(simpleNaiveBayes);
			
			ibkClassifier.buildClassifier(training);		
			eval.evaluateModel(ibkClassifier, testing);
			ClassifierEvaluation simpleIBk = new ClassifierEvaluation(this.projName, i, IBK, false, false, false);
			simpleIBk.setTrainingPercent(100.0*training.numInstances()/(training.numInstances()+testing.numInstances()));
			simpleIBk.setPrecision(eval.precision(0));
			simpleIBk.setRecall(eval.recall(0));
			simpleIBk.setAuc(eval.areaUnderROC(0));
			simpleIBk.setKappa(eval.kappa());
			simpleIBk.setTp(eval.numTruePositives(0));
			simpleIBk.setFp(eval.numFalsePositives(0));
			simpleIBk.setTn(eval.numTrueNegatives(0));
			simpleIBk.setFn(eval.numFalseNegatives(0));
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
			ClassifierEvaluation featureSelRandomForest = new ClassifierEvaluation(this.projName, i, RANDOM_FOREST, true, false, false);
			featureSelRandomForest.setTrainingPercent(100.0*filteredTraining.numInstances()/(filteredTraining.numInstances()+filteredTesting.numInstances()));
			featureSelRandomForest.setPrecision(eval.precision(0));
			featureSelRandomForest.setRecall(eval.recall(0));
			featureSelRandomForest.setAuc(eval.areaUnderROC(0));
			featureSelRandomForest.setKappa(eval.kappa());
			featureSelRandomForest.setTp(eval.numTruePositives(0));
			featureSelRandomForest.setFp(eval.numFalsePositives(0));
			featureSelRandomForest.setTn(eval.numTrueNegatives(0));
			featureSelRandomForest.setFn(eval.numFalseNegatives(0));
			featureSelRandomForestList.add(featureSelRandomForest);
			
			naiveBayesClassifier.buildClassifier(filteredTraining);		
			eval.evaluateModel(naiveBayesClassifier, filteredTesting);
			ClassifierEvaluation featureSelNaiveBayes = new ClassifierEvaluation(this.projName, i, NAIVE_BAYES, true, false, false);
			featureSelNaiveBayes.setTrainingPercent(100.0*filteredTraining.numInstances()/(filteredTraining.numInstances()+filteredTesting.numInstances()));
			featureSelNaiveBayes.setPrecision(eval.precision(0));
			featureSelNaiveBayes.setRecall(eval.recall(0));
			featureSelNaiveBayes.setAuc(eval.areaUnderROC(0));
			featureSelNaiveBayes.setKappa(eval.kappa());
			featureSelNaiveBayes.setTp(eval.numTruePositives(0));
			featureSelNaiveBayes.setFp(eval.numFalsePositives(0));
			featureSelNaiveBayes.setTn(eval.numTrueNegatives(0));
			featureSelNaiveBayes.setFn(eval.numFalseNegatives(0));
			featureSelNaiveBayesList.add(featureSelNaiveBayes);
			
			ibkClassifier.buildClassifier(filteredTraining);		
			eval.evaluateModel(ibkClassifier, filteredTesting);
			ClassifierEvaluation featureSelIBk = new ClassifierEvaluation(this.projName, i, IBK, true, false, false);
			featureSelIBk.setTrainingPercent(100.0*filteredTraining.numInstances()/(filteredTraining.numInstances()+filteredTesting.numInstances()));
			featureSelIBk.setPrecision(eval.precision(0));
			featureSelIBk.setRecall(eval.recall(0));
			featureSelIBk.setAuc(eval.areaUnderROC(0));
			featureSelIBk.setKappa(eval.kappa());
			featureSelIBk.setTp(eval.numTruePositives(0));
			featureSelIBk.setFp(eval.numFalsePositives(0));
			featureSelIBk.setTn(eval.numTrueNegatives(0));
			featureSelIBk.setFn(eval.numFalseNegatives(0));
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
			ClassifierEvaluation samplingRandomForest = new ClassifierEvaluation(this.projName, i, RANDOM_FOREST, true, true, false);
			samplingRandomForest.setTrainingPercent(100.0*filteredTraining.numInstances()/(filteredTraining.numInstances()+filteredTesting.numInstances()));
			samplingRandomForest.setPrecision(eval.precision(0));
			samplingRandomForest.setRecall(eval.recall(0));
			samplingRandomForest.setAuc(eval.areaUnderROC(0));
			samplingRandomForest.setKappa(eval.kappa());
			samplingRandomForest.setTp(eval.numTruePositives(0));
			samplingRandomForest.setFp(eval.numFalsePositives(0));
			samplingRandomForest.setTn(eval.numTrueNegatives(0));
			samplingRandomForest.setFn(eval.numFalseNegatives(0));
			samplingRandomForestList.add(samplingRandomForest);
			
			fc.setClassifier(naiveBayesClassifier);			
			fc.buildClassifier(filteredTraining);
			eval.evaluateModel(fc, filteredTesting);
			ClassifierEvaluation samplingNaiveBayes = new ClassifierEvaluation(this.projName, i, NAIVE_BAYES, true, true, false);
			samplingNaiveBayes.setTrainingPercent(100.0*filteredTraining.numInstances()/(filteredTraining.numInstances()+filteredTesting.numInstances()));
			samplingNaiveBayes.setPrecision(eval.precision(0));
			samplingNaiveBayes.setRecall(eval.recall(0));
			samplingNaiveBayes.setAuc(eval.areaUnderROC(0));
			samplingNaiveBayes.setKappa(eval.kappa());
			samplingNaiveBayes.setTp(eval.numTruePositives(0));
			samplingNaiveBayes.setFp(eval.numFalsePositives(0));
			samplingNaiveBayes.setTn(eval.numTrueNegatives(0));
			samplingNaiveBayes.setFn(eval.numFalseNegatives(0));
			samplingNaiveBayesList.add(samplingNaiveBayes);
			
			fc.setClassifier(ibkClassifier);			
			fc.buildClassifier(filteredTraining);
			eval.evaluateModel(fc, filteredTesting);
			ClassifierEvaluation samplingIBk = new ClassifierEvaluation(this.projName, i, IBK, true, true, false);
			samplingIBk.setTrainingPercent(100.0*filteredTraining.numInstances()/(filteredTraining.numInstances()+filteredTesting.numInstances()));
			samplingIBk.setPrecision(eval.precision(0));
			samplingIBk.setRecall(eval.recall(0));
			samplingIBk.setAuc(eval.areaUnderROC(0));
			samplingIBk.setKappa(eval.kappa());
			samplingIBk.setTp(eval.numTruePositives(0));
			samplingIBk.setFp(eval.numFalsePositives(0));
			samplingIBk.setTn(eval.numTrueNegatives(0));
			samplingIBk.setFn(eval.numFalseNegatives(0));
			samplingIBkList.add(samplingIBk);
			
			//VALIDATION WITH FEATURE SELECTION (GREEDY BACKWARD SEARCH) AND WITH SENSITIVE LEARNING (CFN = 10*CFP)
			CostMatrix costMatrix = new CostMatrix(2);
			costMatrix.setCell(0, 0, 0.0);
			costMatrix.setCell(1, 0, 10.0);
			costMatrix.setCell(0, 1, 1.0);
			costMatrix.setCell(1, 1, 0.0);
			
			CostSensitiveClassifier csc = new CostSensitiveClassifier();
			
			csc.setClassifier(randomForestClassifier);
			csc.setCostMatrix(costMatrix);
			csc.buildClassifier(filteredTraining);
			eval.evaluateModel(csc, filteredTesting);
			ClassifierEvaluation costSensRandomForest = new ClassifierEvaluation(this.projName, i, RANDOM_FOREST, true, false, true);
			costSensRandomForest.setTrainingPercent(100.0*filteredTraining.numInstances()/(filteredTraining.numInstances()+filteredTesting.numInstances()));
			costSensRandomForest.setPrecision(eval.precision(0));
			costSensRandomForest.setRecall(eval.recall(0));
			costSensRandomForest.setAuc(eval.areaUnderROC(0));
			costSensRandomForest.setKappa(eval.kappa());
			costSensRandomForest.setTp(eval.numTruePositives(0));
			costSensRandomForest.setFp(eval.numFalsePositives(0));
			costSensRandomForest.setTn(eval.numTrueNegatives(0));
			costSensRandomForest.setFn(eval.numFalseNegatives(0));
			costSensRandomForestList.add(costSensRandomForest);
			
			csc.setClassifier(naiveBayesClassifier);
			csc.setCostMatrix(costMatrix);
			csc.buildClassifier(filteredTraining);
			eval.evaluateModel(csc, filteredTesting);
			ClassifierEvaluation costSensNaiveBayes = new ClassifierEvaluation(this.projName, i, NAIVE_BAYES, true, false, true);
			costSensNaiveBayes.setTrainingPercent(100.0*filteredTraining.numInstances()/(filteredTraining.numInstances()+filteredTesting.numInstances()));
			costSensNaiveBayes.setPrecision(eval.precision(0));
			costSensNaiveBayes.setRecall(eval.recall(0));
			costSensNaiveBayes.setAuc(eval.areaUnderROC(0));
			costSensNaiveBayes.setKappa(eval.kappa());
			costSensNaiveBayes.setTp(eval.numTruePositives(0));
			costSensNaiveBayes.setFp(eval.numFalsePositives(0));
			costSensNaiveBayes.setTn(eval.numTrueNegatives(0));
			costSensNaiveBayes.setFn(eval.numFalseNegatives(0));
			costSensNaiveBayesList.add(costSensNaiveBayes);
			
			csc.setClassifier(ibkClassifier);
			csc.setCostMatrix(costMatrix);
			csc.buildClassifier(filteredTraining);
			eval.evaluateModel(csc, filteredTesting);
			ClassifierEvaluation costSensIBk = new ClassifierEvaluation(this.projName, i, IBK, true, false, true);
			costSensIBk.setTrainingPercent(100.0*filteredTraining.numInstances()/(filteredTraining.numInstances()+filteredTesting.numInstances()));
			costSensIBk.setPrecision(eval.precision(0));
			costSensIBk.setRecall(eval.recall(0));
			costSensIBk.setAuc(eval.areaUnderROC(0));
			costSensIBk.setKappa(eval.kappa());
			costSensIBk.setTp(eval.numTruePositives(0));
			costSensIBk.setFp(eval.numFalsePositives(0));
			costSensIBk.setTn(eval.numTrueNegatives(0));
			costSensIBk.setFn(eval.numFalseNegatives(0));
			costSensIBkList.add(costSensIBk);
			
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
		avgEvaluationsList.add(ClassifierEvaluationUtil.getAvgEvaluation(costSensRandomForestList));
		avgEvaluationsList.add(ClassifierEvaluationUtil.getAvgEvaluation(costSensNaiveBayesList));
		avgEvaluationsList.add(ClassifierEvaluationUtil.getAvgEvaluation(costSensIBkList));
		
		AllEvaluationLists allLists = new AllEvaluationLists();
		
		allLists.setAvgEvaluationsList(avgEvaluationsList);
		allLists.setCostSensIBkList(costSensIBkList);
		allLists.setCostSensNaiveBayesList(costSensNaiveBayesList);
		allLists.setCostSensRandomForestList(costSensRandomForestList);
		allLists.setFeatureSelIBkList(featureSelIBkList);
		allLists.setFeatureSelNaiveBayesList(featureSelNaiveBayesList);
		allLists.setFeatureSelRandomForestList(featureSelRandomForestList);
		allLists.setSamplingIBkList(samplingIBkList);
		allLists.setSamplingNaiveBayesList(samplingNaiveBayesList);
		allLists.setSamplingRandomForestList(samplingRandomForestList);
		allLists.setSimpleIBkList(simpleIBkList);
		allLists.setSimpleNaiveBayesList(simpleNaiveBayesList);
		allLists.setSimpleRandomForestList(simpleRandomForestList);
		allLists.mergeAll();
		
		return allLists;
		
	}

}
