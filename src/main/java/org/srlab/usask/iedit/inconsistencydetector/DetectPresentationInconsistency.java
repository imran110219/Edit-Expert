package org.srlab.usask.iedit.inconsistencydetector;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class DetectPresentationInconsistency {
	
	public static List<Integer> detectPresentationInconsistency(Document preEditDoc, Document postEditDoc, String preEditText, String postEditText){
		
		List<Integer> presentationInconsistency = new ArrayList<Integer>();		
		int presentation=0;
		int presentationAccept=0;
		int presentationReject=0;
		
		List<String> preCodeElements = new ArrayList<String>();
		List<String> postCodeElements = new ArrayList<String>();
		
//		int preEditCodeElementCount = 0;
//		int postEditCodeElementCount =0;
		
		// Code elements of pre-edit-texts
		
		for(Element element : preEditDoc.select("pre")) {
			element.remove();
		}
		for(Element codeElement : preEditDoc.select("code")) {
//			System.out.print(codeElement.text()+"<>");
			preCodeElements.add(codeElement.text().toString().trim());
//			preEditCodeElementCount++;
		}
		
		// Code elements of post-edit-texts
		
		for(Element element : postEditDoc.select("pre")) {
			element.remove();
		}
		for(Element codeElement : postEditDoc.select("code")) {
//			System.out.println(codeElement.text());
			postCodeElements.add(codeElement.text().toString().trim());
//			postEditCodeElementCount++;
		}
		
		try {
			int flag =0;
			if(!(preCodeElements.equals(postCodeElements))) { 
				for(int index =0; index < preCodeElements.size(); index++) {
					if( !(postCodeElements.contains(preCodeElements.get(index))) && (postEditText.contains(preCodeElements.get(index)))) {
						flag=1; 
						presentationReject = 1;
						break;
					}
				}
				
				for(int index =0; index < postCodeElements.size() && flag ==0; index++) {
					if( !(preCodeElements.contains(postCodeElements.get(index))) && (preEditText.contains(postCodeElements.get(index)))) {
						flag=1; 
						presentationAccept = 1;
						break;
					}
				}
				
				if(flag == 1) {
					presentation = 1;
				}
				
				if(presentationAccept ==1 ) {
					for(int index =0; index < postCodeElements.size(); index++) {
						if( !(preCodeElements.contains(postCodeElements.get(index))) && (preEditText.contains(postCodeElements.get(index)))) {
//							System.out.println(postCodeElements.get(index));
						}
					}
				}
				
				if(presentationReject == 1) {
					for(int index =0; index < preCodeElements.size(); index++) {
						if( !(postCodeElements.contains(preCodeElements.get(index))) && (postEditText.contains(preCodeElements.get(index)))) {
//							System.out.println(preCodeElements.get(index));
						}
					}
				}
				
//				if((preCodeElements.size() < postCodeElements.size()) && flag == 1) {
//					presentationAccept = 1;
//				}
//				
//				if((preCodeElements.size() > postCodeElements.size()) && flag == 1) {
//					presentationReject = 1;
//				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}

		presentationInconsistency.add(presentation);
		presentationInconsistency.add(presentationAccept);
		presentationInconsistency.add(presentationReject);
		
		return presentationInconsistency;
		
	}

}
