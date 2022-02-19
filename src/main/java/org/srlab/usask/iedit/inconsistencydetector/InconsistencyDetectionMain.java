package org.srlab.usask.iedit.inconsistencydetector;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
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

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class InconsistencyDetectionMain {

	public static void main(String[] args) {
		ICsvListReader editReader = null;
		ICsvListWriter csvWriter = null;
		try {
			
			csvWriter = new CsvListWriter(new FileWriter("E:/Projects/SORejectedEdits/AUSE/Results/"), CsvPreference.STANDARD_PREFERENCE);
			//csvWriter = new CsvListWriter(new FileWriter(""),CsvPreference.STANDARD_PREFERENCE);
			
			//editReader = new CsvListReader(new FileReader("./testdata/testdataset.csv"),CsvPreference.STANDARD_PREFERENCE);
			editReader = new CsvListReader(new FileReader("E:/Projects/SORejectedEdits/AUSE/Database/LargeScaleDataset/RollbackEdits/Rollback_Edits.csv"), CsvPreference.STANDARD_PREFERENCE);
			
			csvWriter.write("postId",
					        "presentation","preAcc","preRej",
					        "gratitude", "gratAcc", "gratRej",
					        "signature","sigAcc","sigRej",
					        "status", "statusAcc", "statusRej",
					        "deprecation", "depAcc","depRej",
					        "duplication","dupAcc","dupRej", "Structural", "Temporal");
			
			//csvWriter.write("postId","temporal","structural");
			//csvWriter.write("id","reasons");
			
			editReader.getHeader(true);
			
			final CellProcessor[] processors = getProcessors();
			List<Object> editList;
			
			Parser parser = Parser.builder().build();
			int totalCounter = 0;
			while((editList = editReader.read(processors)) != null) {
				
				String postId = "";
				String userID = "";
				String rollbackUserName = "";
				String rejectedEditUserName = "";
				
				Document preEditDoc = null;
				Document postEditDoc = null;
				Elements preText = null;
				Elements preCode = null;
				Elements postText = null;
				Elements postCode = null;
				
				String preEditText = "";
				String postEditText = "";
				String preEditCode = "";
				String postEditCode = "";
				

				List<Integer> gratitudeInconsistency = new ArrayList<Integer>(); //Index 0: inconsistency, Index 1: Accept, Index 2: Reject
				List<Integer> presentationInconsistency = new ArrayList<Integer>(); //Index 0: inconsistency, Index 1: Accept, Index 2: Reject
				List<Integer> deprecationInconsistency = new ArrayList<Integer>();
				List<Integer> duplicationInconsistency = new ArrayList<Integer>();
				List<Integer> statusInconsistency = new ArrayList<Integer>();
				List<Integer> signatureInconsistency = new ArrayList<Integer>();
				
				
//				int status=0;
//				int deprecation=0;
//				int duplication=0;
//				int signature=0;
//
//				int inactiveLink=0;
//				int referenceModification=0;
//				
//				int reputation=0;
//				int rejectionStatus =0;
				
				Node preTextDocument = null;
				Node postTextDocument = null;
				
//				String suggestion = "";
//				int numberOfReason=0;
				
				int currentRev = 0;
				int revisionTo = 0;
								
				try {
					postId = editList.get(0).toString().trim();
					System.out.println(postId);
	    								
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					currentRev = Integer.parseInt(editList.get(2).toString().trim());
	    								
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					revisionTo = Integer.parseInt(editList.get(4).toString().trim());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					preEditDoc = Jsoup.parse(editList.get(7).toString());
					
					//Parse markup to HTML
					preTextDocument = parser.parse(preEditDoc.toString());
					HtmlRenderer renderer = HtmlRenderer.builder().build();
					renderer.render(preTextDocument);  // "<p>This is <em>Sparta</em></p>\n"
					preEditDoc = Jsoup.parse(renderer.render(preTextDocument));
//					System.out.println(preEditDoc);
					
	    			preText = preEditDoc.select("p");	    			
					preEditText = preText.text().toString();

            		preCode = preEditDoc.select("pre");
            		preEditCode = preCode.text();
            		            							
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					postEditDoc = Jsoup.parse(editList.get(8).toString());
					
					//Parse markup to HTML 
					postTextDocument = parser.parse(postEditDoc.toString());
					HtmlRenderer renderer = HtmlRenderer.builder().build();
					renderer.render(postTextDocument);  // "<p>This is <em>Sparta</em></p>\n"
					postEditDoc = Jsoup.parse(renderer.render(postTextDocument));
//					System.out.println(preEditDoc);
					
	    			postText = postEditDoc.select("p");
					postEditText = postText.text().toString();
					
					postCode = postEditDoc.select("pre");
					postEditCode = postCode.text();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					String[] userName = editList.get(10).toString().trim().split(" ");
					rollbackUserName = userName[0];
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
//				try {
//					userID = editList.get(13).toString().trim();
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				
				try {
					String[] userName = editList.get(13).toString().trim().split(" ");
					rejectedEditUserName = userName[0];
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// Attribute Detector
				
				totalCounter++;
				
				presentationInconsistency = DetectPresentationInconsistency.detectPresentationInconsistency(preEditDoc, postEditDoc, preEditText, postEditText);
				gratitudeInconsistency = DetectGratitudeInconsistency.detectGratitudeInconsistency(preEditText.toLowerCase(), postEditText.toLowerCase());
				statusInconsistency = DetectStatusInconsistency.detectStatusInconsistency(preEditText.toLowerCase(), postEditText.toLowerCase());
				signatureInconsistency = DetectSignatureInconsistency.detectSignatureInconsistency(preEditText, postEditText, rollbackUserName, rejectedEditUserName);
				deprecationInconsistency = DetectDeprecationInconsistency.detectDeprecationInconsistency(preEditText.toLowerCase(), postEditText.toLowerCase());
				duplicationInconsistency = DetectDuplicationInconsistency.detectDuplicationInconsistency(preEditText.toLowerCase(), postEditText.toLowerCase());
				
				int structural = 0;
				int temporal = 0;
				
				if((currentRev - revisionTo) !=2 ) {				
					
					if ((currentRev - revisionTo)>2) temporal =1;
					if ((currentRev - revisionTo) == 1) structural =1;
//					csvWriter.write(postId,
//									temporal,
//									structural);
				
				}
						
				if((presentationInconsistency.get(0) + gratitudeInconsistency.get(0)+signatureInconsistency.get(0)
				   +statusInconsistency.get(0)+deprecationInconsistency.get(0)+duplicationInconsistency.get(0)+structural+temporal>0 )) { //|| (currentRev-revisionTo) !=2
				
					csvWriter.write(postId,
									presentationInconsistency.get(0),presentationInconsistency.get(1),presentationInconsistency.get(2),
									gratitudeInconsistency.get(0),gratitudeInconsistency.get(1),gratitudeInconsistency.get(2),
								    signatureInconsistency.get(0), signatureInconsistency.get(1), signatureInconsistency.get(2),
								    statusInconsistency.get(0),statusInconsistency.get(1),statusInconsistency.get(2),
								    deprecationInconsistency.get(0),deprecationInconsistency.get(1),deprecationInconsistency.get(2),
								    duplicationInconsistency.get(0),duplicationInconsistency.get(1),duplicationInconsistency.get(2),
									structural,
									temporal);
					
					} 
				
				 }
				
				csvWriter.close();
				System.out.println("Total:"+totalCounter);
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
			new Optional(),
			new Optional(),
			new Optional(),
			new Optional()
			
		};
		return processor;
	}
}
