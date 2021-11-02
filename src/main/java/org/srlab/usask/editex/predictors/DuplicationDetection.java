package org.srlab.usask.editex.predictors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DuplicationDetection {
	
	public static int detectDeprecation(String preEditText, String postEditText){
		int duplication=0;
		
		try {
			
			Pattern duplicatePattern = Pattern.compile("(\\bduplicate\\b|\\bduplication\\b)");
			Matcher duplicateMatcherPreText = duplicatePattern.matcher(preEditText);
			Matcher duplicateMatcherPostText = duplicatePattern.matcher(postEditText);
			
			List<String> preDuplicateList = new ArrayList<String>();
			List<String> postDuplicateList = new ArrayList<String>();
			
			while(duplicateMatcherPreText.find()) {
				preDuplicateList.add(duplicateMatcherPreText.group(0));
			}						
			
			while(duplicateMatcherPostText.find()) {
				postDuplicateList.add(duplicateMatcherPostText.group(0));
			}

			if(!preDuplicateList.equals(postDuplicateList)) {
				duplication=1;
			}
			else {
				duplication=0;
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return duplication;
	}

}
