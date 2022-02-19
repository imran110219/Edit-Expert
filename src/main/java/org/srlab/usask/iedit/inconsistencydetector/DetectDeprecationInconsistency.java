package org.srlab.usask.iedit.inconsistencydetector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetectDeprecationInconsistency {
	
	public static List<Integer> detectDeprecationInconsistency(String preEditText, String postEditText){
		
		List<Integer> deprecationInconsistency = new ArrayList<Integer>();		
		int deprecation=0;
		int deprecationAccept=0;
		int deprecationReject=0;
		
		try {
			
			Pattern deprecationPattern = Pattern.compile("(\\bdeprecation\\b|\\\\bdeprecate\\\\b)");
			Matcher deprecationMatcherPreText = deprecationPattern.matcher(preEditText);
			Matcher deprecationMatcherPostText = deprecationPattern.matcher(postEditText);
			
			List<String> preDeprecationList = new ArrayList<String>();
			List<String> postDeprecationList = new ArrayList<String>();
			
			while(deprecationMatcherPreText.find()) {
				preDeprecationList.add(deprecationMatcherPreText.group(0));
			}						
			
			while(deprecationMatcherPostText.find()) {
				postDeprecationList.add(deprecationMatcherPostText.group(0));
			}
			
			if(!preDeprecationList.equals(postDeprecationList)){
				deprecation = 1;
				
				if(preDeprecationList.size() < postDeprecationList.size()) {
					deprecationAccept = 1;
				}else {
					deprecationReject = 1;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		deprecationInconsistency.add(deprecation);
		deprecationInconsistency.add(deprecationAccept);
		deprecationInconsistency.add(deprecationReject);

		return deprecationInconsistency;	
	}

}
