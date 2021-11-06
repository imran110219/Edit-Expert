package org.srlab.usask.editex.predictors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GreetingsDetection {
	
	public static int detectGreetings(String preEditText, String postEditText){
		int greeting=0;
		
		try {
			
			Pattern greetingsPattern = Pattern.compile("(\\bhi\\b|\\bhello\\b|\\bhey\\b|\\bdear\\b|\\bgreetings\\b|\\bhai\\b|\\bguys\\b|\\bhii\\b|\\bhowdy\\b|\\bhiya\\b|\\bhay\\b|\\bheya\\b|\\bhola\\b|\\bhihi\\b|\\bsalutations\\b)");
			//Matcher greetingsMatcherPreText = greetingsPattern.matcher(preEditText);
			Matcher greetingsMatcherPostText = greetingsPattern.matcher(postEditText);
			
//			List<String> preGreetingsList = new ArrayList<String>();
//			List<String> postGreetingsList = new ArrayList<String>();
			
//			while(greetingsMatcherPreText.find()) {
//				preGreetingsList.add(greetingsMatcherPreText.group(0));
//			}						
			
			if(greetingsMatcherPostText.find()) {
				//postGreetingsList.add(greetingsMatcherPostText.group(0));
				greeting=1;
			}
//			if(!preGreetingsList.equals(postGreetingsList)){
//				greeting=1;
//			}
			else {
				greeting=0;
				
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return greeting;
		
	}

}

