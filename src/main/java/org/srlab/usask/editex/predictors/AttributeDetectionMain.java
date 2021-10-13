package org.srlab.usask.editex.predictors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pmml4s.model.Model;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AttributeDetectionMain {
	
	public static void main(String[] args) {
		ICsvListReader editReader = null;
		//ICsvListWriter csvWriter = null;
		try {
			
			//csvWriter = new CsvListWriter(new FileWriter("E:/Projects/SORejectedEdits/EMSE/DevelopClassifier/Features/AcceptedEdit___Answers.csv"),CsvPreference.STANDARD_PREFERENCE);
			//csvWriter = new CsvListWriter(new FileWriter("E:/Projects/SORejectedEdits/EMSE/DevelopClassifier/Features/RejectedEdit.csv"),CsvPreference.STANDARD_PREFERENCE);
			
			editReader = new CsvListReader(new FileReader("./testdata/testdataset.csv"),CsvPreference.STANDARD_PREFERENCE);
			//editReader = new CsvListReader(new FileReader("E:/Projects/SORejectedEdits/EMSE/DevelopClassifier/Dataset/RollbackEdits.csv"),CsvPreference.STANDARD_PREFERENCE);
			
			//csvWriter.write("id","text-format","text-change","code-format","code-change","gratitude","greetings","status","deprecation","duplication","signature","inactive-link","ref-modification","deface-post","complete-change","reputation" );
			
			editReader.getHeader(true);
			
			final CellProcessor[] processors = getProcessors();
			List<Object> editList;
			
			Model model = Model.fromFile("./model/model.pmml");
			
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
				
				int editDistance=0;
				int textFormatting=0;
				float textChange=0;
				int codeFormatting=0;
				float codeChange=0;
				int gratitude=0;
				int greeting=0;
				
				int status=0;
				int deprecation=0;
				int duplication=0;
				int signature=0;

				int inactiveLink=0;
				int referenceModification=0;
				
				int defacePost=0;
				int completeChange=0;
				
				int reputation=0;

				
				try {
					postId = editList.get(0).toString().trim();
	    								
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					preEditDoc = Jsoup.parse(editList.get(4).toString());					
	    			preText = preEditDoc.select("p");	    			
					preEditText = preText.text().toString();

            		preCode = preEditDoc.select("pre");
            		preEditCode = preCode.text();
            							
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					postEditDoc = Jsoup.parse(editList.get(5).toString());
	    			postText = postEditDoc.select("p");
					postEditText = postText.text().toString();
					
					postCode = postEditDoc.select("pre");
					postEditCode = postCode.text();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					String[] rollbackUN = editList.get(7).toString().trim().split(" ");
					rollbackUserName = rollbackUN[0];
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					userID = editList.get(9).toString().trim();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					String[] rejectedUN = editList.get(10).toString().trim().split(" ");
					rejectedEditUserName = rejectedUN[0];
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// Attribute Detector
				
				if(preEditText != null && !preEditText.isEmpty() && postEditText != null && !postEditText.isEmpty()){
					
					editDistance = LevenshteinDistance.calculate(preEditText.toLowerCase(), postEditText.toLowerCase());		

					if(editDistance==0) {
					textFormatting = 1;
					}
				}
				editDistance = 0; //reset edit distance value
				
				if(preText != null || postText != null) {
					editDistance = LevenshteinDistance.calculate(preText.toString(), postText.toString());
					if(preText.toString().length() !=0) {
						textChange = (editDistance/(float)preText.toString().length())*100;
					}
				}
				
				if(preEditCode != null && !preEditCode.isEmpty() && postEditCode != null && !postEditCode.isEmpty()){
				
					editDistance = LevenshteinDistance.calculate(preEditCode.toLowerCase(), postEditCode.toLowerCase());		

					if(editDistance==0) {
					codeFormatting = 1;
					}
				}
				
				editDistance = 0; //reset edit distance value
				if(preCode != null || postCode != null) {
					editDistance = LevenshteinDistance.calculate(preCode.toString(), postCode.toString());
					if(preCode.toString().length() !=0) {
						codeChange = (editDistance/(float)preCode.toString().length())*100;
					}
				}
				
				gratitude = GratitudeDetection.detectGratitute(preEditText.toLowerCase(), postEditText.toLowerCase());
				greeting = GreetingsDetection.detectGreetings(preEditText.toLowerCase(), postEditText.toLowerCase());
				
				status = StatusDetection.detectStatus(preEditText.toLowerCase(), postEditText.toLowerCase());
				deprecation = DeprecationDetecton.detectDeprecation(preEditText.toLowerCase(), postEditText.toLowerCase());
				duplication = DuplicationDetection.detectDeprecation(preEditText.toLowerCase(), postEditText.toLowerCase());
				signature = SignatureDetection.detectSignature(preEditText, postEditText, rollbackUserName, rejectedEditUserName);
				
				inactiveLink = EditHyperLink.detectInactiveHyperlink(preEditText, postEditText);
				referenceModification= EditHyperLink.detectHyperLinkModification(preEditText, postEditText);

				if((preEditText==null && preEditCode==null) || (postEditText==null && postEditCode==null)) {
					defacePost =1;
				}
				
				if((preEditText !=null && !preEditText.isEmpty()) || (postEditText!=null && !postEditText.isEmpty())) {
					
					editDistance = 0; //reset edit distance value
					editDistance = LevenshteinDistance.calculate(preEditText, postEditText);
					
					if(editDistance == preEditText.length() || editDistance == postEditText.length()) {
						completeChange = 1;
					}
				}
				
				if((preEditCode !=null && !preEditCode.isEmpty()) || (postEditCode!=null && !postEditCode.isEmpty())) {
					
					editDistance = 0; //reset edit distance value
					editDistance = LevenshteinDistance.calculate(preEditCode, postEditCode);
					
					if(editDistance == preEditCode.length() || editDistance == postEditCode.length()) {
						completeChange = 1;
					}					
				}
				
				reputation = UserReputation.calculateReputation(userID);
				
				//csvWriter.write(postId,textFormatting,textChange,codeFormatting,codeChange,gratitude,greeting,status,deprecation,duplication,signature,inactiveLink,referenceModification,defacePost,completeChange,reputation);
				
				Map<String, Object> feature = new HashMap<String, Object>();
				
				double rejected, accepted;
				
				feature.put("text-format", textFormatting);
				feature.put("text-change", textChange);
				feature.put("code-format", codeFormatting);
				feature.put("code-change", codeChange);
				feature.put("gratitude", gratitude);
				feature.put("greetings", greeting);
				feature.put("status", status);
				feature.put("deprecation", deprecation);
				feature.put("duplication", duplication);
				feature.put("signature", signature);
				feature.put("inactive-link", inactiveLink);
				feature.put("ref-modification", referenceModification);
				feature.put("deface-post", defacePost);
				feature.put("complete-change", completeChange);
				feature.put("reputation", reputation);
				feature.put("emotion", 0);
				
				Map<String, Object> results = model.predict(feature);
				
//				for (String key: results.keySet()) {
//					System.out.println(key+" "+results.get(key));
//				}
				
				rejected = (double) results.get("probability(1)");
				accepted = (double) results.get("probability(0)");
				
				if(rejected >= accepted) {
					System.out.println("Rejected with probability:"+rejected);
				}else {
					System.out.println("Accepted with probability:"+accepted);
				}
				
				}
				
				//csvWriter.close();
				//System.out.println("Feature Extraction Successful !!!!");
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
