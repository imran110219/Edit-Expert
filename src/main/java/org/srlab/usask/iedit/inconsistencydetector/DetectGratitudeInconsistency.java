package org.srlab.usask.iedit.inconsistencydetector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DetectGratitudeInconsistency extends Thread{
	
	public static List<Integer> detectGratitudeInconsistency(String preEditText, String postEditText){
		
		List<Integer> gratitudeInconsistency = new ArrayList<Integer>();		
		int gratitude=0;
		int gratitudeAccept=0;
		int gratitudeReject=0;
		
		try {
			
			Pattern gratitudePattern = Pattern.compile("(\\bwelcome\\b|\\bthanks\\b|\\bsorry\\b|\\bappreciated\\b|\\bthank\\b|\\bty\\b|\\bthx\\b|\\bregards\\b|\\btia\\b)");
			Matcher gratitudeMatcherPreText = gratitudePattern.matcher(preEditText);
			Matcher gratitudeMatcherPostText = gratitudePattern.matcher(postEditText);
			
			List<String> preGratitudeList = new ArrayList<String>();
			List<String> postGratitudeList = new ArrayList<String>();
			
			while(gratitudeMatcherPreText.find()) {
				preGratitudeList.add(gratitudeMatcherPreText.group(0));
			}						
			
			while(gratitudeMatcherPostText.find()) {
				postGratitudeList.add(gratitudeMatcherPostText.group(0));
			}
			
			if(!preGratitudeList.equals(postGratitudeList)){
				gratitude = 1;
				
				if(preGratitudeList.size() < postGratitudeList.size()) {
					gratitudeAccept = 1;
				}else {
					gratitudeReject = 1;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		gratitudeInconsistency.add(gratitude);
		gratitudeInconsistency.add(gratitudeAccept);
		gratitudeInconsistency.add(gratitudeReject);

		return gratitudeInconsistency;	
	}
}