package org.srlab.usask.editex.statsfrommanualanalysis;

import java.io.FileReader;
import java.util.List;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

public class FindRollbackReasonStats {

	public static void main(String[] args) {
		AnswerRollbackStats obj = new AnswerRollbackStats();
		obj.start();
	}

}

class AnswerRollbackStats extends Thread{
	
	ICsvListReader listReader = null;
	ICsvListWriter csvWriter = null;
	
	
	public void run() {
		
		try {
					
 			listReader = new CsvListReader(new FileReader("E:/Projects/SORejectedEdits/EMSE/Database/SODataStore/AnswerRollback_Manually_Analyzed.csv"), CsvPreference.STANDARD_PREFERENCE);
			listReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();
			List<Object> editList;
			int total = 0;		
			int txtFormat = 0;
			int txtAddRemove = 0;
			int incTxtChange = 0;
			int txtChange = 0;
			int codeFormat = 0;
			int codeAddRemove = 0;
			int codeChange = 0;
			int incCodeChange = 0;
			int statusUpdate = 0;
			int emoAddRemove = 0;
			int greetingsAddRemove = 0;
			int parAcceptance = 0;
			int sigAddRemove = 0;
			int refModification = 0;
			int duplicationNote = 0;
			int deprecationNote = 0;
			int other = 0;
						
			while((editList = listReader.read(processors))!= null) {

				String reason1 = "";
				String reason2 = "";
				String reason3 = "";
				String reason4 = "";
							
				try {
					reason1 = editList.get(8).toString().trim();
					
					if(reason1.equals("Undesired Text Formatting")) txtFormat++;
					if(reason1.equals("Undesired Text Addition") || reason1.equals("Undesired Text Remove")) txtAddRemove++;
					if(reason1.equals("Incorret Text Change")) incTxtChange++;
					if(reason1.equals("Undesired Text Change")) txtChange++;
					if(reason1.equals("Undesired Code Format") || reason1.equals("UD Code Format")) codeFormat++;
					if(reason1.equals("Undesired Code Add") || reason1.equals("Undesired Code Removal") || reason1.equals("UD Code Add/Removal")) codeAddRemove++;
					if(reason1.equals("UD code change") || reason1.equals("Undesired Code Change")) codeChange++;
					if(reason1.equals("Incorrect Code Change") || reason1.equals("Inc Code Change")) incCodeChange++;
					if(reason1.equals("Status add/remove")) statusUpdate++;
					if(reason1.equals("Emotion Remove") || reason1.equals("Emotion Add") || reason1.equals("Gratitude Add/Remove")) emoAddRemove++;
					if(reason1.equals("Greetings Add/Remove")) greetingsAddRemove++;
					if(reason1.equals("Part Accept")) parAcceptance++;
					if(reason1.equals("Signature add/remove")) sigAddRemove++;
					if(reason1.equals("Ref Mod")) refModification++;
					if(reason1.equals("Deprecation Note")) deprecationNote++;
					if(reason1.equals("Duplication Note")) duplicationNote++;					
					if(reason1.equals("Other")) other++;										
	
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					reason2 = editList.get(9).toString().trim();
					
					if(reason2.equals("Undesired Text Formatting")) txtFormat++;
					if(reason2.equals("Undesired Text Addition") || reason1.equals("Undesired Text Remove")) txtAddRemove++;
					if(reason2.equals("Incorret Text Change")) incTxtChange++;
					if(reason2.equals("Undesired Text Change")) txtChange++;
					if(reason2.equals("Undesired Code Format") || reason1.equals("UD Code Format")) codeFormat++;
					if(reason2.equals("Undesired Code Add") || reason1.equals("Undesired Code Removal") || reason1.equals("UD Code Add/Removal")) codeAddRemove++;
					if(reason2.equals("UD code change") || reason2.equals("Undesired Code Change")) codeChange++;
					if(reason2.equals("Incorrect Code Change") || reason1.equals("Inc Code Change")) incCodeChange++;
					if(reason2.equals("Status add/remove")) statusUpdate++;
					if(reason2.equals("Emotion Remove") || reason1.equals("Emotion Add") || reason1.equals("Gratitude Add/Remove")) emoAddRemove++;
					if(reason2.equals("Greetings Add/Remove")) greetingsAddRemove++;
					if(reason2.equals("Part Accept")) parAcceptance++;
					if(reason2.equals("Signature add/remove")) sigAddRemove++;
					if(reason2.equals("Ref Mod")) refModification++;
					if(reason2.equals("Deprecation Note")) deprecationNote++;
					if(reason2.equals("Duplication Note")) duplicationNote++;					
					if(reason2.equals("Other")) other++;
	
				} catch (Exception e) {
//					e.printStackTrace();
				}
				try {
					reason3 = editList.get(10).toString().trim();
					
					if(reason3.equals("Undesired Text Formatting")) txtFormat++;
					if(reason3.equals("Undesired Text Addition") || reason1.equals("Undesired Text Remove")) txtAddRemove++;
					if(reason3.equals("Incorret Text Change")) incTxtChange++;
					if(reason3.equals("Undesired Text Change")) txtChange++;
					if(reason3.equals("Undesired Code Format") || reason1.equals("UD Code Format")) codeFormat++;
					if(reason3.equals("Undesired Code Add") || reason1.equals("Undesired Code Removal") || reason1.equals("UD Code Add/Removal")) codeAddRemove++;
					if(reason3.equals("UD code change") || reason2.equals("Undesired Code Change")) codeChange++;
					if(reason3.equals("Incorrect Code Change") || reason1.equals("Inc Code Change")) incCodeChange++;
					if(reason3.equals("Status add/remove")) statusUpdate++;
					if(reason3.equals("Emotion Remove") || reason1.equals("Emotion Add") || reason1.equals("Gratitude Add/Remove")) emoAddRemove++;
					if(reason3.equals("Greetings Add/Remove")) greetingsAddRemove++;
					if(reason3.equals("Part Accept")) parAcceptance++;
					if(reason3.equals("Signature add/remove")) sigAddRemove++;
					if(reason3.equals("Ref Mod")) refModification++;
					if(reason3.equals("Deprecation Note")) deprecationNote++;
					if(reason3.equals("Duplication Note")) duplicationNote++;					
					if(reason3.equals("Other")) other++;
	
				} catch (Exception e) {
//					e.printStackTrace();
				}
				try {
					reason4 = editList.get(11).toString().trim();
					
					if(reason4.equals("Undesired Text Formatting")) txtFormat++;
					if(reason4.equals("Undesired Text Addition") || reason1.equals("Undesired Text Remove")) txtAddRemove++;
					if(reason4.equals("Incorret Text Change")) incTxtChange++;
					if(reason4.equals("Undesired Text Change")) txtChange++;
					if(reason4.equals("Undesired Code Format") || reason1.equals("UD Code Format")) codeFormat++;
					if(reason4.equals("Undesired Code Add") || reason1.equals("Undesired Code Removal") || reason1.equals("UD Code Add/Removal")) codeAddRemove++;
					if(reason4.equals("UD code change") || reason4.equals("Undesired Code Change")) codeChange++;
					if(reason4.equals("Incorrect Code Change") || reason1.equals("Inc Code Change")) incCodeChange++;
					if(reason4.equals("Status add/remove")) statusUpdate++;
					if(reason4.equals("Emotion Remove") || reason1.equals("Emotion Add") || reason1.equals("Gratitude Add/Remove")) emoAddRemove++;
					if(reason4.equals("Greetings Add/Remove")) greetingsAddRemove++;
					if(reason4.equals("Part Accept")) parAcceptance++;
					if(reason4.equals("Signature add/remove")) sigAddRemove++;
					if(reason4.equals("Ref Mod")) refModification++;
					if(reason4.equals("Deprecation Note")) deprecationNote++;
					if(reason4.equals("Duplication Note")) duplicationNote++;					
					if(reason4.equals("Other")) other++;
	
				} catch (Exception e) {
//					e.printStackTrace();
				}
				
				total++;
			}
			System.out.println("Total:"+total);
			System.out.println("UD Text Format:"+txtFormat/(float)total);
			System.out.println("UD Text Add/Remove:"+txtAddRemove/(float)total);
			System.out.println("Inc. Text Change:"+incTxtChange/(float)total);
			System.out.println("UD Text Change:"+txtChange/(float)total);
			System.out.println("UD Code Format:"+codeFormat/(float)total);
			System.out.println("UD Code Add/Remove:"+codeAddRemove/(float)total);
			System.out.println("UD Code Change:"+codeChange/(float)total);
			System.out.println("Inc. Code Change:"+incCodeChange/(float)total);
			System.out.println("Status Update:"+statusUpdate/(float)total);
			System.out.println("Emotion Add/Remove:"+emoAddRemove/(float)total);
			System.out.println("Greetings Add/Remove:"+greetingsAddRemove/(float)total);
			System.out.println("Partial Acceptance:"+parAcceptance/(float)total);
			System.out.println("Signature Add/Remove:"+sigAddRemove/(float)total);
			System.out.println("Ref. Modification:"+refModification/(float)total);
			System.out.println("Deprecation Note:"+deprecationNote/(float)total);
			System.out.println("Duplication Note:"+duplicationNote/(float)total);
			System.out.println("Other:"+other/(float)total);
			
			/*******************************************************************/
			System.out.println("1 "+(txtFormat/(float)total)*100);
			System.out.println("2 "+(txtAddRemove/(float)total)*100);
			System.out.println("3 "+(incTxtChange/(float)total)*100);
			System.out.println("4 "+(txtChange/(float)total)*100);
			System.out.println("5 "+(codeFormat/(float)total)*100);
			System.out.println("6 "+(codeAddRemove/(float)total)*100);
			System.out.println("7 "+(codeChange/(float)total)*100);
			System.out.println("8 "+(incCodeChange/(float)total)*100);
			System.out.println("9 "+(statusUpdate/(float)total)*100);
			System.out.println("10 "+(emoAddRemove/(float)total)*100);
			System.out.println("11 "+(greetingsAddRemove/(float)total)*100);
			System.out.println("12 "+(parAcceptance/(float)total)*100);
			System.out.println("13 "+(sigAddRemove/(float)total)*100);
			System.out.println("14 "+(refModification/(float)total)*100);
			System.out.println("15 "+(deprecationNote/(float)total)*100);
			System.out.println("16 "+(duplicationNote/(float)total)*100);
			System.out.println("17 "+(other/(float)total)*100);
		
		} catch (Exception e) {
		e.printStackTrace();
		}
		
		finally {
	    	try {
	    		if( listReader != null ) {
	                listReader.close();
	        }	
			} catch (Exception e2) {
				
			}
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

