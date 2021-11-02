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

public class FilterData {
	public static void main(String[] args) {
		FilterUniqueData.filterUniqueData();
	}

}

class FilterUniqueData{
		
	public static void filterUniqueData() {
		
		ICsvListReader listReader = null;
		ICsvListWriter csvWriter = null;
		
		List<String> postIDList = new ArrayList<String>();
		
		postIDList = LoadDataList.loadDataList();
		
		try {
			listReader = new CsvListReader(new FileReader("E:/Projects/SORejectedEdits/EMSE/Database/SODataStore/AnswerEdit.csv"), CsvPreference.STANDARD_PREFERENCE);
			listReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();
			List<Object> editList;
			
			csvWriter = new CsvListWriter(new FileWriter("E:/Projects/SORejectedEdits/EMSE/Database/AnswerEditForSavingWebpage.csv"),CsvPreference.STANDARD_PREFERENCE);
			csvWriter.write("PostId","Score","PostHistoryTypeId","RevisionGUID","CreationDate","UserId","Comment");

			while((editList = listReader.read(processors))!= null) {

				String postId = "-9999";
				String score = "-9999";
				String postHistoryTypeId = "-9999";
				String revisionGUID = "-9999";
				String creationDate = "-9999";
				String userId = "-9999";
				String comment = "-9999";

				

				try {
					postId = editList.get(1).toString().trim();
					System.out.println(postId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					score = editList.get(2).toString().trim();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					postHistoryTypeId = editList.get(3).toString().trim();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					revisionGUID = editList.get(4).toString().trim();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					creationDate = editList.get(5).toString().trim();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					userId = editList.get(6).toString().trim();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					comment = editList.get(7).toString().trim();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(postId != null && !postIDList.contains(postId)) {
					postIDList.add(postId);
					
					csvWriter.write(postId,score,postHistoryTypeId,revisionGUID,creationDate,userId,comment);
				}
			}
			csvWriter.close();
			System.out.println("Write Successful!!");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
		
	private static CellProcessor[] getProcessors() {
        
	    final CellProcessor[] processors = new CellProcessor[] {
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

class LoadDataList{
	
	public static List<String> loadDataList() {
		
		ICsvListReader listReader = null;
		List<String> dataList = new ArrayList<String>();
		
		try {
			listReader = new CsvListReader(new FileReader("E:/Projects/SORejectedEdits/EMSE/Database/AnswerRollback.csv"), CsvPreference.STANDARD_PREFERENCE);
			listReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();
			List<Object> editList;
	
			while((editList = listReader.read(processors))!= null) {
	
				String postId = "-9999";
	
				try {
					postId = editList.get(0).toString().trim();
					dataList.add(postId);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}		
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
		
	}
		
	private static CellProcessor[] getProcessors() {
	    
	    final CellProcessor[] processors = new CellProcessor[] {
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
