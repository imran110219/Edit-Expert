package org.srlab.usask.editex.predictors;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TextFormattingDetection {
	
	public static int detectTextFormatting(Document preEditDoc, Document postEditDoc, String preEditText, String postEditText) {
		
		int textFormat = 0;
		
		List<String> preCodeElements = new ArrayList<String>();
		List<String> postCodeElements = new ArrayList<String>();
		
		try {
			
			for(Element element : preEditDoc.select("pre")) {
				element.remove();
			}
			for(Element codeElement : preEditDoc.select("code")) {
				preCodeElements.add(codeElement.text().toString().trim());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			
			for(Element element : postEditDoc.select("pre")) {
				element.remove();
			}
			for(Element codeElement : postEditDoc.select("code")) {
				postCodeElements.add(codeElement.text().toString().trim());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		try {
			int flag =0;
			if(!(preCodeElements.equals(postCodeElements))) { 
				for(int index =0; index < preCodeElements.size(); index++) {
					if( !(postCodeElements.contains(preCodeElements.get(index))) && (postEditText.contains(preCodeElements.get(index)))) {
						flag=1; break;
					}
				}
				
				for(int index =0; index < postCodeElements.size() && flag ==0; index++) {
					if( !(preCodeElements.contains(postCodeElements.get(index))) && (preEditText.contains(postCodeElements.get(index)))) {
						flag=1; break;
					}
				}
				
				if(flag == 1) {
					textFormat=1;
				}
				else{

					textFormat=0;
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return textFormat;
	}

}
