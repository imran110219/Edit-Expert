package org.srlab.usask.editex.predictors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeprecationDetecton {
		
	public static int detectDeprecation(String preEditText, String postEditText){
		int deprecation=0;
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
									
			if(!preDeprecationList.equals(postDeprecationList)) {
				deprecation=1;
			}
			else {
				deprecation=0;
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return deprecation;
	}

}
