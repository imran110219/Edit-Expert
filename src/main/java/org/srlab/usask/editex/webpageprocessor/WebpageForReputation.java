package org.srlab.usask.editex.webpageprocessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

public class WebpageForReputation {
	public static void main(String[] args) throws IOException {
		WebPageSaver obj = new WebPageSaver();
		obj.saveWebPage();
	}
}

class WebPageSaver{
	
	ICsvListReader listReader = null;
	
	
	public void saveWebPage() throws IOException {
		
		try {
			FileWriter fileWriter;
			PrintWriter printInFile;
			
			List<String> existingFiles = new ArrayList<String>();
			List<String> duplicateCheck = new ArrayList<String>();
			
			String location = "E:/Projects/SORejectedEdits/EMSE/Database/Webpages/Reputation/";
			File folder = new File(location);
			File[] listOfFiles = folder.listFiles();
			
			for (int i = 0; i <listOfFiles.length; i++) {
			      if (listOfFiles[i].isFile()) {
			    	  String srcFile=listOfFiles[i].getName();
			    	  String [] fileparts = srcFile.split("\\.");
			    	  String filename = fileparts[0];
			    	  existingFiles.add(filename);
			      }
			}
			
 			listReader = new CsvListReader(new FileReader("E:/Projects/SORejectedEdits/EMSE/DevelopClassifier/Dataset/UserID.csv"), CsvPreference.STANDARD_PREFERENCE);
			listReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();
			List<Object> userList;
			
			while((userList = listReader.read(processors))!= null) {
            				
				String userID = "";
				try {
					userID = userList.get(1).toString().trim();
					System.out.println(userID);
					if(userID != null && !userID.equals("inactive") && !existingFiles.contains(userID) && !duplicateCheck.contains(userID)) {
						System.out.println("Yes, I am saving....");
						Thread.sleep((long)(Math.random() * 5000));
						duplicateCheck.add(userID);
						
						fileWriter = new FileWriter(location+userID+".txt");
						printInFile = new PrintWriter(fileWriter);
						
						String webURL = "https://stackoverflow.com/users/"+userID+"/gmannickg?tab=reputation&sort=graph";
						
						URL url = new URL(webURL);
						URLConnection con = url.openConnection();
						InputStream is = con.getInputStream();
						
						BufferedReader br = new BufferedReader(new InputStreamReader(is));
						
						String line = null;
						
						Pattern p = Pattern.compile("var rawData");
						Pattern p2 = Pattern.compile("\\[(.+?)\\]");
						
						while((line = br.readLine()) != null) {
							
							String tempLine = line;
							Matcher m = p.matcher(line);
							
							if(m.find()) {
								Matcher m2 = p2.matcher(tempLine);
								while(m2.find()) {
									String tempDate = m2.group();
									tempDate = tempDate.replaceAll("\\[", "").replaceAll("\\]", "");
									printInFile.write(tempDate+"\n");
								}
								printInFile.close();
								break;
							}
							
						}
						
					}
					else {
						System.out.println("No. Either duplicate or existing..");
					}
								
					
				} catch (Exception e) {
					e.printStackTrace();
				}			
			}
		
		} catch (Exception e) {
		e.printStackTrace();
		}
		
		finally {
	    	try {
	    		if( listReader != null ) {
	                listReader.close();
	        }	
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}
	
	private static CellProcessor[] getProcessors() {
        
	    final CellProcessor[] processors = new CellProcessor[] {
	    		new Optional(),
	    		new Optional(),
	    		new Optional(),
	    		new Optional()
	    };
	    
	    return processors;
	}
}