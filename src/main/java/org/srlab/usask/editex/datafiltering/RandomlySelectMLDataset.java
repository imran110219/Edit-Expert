package org.srlab.usask.editex.datafiltering;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import java.util.List;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

public class RandomlySelectMLDataset {

	public static void main(String[] args) throws Exception {
		readForRandomSample();
	}
	
private static void readForRandomSample() throws Exception {
	

	ICsvListReader listReader = null;
	ICsvListWriter csvWriter = null;
	
	final int SAMPLE_SIZE = 385;
	    
    try {
    	
    	listReader = new CsvListReader(new FileReader("E:/Projects/SORejectedEdits/EMSE/DevelopClassifier/Features/AcceptedEdit___Answers.csv"), CsvPreference.STANDARD_PREFERENCE);
        csvWriter = new CsvListWriter(new FileWriter("E:/Projects/SORejectedEdits/EMSE/DevelopClassifier/Features/AcceptedEdit___Answers_Sampled.csv"),CsvPreference.STANDARD_PREFERENCE);
        
		csvWriter.write("id","text-format","text-change","code-format","code-change","gratitude","greetings","status","deprecation","duplication","signature","inactive-link","ref-modification","deface-post","complete-change","reputation");

        listReader.getHeader(true); // skip the header (can't be used with CsvListReader)
        final CellProcessor[] processors = getProcessors();
        List<Object> questionList;
        
        
        List<String> duplicateCheck = new ArrayList<String>();
        
        
        
        int i =0;
        while( (questionList = listReader.read(processors)) != null && i<SAMPLE_SIZE) {
        	    
        	String column1=" ";
        	String column2=" ";
        	String column3=" ";
        	String column4=" ";
        	String column5=" ";
        	String column6=" ";
        	String column7=" ";
        	String column8=" ";
        	String column9=" ";
        	String column10=" ";
        	String column11=" ";
        	String column12=" ";
        	String column13=" ";
        	String column14=" ";
        	String column15=" ";
        	String column16=" ";
        	
        	try {
        		column1=questionList.get(0).toString();
			}catch (Exception e) {
				e.printStackTrace();
			}
        	try {
        		column2=questionList.get(1).toString();
			}catch (Exception e) {
				e.printStackTrace();
			}
        	try {
        		column3=questionList.get(2).toString();
			}catch (Exception e) {
				e.printStackTrace();
			}
        	try {
        		column4=questionList.get(3).toString();
			}catch (Exception e) {
				e.printStackTrace();
			}
        	try {
        		column5=questionList.get(4).toString();
			}catch (Exception e) {
				e.printStackTrace();
			}
        	try {
        		column6=questionList.get(5).toString();
			}catch (Exception e) {
				e.printStackTrace();
			}
        	try {
        		column7=questionList.get(6).toString();
			}catch (Exception e) {
				e.printStackTrace();
			}
        	try {
        		column8=questionList.get(7).toString();
			}catch (Exception e) {
				e.printStackTrace();
			}
        	try {
        		column9=questionList.get(8).toString();
			}catch (Exception e) {
				e.printStackTrace();
			}
        	try {
        		column10=questionList.get(9).toString();
			}catch (Exception e) {
				e.printStackTrace();
			}
        	try {
        		column11=questionList.get(10).toString();
			}catch (Exception e) {
				e.printStackTrace();
			}
        	try {
        		column12=questionList.get(11).toString();
			}catch (Exception e) {
				e.printStackTrace();
			}
        	try {
        		column13=questionList.get(12).toString();
			}catch (Exception e) {
				e.printStackTrace();
			}
        	try {
        		column14=questionList.get(13).toString();
			}catch (Exception e) {
				e.printStackTrace();
			}
        	try {
        		column15=questionList.get(14).toString();
			}catch (Exception e) {
				e.printStackTrace();
			}
        	try {
        		column16=questionList.get(15).toString();
			}catch (Exception e) {
				e.printStackTrace();
			}
        	
        	
        	if(duplicateCheck.contains(column1)) {
        		continue;
        	}
        	else {
        		duplicateCheck.add(column1);
                csvWriter.write(column1,column2,column3,column4,column5,column6,column7,column8,column9,column10,column11,column12,column13,column14,column15,column16);
                i++;
                System.out.println(i+">>"+column1);
        	}
        	       	
        }
        
        csvWriter.close();
        System.out.println("Data Write in File Finished Successfully!!");
       
    }
    finally {
    	
        if( listReader != null ) {
                listReader.close();
        }
    }
}

private static CellProcessor[] getProcessors() {
	         
    final CellProcessor[] processors = new CellProcessor[] {
    		new Optional(), // post Id not null
            new Optional(), 
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional()
    };
    
    return processors;
}

}
