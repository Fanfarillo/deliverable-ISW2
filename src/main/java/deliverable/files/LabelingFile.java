package deliverable.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import deliverable.enums.CsvNamesEnum;
import deliverable.model.JavaClass;

public class LabelingFile {
	
	private String projName;
	private CsvNamesEnum csvName;
	private int csvIndex;
	private List<JavaClass> javaClassesList;
	
	public LabelingFile(String projName, CsvNamesEnum csvName, int csvIndex, List<JavaClass> javaClassesList) {
		this.projName = projName;
		this.csvName = csvName;
		this.csvIndex = csvIndex;
		this.javaClassesList = javaClassesList;
		
	}
	
	private String enumToString() {
		
		switch(csvName) {
		
		case TRAINING:
			return "_TR" + csvIndex;
		case TESTING:
			return "_TE" + csvIndex;
		case BUGGY:
			return "_buggy_classes";
		case CURRENT:
			return "_current_classes";
		default:
			return null;
		
		}
		
	}
	
	public Sheet writeOnCsv() throws IOException {
			
		Sheet sheet;
		String csvNameStr = enumToString();
		Workbook wb = new HSSFWorkbook();
		
		try(OutputStream os = new FileOutputStream(this.projName + csvNameStr + ".csv")) {
			sheet = wb.createSheet(this.projName);
                
	        for(int i=-1; i<this.javaClassesList.size(); i++) {
	        	Row row = sheet.createRow(i+1);		//i = row index - 1
	        	
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

	        	if(i==-1) {	        		
	    	        cell0.setCellValue("JAVA_CLASS");
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
		        	cell0.setCellValue(this.javaClassesList.get(i).getName());
		        	cell1.setCellValue(this.javaClassesList.get(i).getRelease().getId());
		        	cell2.setCellValue(this.javaClassesList.get(i).getSize());
		        	cell3.setCellValue(this.javaClassesList.get(i).getNr());
		        	cell4.setCellValue(this.javaClassesList.get(i).getnAuth());
		        	cell5.setCellValue(this.javaClassesList.get(i).getLocAdded());
		        	cell6.setCellValue(this.javaClassesList.get(i).getMaxLocAdded());
		        	cell7.setCellValue(this.javaClassesList.get(i).getAvgLocAdded());
		        	cell8.setCellValue(this.javaClassesList.get(i).getChurn());
		        	cell9.setCellValue(this.javaClassesList.get(i).getMaxChurn());
		        	cell10.setCellValue(this.javaClassesList.get(i).getAvgChurn());
		        	cell11.setCellValue(this.javaClassesList.get(i).isBuggy());
	        		
	        	}	    
	        	
	        }	        
	        wb.write(os);	//Write on file Excel
			
		}
		return sheet;
		
	}
	
	public void writeOnArff(boolean deleteCsv) throws IOException {
		
		String csvNameStr = enumToString();
		Sheet sheet = writeOnCsv();		
		FileWriter wr = new FileWriter(this.projName + csvNameStr + ".arff");
		
		wr.write("@relation " + this.projName + csvNameStr + "\n");
		wr.write("@attribute SIZE numeric\n");
		wr.write("@attribute NR numeric\n");
		wr.write("@attribute N_AUTH numeric\n");
		wr.write("@attribute LOC_ADDED numeric\n");
		wr.write("@attribute MAX_LOC_ADDED numeric\n");
		wr.write("@attribute AVG_LOC_ADDED numeric\n");
		wr.write("@attribute CHURN numeric\n");
		wr.write("@attribute MAX_CHURN numeric\n");
		wr.write("@attribute AVG_CHURN numeric\n");
		wr.write("@attribute IS_BUGGY {'true', 'false'}\n");
		wr.write("@data\n");
		
		for (int r=1; r<=sheet.getLastRowNum(); r++){
			Row row = sheet.getRow(r);
			
			Double val2 = row.getCell(2).getNumericCellValue();
			Double val3 = row.getCell(3).getNumericCellValue();
			Double val4 = row.getCell(4).getNumericCellValue();
			Double val5 = row.getCell(5).getNumericCellValue();
			Double val6 = row.getCell(6).getNumericCellValue();
			Double val7 = row.getCell(7).getNumericCellValue();
			Double val8 = row.getCell(8).getNumericCellValue();
			Double val9 = row.getCell(9).getNumericCellValue();
			Double val10 = row.getCell(10).getNumericCellValue();
			Boolean val11 = row.getCell(11).getBooleanCellValue();
			
			wr.write(val2.toString() + "," + val3.toString() + "," + val4.toString() + "," + val5.toString() + "," + val6.toString() + "," +
					val7.toString() + "," + val8.toString() + "," + val9.toString() + "," + val10.toString() + "," + val11.toString() + "\n");
			
		}
		wr.close();
		
		if(deleteCsv) {
			File csvFile = new File(this.projName + csvNameStr + ".csv");
			csvFile.delete();
		}
		
	}

}