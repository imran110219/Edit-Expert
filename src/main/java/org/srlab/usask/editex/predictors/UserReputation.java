package org.srlab.usask.editex.predictors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class UserReputation {

	public static int calculateReputation(String userID) {
		
		File file;
		Scanner sc;
		int reputation = 0;
		
		if(userID != null && !userID.equals("inactive") && Integer.parseInt(userID)>0) {
			
			String location = "E:/Projects/SORejectedEdits/EMSE/Database/Webpages/Reputation/";
			file = new File(location+userID+".txt");
			try {
				sc = new Scanner(file);
				
				while(sc.hasNext()) {
					String line = sc.nextLine();
					String[] dateReputation = line.split(",");
					reputation = reputation + Integer.parseInt(dateReputation[3]);
				}					
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}						
		}
		
		return reputation;
	}
}