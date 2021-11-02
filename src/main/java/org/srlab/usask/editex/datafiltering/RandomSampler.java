package org.srlab.usask.editex.datafiltering;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

public class RandomSampler {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		readForRandomSample();

	}
	
private static void readForRandomSample() throws Exception {
	

	ICsvListReader listReader = null;
	ICsvListWriter csvWriter = null;
	
	final int SAMPLE_SIZE = 1000;
	    
    try {
    	
    	listReader = new CsvListReader(new FileReader("E:/Projects/SORejectedEdits/EMSE/Database/AnswerEditForSavingWebpage.csv"), CsvPreference.STANDARD_PREFERENCE);
        csvWriter = new CsvListWriter(new FileWriter("E:/Projects/SORejectedEdits/EMSE/Database/AnswerEditForSavingWebpageSampled.csv"),CsvPreference.STANDARD_PREFERENCE);
        
		csvWriter.write("PostId","Score","PostHistoryTypeId","RevisionGUID","CreationDate","UserId","Comment");
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
        	
        	if(duplicateCheck.contains(column1)) {
        		continue;
        	}
        	else {
        		duplicateCheck.add(column1);
                csvWriter.write(column1,column2,column3,column4,column5,column6,column7);
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
            new Optional()
    };
    
    return processors;
}

}
