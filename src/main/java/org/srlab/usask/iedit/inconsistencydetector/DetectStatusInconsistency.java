package org.srlab.usask.iedit.inconsistencydetector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetectStatusInconsistency {

	public static List<Integer> detectStatusInconsistency(String preEditText, String postEditText){
		
		List<Integer> statusInconsistency = new ArrayList<Integer>();		
		int status=0;
		int statusAccept=0;
		int statusReject=0;
		
		try {
			
			Pattern statusPattern = Pattern.compile("(\\bedit|\\bupdate|\\bnote|\\bps)");
			Matcher statusMatcherPreText = statusPattern.matcher(preEditText);
			Matcher statusMatcherPostText = statusPattern.matcher(postEditText);
			
			List<String> preStatusList = new ArrayList<String>();
			List<String> postStatusList = new ArrayList<String>();
			
			while(statusMatcherPreText.find()) {
				preStatusList.add(statusMatcherPreText.group(0));
			}						
			
			while(statusMatcherPostText.find()) {
				postStatusList.add(statusMatcherPostText.group(0));
			}
			
			if(!preStatusList.equals(postStatusList)){
				status = 1;
				
				if(preStatusList.size() < postStatusList.size()) {
					statusAccept = 1;
				}else {
					statusReject = 1;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		statusInconsistency.add(status);
		statusInconsistency.add(statusAccept);
		statusInconsistency.add(statusReject);

		return statusInconsistency;	
	}	
}
