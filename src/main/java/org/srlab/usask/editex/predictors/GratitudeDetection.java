package org.srlab.usask.editex.predictors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GratitudeDetection {
	
	public static int detectGratitute(String preEditText, String postEditText){
		int gratitute=0;
		
		try {
			
			Pattern gratitudePattern = Pattern.compile("(\\bwelcome\\b|\\bthanks\\b|\\bsorry\\b|\\bappreciated\\b|\\bthank\\b|\\bty\\b|\\bthx\\b|\\bregards\\b|\\btia\\b)");
//			Matcher gratitudeMatcherPreText = gratitudePattern.matcher(preEditText);
			Matcher gratitudeMatcherPostText = gratitudePattern.matcher(postEditText);
			
//			List<String> preGratitudeList = new ArrayList<String>();
//			List<String> postGratitudeList = new ArrayList<String>();
			
//			while(gratitudeMatcherPreText.find()) {
//				preGratitudeList.add(gratitudeMatcherPreText.group(0));
//			}						
			
			if(gratitudeMatcherPostText.find()) {
				//postGratitudeList.add(gratitudeMatcherPostText.group(0));
				gratitute=1;
			}
//			if(!preGratitudeList.equals(postGratitudeList)){
//				gratitute=1;
//			}
			else {
				gratitute=0;
				
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return gratitute;
		
	}

}
