package org.srlab.usask.editex.predictors;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SignatureDetection {
	
	public static int detectSignature(String preEditText, String postEditText, String rollbackUserName, String rejectedEditUserName){
		int signature=0;
		
		try {
			Pattern rollbackNamePattern = Pattern.compile(rollbackUserName);
			Matcher rollbackNameMatchPreText = rollbackNamePattern.matcher(preEditText);
			Matcher rollbackNameMatchPostText = rollbackNamePattern.matcher(postEditText);
			
			Pattern rejectedNamePattern = Pattern.compile(rejectedEditUserName);
			Matcher rejectedNameMatchPreText = rejectedNamePattern.matcher(preEditText);
			Matcher rejectedNameMatchPostText = rejectedNamePattern.matcher(postEditText);
			
			if(rollbackNameMatchPreText.find()) {
				if(!rollbackNameMatchPostText.find()) {
					signature =1;
				}
			}
			else if(rollbackNameMatchPostText.find()) {
				if(!rollbackNameMatchPreText.find()) {
					signature =1;
				}
			}
			else if(rejectedNameMatchPostText.find()) {
				if(!rejectedNameMatchPreText.find()) {
					signature =1;
				}
			}
			else if(rejectedNameMatchPreText.find()) {
				if(!rejectedNameMatchPostText.find()) {
					signature=1;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return signature;
		
	}

}
