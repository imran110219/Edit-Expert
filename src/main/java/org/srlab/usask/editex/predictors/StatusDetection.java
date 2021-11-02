package org.srlab.usask.editex.predictors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatusDetection{
	
	public static int detectStatus(String preEditText, String postEditText){
		
			int statusFlag = 0;

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
					statusFlag = 1;
				}
				else {
					statusFlag = 0;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(statusFlag == 1) {
				return 1;
			}
			else {
				return 0;
			}
						
		}	
}