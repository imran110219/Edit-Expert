package org.srlab.usask.iedit.inconsistencydetector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetectDuplicationInconsistency {

	public static List<Integer> detectDuplicationInconsistency(String preEditText, String postEditText){
		
		List<Integer> duplicationInconsistency = new ArrayList<Integer>();		
		int duplication=0;
		int duplicationAccept=0;
		int duplicationReject=0;
		
		try {
			
			Pattern duplicationPattern = Pattern.compile("(\\bduplicate\\b|\\bduplication\\b)");
			Matcher duplicationMatcherPreText = duplicationPattern.matcher(preEditText);
			Matcher duplicationMatcherPostText = duplicationPattern.matcher(postEditText);
			
			List<String> preDuplicationList = new ArrayList<String>();
			List<String> postDuplicationList = new ArrayList<String>();
			
			while(duplicationMatcherPreText.find()) {
				preDuplicationList.add(duplicationMatcherPreText.group(0));
			}						
			
			while(duplicationMatcherPostText.find()) {
				postDuplicationList.add(duplicationMatcherPostText.group(0));
			}
			
			if(!preDuplicationList.equals(postDuplicationList)){
				duplication = 1;
				
				if(preDuplicationList.size() < postDuplicationList.size()) {
					duplicationAccept = 1;
				}else {
					duplicationReject = 1;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		duplicationInconsistency.add(duplication);
		duplicationInconsistency.add(duplicationAccept);
		duplicationInconsistency.add(duplicationReject);

		return duplicationInconsistency;	
	}
	
}
