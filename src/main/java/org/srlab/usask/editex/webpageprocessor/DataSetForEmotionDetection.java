package org.srlab.usask.editex.webpageprocessor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

public class DataSetForEmotionDetection {
	
	public static void main(String[] args) {
		ICsvListReader editReader = null;
		//ICsvListWriter csvWriter = null;
		
				
		try {
			
			//csvWriter = new CsvListWriter(new FileWriter("E:/Projects/SORejectedEdits/EMSE/DevelopClassifier/Features/EditExFeaturesRejectedEditGrat.csv"),CsvPreference.STANDARD_PREFERENCE);
			//csvWriter = new CsvListWriter(new FileWriter("E:/Projects/SORejectedEdits/EMSE/DevelopClassifier/Features/EditExFeaturesRejectedEdit.csv"),CsvPreference.STANDARD_PREFERENCE);
			
			//csvWriter.write("id","Text");
			PrintWriter myFileWriter = new PrintWriter("E:/Projects/SORejectedEdits/EMSE/SEntiMoji/AcceptedEditsForEmotion.txt","UTF-8");
			
			editReader = new CsvListReader(new FileReader("E:/Projects/SORejectedEdits/EMSE/DevelopClassifier/Dataset/AcceptedEdits.csv"),CsvPreference.STANDARD_PREFERENCE);
			//editReader = new CsvListReader(new FileReader("E:/Projects/SORejectedEdits/EMSE/DevelopClassifier/Dataset/RollbackEdits.csv"),CsvPreference.STANDARD_PREFERENCE);
						
			editReader.getHeader(true);
			
			final CellProcessor[] processors = getProcessors();
			List<Object> editList;
			
			while((editList = editReader.read(processors)) != null) {
				String preEditText = "";
				String postEditText = "";
				String postId = "";
				
				Document preEditDoc = null;
				Document postEditDoc = null;
				
				String singleLineString = "";
				
				try {
					postId = editList.get(0).toString().trim();
					System.out.println(postId);
	    								
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					preEditDoc = Jsoup.parse(editList.get(4).toString());
	    			Elements preText = preEditDoc.select("p");
					preEditText = preText.text().toString();
					
					singleLineString = preEditText.replaceAll("[\r\n]+", " ");
					
					if(singleLineString.equals("") || singleLineString.isEmpty()) {
						singleLineString = "I have only code.";
						System.out.println(singleLineString);
					}					
					myFileWriter.write(postId+"\t"+singleLineString+"\n");
					
			        //System.out.println(singleLineString); 
			        //Thread.sleep(5000);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					postEditDoc = Jsoup.parse(editList.get(5).toString());
	    			Elements postText = postEditDoc.select("p");
					postEditText = postText.text().toString().toLowerCase();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				//csvWriter.write(postId,textFormatting);
				
				}
			
				//csvWriter.close();
				
				if(myFileWriter!= null) {
					myFileWriter.close();
				}
				
				System.out.println("Feature Extraction Successful !!!!");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
			
		finally {
			try {
				if(editReader != null) {
					editReader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}		
	}
	private static CellProcessor[] getProcessors() {
		final CellProcessor[] processor = new CellProcessor[] {
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
		return processor;
	}
}

