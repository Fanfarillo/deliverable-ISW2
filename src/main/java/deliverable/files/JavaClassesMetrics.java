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

import deliverable.model.JavaClass;

public class JavaClassesMetrics {
	
	private String projName;
	private List<JavaClass> javaClassesList;
	
	public JavaClassesMetrics(String projName, List<JavaClass> javaClassesList) {
		this.projName = projName;
		this.javaClassesList = javaClassesList;
		
	}
	
	public void writeOnCsv() throws IOException {
		
		Workbook wb = new HSSFWorkbook();
		
		try(OutputStream os = new FileOutputStream("Buggy_classes_" + this.projName + ".csv")) {
			Sheet sheet = wb.createSheet(this.projName);
        
	        int i=0;	//Row index	        
	        for(JavaClass javaClass : this.javaClassesList) {
	        	Row row = sheet.createRow(i);
	        	
	        	Cell cell0 = row.createCell(0);
	        	Cell cell1 = row.createCell(1);
	        	Cell cell2 = row.createCell(2);
	        	Cell cell3 = row.createCell(3);
	        	Cell cell4 = row.createCell(4);
	        	Cell cell5 = row.createCell(5);
	        	Cell cell6 = row.createCell(6);
	        	Cell cell7 = row.createCell(7);
    	        Cell cell8 = row.createCell(8);
    	        Cell cell9 = row.createCell(9);
    	        Cell cell10 = row.createCell(10);
    	        Cell cell11 = row.createCell(11);

	        	if(i==0) {	        		
	    	        cell0.setCellValue("CLASS");
	    	        cell1.setCellValue("RELEASE");
	    	        cell2.setCellValue("SIZE");
	    	        cell3.setCellValue("NR");
	    	        cell4.setCellValue("N_AUTH");
	    	        cell5.setCellValue("LOC_ADDED");
	    	        cell6.setCellValue("MAX_LOC_ADDED");
	    	        cell7.setCellValue("AVG_LOC_ADDED");
	    	        cell8.setCellValue("CHURN");
	    	        cell9.setCellValue("MAX_CHURN");
	    	        cell10.setCellValue("AVG_CHURN");
	    	        cell11.setCellValue("IS_BUGGY");
	        		
	        	}
	        	else {		        	
		        	cell0.setCellValue(javaClass.getName());
		        	cell1.setCellValue(javaClass.getRelease().getId());
		        	cell2.setCellValue(javaClass.getSize());
		        	cell3.setCellValue(javaClass.getNr());
		        	cell4.setCellValue(javaClass.getnAuth());
		        	cell5.setCellValue(javaClass.getLocAdded());
		        	cell6.setCellValue(javaClass.getMaxLocAdded());
		        	cell7.setCellValue(javaClass.getAvgLocAdded());
		        	cell8.setCellValue(javaClass.getChurn());
		        	cell9.setCellValue(javaClass.getMaxChurn());
		        	cell10.setCellValue(javaClass.getAvgChurn());
		        	cell11.setCellValue(javaClass.isBuggy());
	        		
	        	}	    
	        	
	        	i++;
	        	
	        }	        
	        wb.write(os);	//Write on file Excel
			
		}
		
	}

}
