package org.srlab.usask.editex.predictors;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditHyperLink {
	
	public static int detectHyperLinkModification(String preEditText, String postEditText){
		int hyperlinkModification=0;
		
		try {
			
			Pattern hyperlinkPattern = Pattern.compile("(\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])");
			Matcher hyperlinkMatcherPreText = hyperlinkPattern.matcher(preEditText);
			Matcher hyperlinkMatcherPostText = hyperlinkPattern.matcher(postEditText);
			
			List<String> preHyperList = new ArrayList<String>();
			List<String> postHyperList = new ArrayList<String>();
			
			while(hyperlinkMatcherPreText.find()) {
				preHyperList.add(hyperlinkMatcherPreText.group(0));
			}						
			
			while(hyperlinkMatcherPostText.find()) {
				postHyperList.add(hyperlinkMatcherPostText.group(0));
			}
			
			for(String hLink: preHyperList) {
				if(!postEditText.contains(hLink)) {
					hyperlinkModification = 1;
					break;
				}
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return hyperlinkModification;
		
	}
	
	public static int detectInactiveHyperlink(String preEditText, String postEditText){
		int inactiveLink=0;
		
		try {
			
			Pattern hyperlinkPattern = Pattern.compile("(\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])");
			Matcher hyperlinkMatcherPreText = hyperlinkPattern.matcher(preEditText);
			//Matcher hyperlinkMatcherPostText = hyperlinkPattern.matcher(postEditText);
			
			while(hyperlinkMatcherPreText.find() && inactiveLink==0) {
				
				try {
					URL url = new URL(hyperlinkMatcherPreText.group(0));
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("HEAD");
					
					if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
						//System.out.println("Active:"+hyperlinkMatcherPreText.group(0));
					}
					
				} catch (Exception e) {
					inactiveLink=1;
					//System.out.println("Inactive Link:"+hyperlinkMatcherPreText.group(0));
				}
				
			}						
			
			/********
			while(hyperlinkMatcherPostText.find() && inactiveLink==0) {
				try {
					URL url = new URL(hyperlinkMatcherPostText.group(0));
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("HEAD");
					
					if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
						System.out.println("Active:"+hyperlinkMatcherPostText.group(0));
					}
					
				} catch (Exception e) {
					inactiveLink=1;
					System.out.println("Inactive Link:"+hyperlinkMatcherPreText.group(0));
				}
				
			}
			**********/
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return inactiveLink;
		
	}


}
