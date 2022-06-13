package deliverable.files;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import deliverable.model.ClassifierEvaluation;

public class EvaluationFile {

	private String projName;
	private List<ClassifierEvaluation> evaluationsList;
	private String description;
	
	public EvaluationFile(String projName, List<ClassifierEvaluation> evaluationList, String description) {
		this.projName = projName;
		this.evaluationsList = evaluationList;
		this.description = description;
		
	}
	
	public void reportEvaluationOnCsv() throws IOException {
		
		Workbook wb = new HSSFWorkbook();		
		try(OutputStream os = new FileOutputStream(this.projName + "_classifiers_report_" + this.description + ".csv")) {
			Sheet sheet = wb.createSheet(this.projName);
			
	        for(int i=-1; i<this.evaluationsList.size(); i++) {
	        	Row row = sheet.createRow(i+1);		//i = row index - 1
	        	
	        	Cell cell00 = row.createCell(0);
	        	Cell cell01 = row.createCell(1);
	        	Cell cell02 = row.createCell(2);
	        	Cell cell03 = row.createCell(3);
	        	Cell cell04 = row.createCell(4);
	        	Cell cell05 = row.createCell(5);
	        	Cell cell06 = row.createCell(6);
	        	Cell cell07 = row.createCell(7);
	        	Cell cell08 = row.createCell(8);
	        	Cell cell09 = row.createCell(9);
	        	Cell cell10 = row.createCell(10);
	        	Cell cell11 = row.createCell(11);
	        	Cell cell12 = row.createCell(12);
	        	Cell cell13 = row.createCell(13);
	        	Cell cell14 = row.createCell(14);

	        	if(i==-1) {	        		
	    	        cell00.setCellValue("DATASET");
	    	        cell01.setCellValue("#TRAINING_RELEASES");
	    	        cell02.setCellValue("%TRAINING_INSTANCES");
	    	        cell03.setCellValue("CLASSIFIER");
	    	        cell04.setCellValue("FEATURE_SELECTION");
	    	        cell05.setCellValue("BALANCING");
	    	        cell06.setCellValue("COST_SENSITIVE");
	    	        cell07.setCellValue("PRECISION");
	    	        cell08.setCellValue("RECALL");
	    	        cell09.setCellValue("AUC");
	    	        cell10.setCellValue("KAPPA");
	    	        cell11.setCellValue("TP");
	    	        cell12.setCellValue("FP");
	    	        cell13.setCellValue("TN");
	    	        cell14.setCellValue("FN");	    	        
	    	        continue;	 
	    	        
	        	}

	        	cell00.setCellValue(this.projName);
	        	if(this.description.equals("details")) {
	       			cell01.setCellValue(this.evaluationsList.get(i).getWalkForwardIterationIndex());
	       			cell02.setCellValue(this.evaluationsList.get(i).getTrainingPercent());
	       		}
	       		else {
	       			cell01.setCellValue("None");
	       			cell02.setCellValue("None");
	        	}
		       	cell03.setCellValue(this.evaluationsList.get(i).getClassifier());
		        	
		       	if(this.evaluationsList.get(i).isFeatureSelection()) {
		       		cell04.setCellValue("Greedy backward search");
		       	}
	        	else {
	        		cell04.setCellValue("None");
	        	}
		        if(this.evaluationsList.get(i).isSampling()) {
		       		cell05.setCellValue("Undersampling");
		       	}
		       	else {
		       		cell05.setCellValue("None");
		       	}
		       	if(this.evaluationsList.get(i).isCostSensitive()) {
		       		cell06.setCellValue("Sensitive learning");
		       	}
	        	else {
	        		cell06.setCellValue("None");
	        	}
		        	
		        cell07.setCellValue(this.evaluationsList.get(i).getPrecision());
		       	cell08.setCellValue(this.evaluationsList.get(i).getRecall());
		       	cell09.setCellValue(this.evaluationsList.get(i).getAuc());
		       	cell10.setCellValue(this.evaluationsList.get(i).getKappa());
		       	cell11.setCellValue(this.evaluationsList.get(i).getTp());
		       	cell12.setCellValue(this.evaluationsList.get(i).getFp());
		       	cell13.setCellValue(this.evaluationsList.get(i).getTn());
		       	cell14.setCellValue(this.evaluationsList.get(i).getFn());   
	        	
	        }	        
	        wb.write(os);	//Write on file Excel
			
		}
		
	}
	
}
