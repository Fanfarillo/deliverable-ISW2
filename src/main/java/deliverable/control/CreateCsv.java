package deliverable.control;

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

public class CreateCsv {
	
	private String projName;
	private List<JavaClass> javaClassesList;
	
	public CreateCsv(String projName, List<JavaClass> javaClassesList) {
		this.projName = projName;
		this.javaClassesList = javaClassesList;
		
	}
	
	public void writeOnCsv() throws IOException {
		
		Workbook wb = new HSSFWorkbook();
		
		try(OutputStream os = new FileOutputStream("Buggy_classes_" + this.projName + ".csv")) {
			Sheet sheet = wb.createSheet(this.projName);
			
	        Row row = sheet.createRow(0);	//First row
	        Cell cell = row.createCell(0);  //First column
	        cell.setCellValue("CLASS");
	            
	        cell = row.createCell(1);
	        cell.setCellValue("RELEASE");
	           
	        cell = row.createCell(2);
	        cell.setCellValue("SIZE");
	            
	        cell = row.createCell(3);
	        cell.setCellValue("NR");
	        
	        cell = row.createCell(4);
	        cell.setCellValue("N_AUTH");
	           
	        cell = row.createCell(5);
	        cell.setCellValue("LOC_ADDED");
	            
	        cell = row.createCell(6);
	        cell.setCellValue("MAX_LOC_ADDED");
	        
	        cell = row.createCell(7);
	        cell.setCellValue("AVG_LOC_ADDED");
	        
	        cell = row.createCell(8);
	        cell.setCellValue("CHURN");
	           
	        cell = row.createCell(9);
	        cell.setCellValue("MAX_CHURN");
	            
	        cell = row.createCell(10);
	        cell.setCellValue("AVG_CHURN");
	        
	        cell = row.createCell(11);
	        cell.setCellValue("IS_BUGGY");
	        
	        int i=1;
	        for(JavaClass javaClass : this.javaClassesList) {
	        	row = sheet.createRow(i);
	        	
	        	cell = row.createCell(0);
	        	cell.setCellValue(javaClass.getName());
	        	
	        	cell = row.createCell(1);
	        	cell.setCellValue(javaClass.getRelease().getId());
	        	
	        	cell = row.createCell(2);
	        	cell.setCellValue(javaClass.getSize());
	        	
	        	cell = row.createCell(3);
	        	cell.setCellValue(javaClass.getNr());
	        	
	        	cell = row.createCell(4);
	        	cell.setCellValue(javaClass.getnAuth());
	        	
	        	cell = row.createCell(5);
	        	cell.setCellValue(javaClass.getLocAdded());
	        	
	        	cell = row.createCell(6);
	        	cell.setCellValue(javaClass.getMaxLocAdded());
	        	
	        	cell = row.createCell(7);
	        	cell.setCellValue(javaClass.getAvgLocAdded());
	        	
	        	cell = row.createCell(8);
	        	cell.setCellValue(javaClass.getChurn());
	        	
	        	cell = row.createCell(9);
	        	cell.setCellValue(javaClass.getMaxChurn());
	        	
	        	cell = row.createCell(10);
	        	cell.setCellValue(javaClass.getAvgChurn());
	        	
	        	cell = row.createCell(11);
	        	if(javaClass.isBuggy()) {
	        		cell.setCellValue("Yes");
	        	}
	        	else {
	        		cell.setCellValue("No");
	        	}
	        	
	        	i++;
	        	
	        }
	        
	        wb.write(os);	//Write on file Excel
			
		}
		
	}

}
