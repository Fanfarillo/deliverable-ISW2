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
	
	public EvaluationFile(String projName, List<ClassifierEvaluation> evaluationList) {
		this.projName = projName;
		this.evaluationsList = evaluationList;
		
	}
	
	public void reportEvaluationOnCsv() throws IOException {
		
		Workbook wb = new HSSFWorkbook();		
		try(OutputStream os = new FileOutputStream(this.projName + "_classifiers_report.csv")) {
			Sheet sheet = wb.createSheet(this.projName);
			
	        for(int i=-1; i<this.evaluationsList.size(); i++) {
	        	Row row = sheet.createRow(i+1);		//i = row index - 1
	        	
	        	Cell cell0 = row.createCell(0);
	        	Cell cell1 = row.createCell(1);
	        	Cell cell2 = row.createCell(2);
	        	Cell cell3 = row.createCell(3);
	        	Cell cell4 = row.createCell(4);
	        	Cell cell5 = row.createCell(5);
	        	Cell cell6 = row.createCell(6);

	        	if(i==-1) {	        		
	    	        cell0.setCellValue("CLASSIFIER");
	    	        cell1.setCellValue("FEATURE_SELECTION");
	    	        cell2.setCellValue("BALANCING");
	    	        cell3.setCellValue("PRECISION");
	    	        cell4.setCellValue("RECALL");
	    	        cell5.setCellValue("AUC");
	    	        cell6.setCellValue("KAPPA");
	        		
	        	}
	        	else {		        	
		        	cell0.setCellValue(this.evaluationsList.get(i).getClassifier());
		        	if(this.evaluationsList.get(i).isFeatureSelection()) {
		        		cell1.setCellValue("Greedy backward search");
		        	}
		        	else {
		        		cell1.setCellValue("None");
		        	}
		        	if(this.evaluationsList.get(i).isSampling()) {
		        		cell2.setCellValue("Undersampling");
		        	}
		        	else {
		        		cell2.setCellValue("None");
		        	}
		        	cell3.setCellValue(this.evaluationsList.get(i).getPrecision());
		        	cell4.setCellValue(this.evaluationsList.get(i).getRecall());
		        	cell5.setCellValue(this.evaluationsList.get(i).getAuc());
		        	cell6.setCellValue(this.evaluationsList.get(i).getKappa());
	        		
	        	}	    
	        	
	        }	        
	        wb.write(os);	//Write on file Excel
			
		}
		
	}
	
}
