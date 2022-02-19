package org.srlab.usask.iedit.inconsistencydetector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetectSignatureInconsistency {

	public static List<Integer> detectSignatureInconsistency(String preEditText, String postEditText, String rollbackUserName, String rejectedEditUserName){
		
		List<Integer> signatureInconsistency = new ArrayList<Integer>();		
		int signature=0;
		int signatureAccept=0;
		int signatureReject=0;
		
		try {
			
			Pattern signaturePattern = Pattern.compile("(\\b"+rollbackUserName+"|\\b"+rejectedEditUserName+")");
			Matcher signatureMatcherPreText = signaturePattern.matcher(preEditText);
			Matcher signatureMatcherPostText = signaturePattern.matcher(postEditText);
			
			List<String> preSignatureList = new ArrayList<String>();
			List<String> postSignatureList = new ArrayList<String>();
			
			while(signatureMatcherPreText.find()) {
				preSignatureList.add(signatureMatcherPreText.group(0));
			}						
			
			while(signatureMatcherPostText.find()) {
				postSignatureList.add(signatureMatcherPostText.group(0));
			}
			
			if(!preSignatureList.equals(postSignatureList)){
				signature = 1;
				
				if(preSignatureList.size() < postSignatureList.size()) {
					signatureAccept = 1;
				}else {
					signatureReject = 1;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		signatureInconsistency.add(signature);
		signatureInconsistency.add(signatureAccept);
		signatureInconsistency.add(signatureReject);

		return signatureInconsistency;	
	}
	
public static List<Integer> detectSignatureInconsistencyForTool(String preEditText, String postEditText, String userFirstName, String userLastName){
		
		List<Integer> signatureInconsistency = new ArrayList<Integer>();		
		int signature=0;
		int signatureAccept=0;
		int signatureReject=0;
		
		try {
			
			Pattern signaturePattern = Pattern.compile("(\\b"+userFirstName+"|\\b"+userLastName+")");
			Matcher signatureMatcherPreText = signaturePattern.matcher(preEditText);
			Matcher signatureMatcherPostText = signaturePattern.matcher(postEditText);
			
			List<String> preSignatureList = new ArrayList<String>();
			List<String> postSignatureList = new ArrayList<String>();
			
			while(signatureMatcherPreText.find()) {
				preSignatureList.add(signatureMatcherPreText.group(0));
			}						
			
			while(signatureMatcherPostText.find()) {
				postSignatureList.add(signatureMatcherPostText.group(0));
			}
			
			if(!preSignatureList.equals(postSignatureList)){
				signature = 1;
				
				if(preSignatureList.size() < postSignatureList.size()) {
					signatureAccept = 1;
				}else {
					signatureReject = 1;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		signatureInconsistency.add(signature);
		signatureInconsistency.add(signatureAccept);
		signatureInconsistency.add(signatureReject);

		return signatureInconsistency;	
	}
}
