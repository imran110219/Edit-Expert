package org.srlab.usask.iedit.webpageprocessor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SaveWebPageForRevisions {
	public static void main(String[] args) throws IOException {
		WebPageSaverForRevisions1 obj1 = new WebPageSaverForRevisions1();
		obj1.start();
	}
}

class WebPageSaverForRevisions1 extends Thread{
	
	ICsvListReader listReader = null;
	
	public void run() {
		
		try {
			FileWriter fileWriter;
			PrintWriter printInFile;
			
			List<String> existingFiles = new ArrayList<String>();
			List<String> duplicateCheck = new ArrayList<String>();
			
			String location = "E:/Projects/SORejectedEdits/AUSE/Database/LargeScaleDataset/AcceptedEdits/XXXX";
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
			for(int year = 2012; year <= 2019; year++) {
								
	 			listReader = new CsvListReader(new FileReader("E:/Projects/SORejectedEdits/AUSE/Database/LargeScaleDataset/AcceptedEdits/SampledDatasetToDownloadWebpages/Sampled_6K_Edits_"+year+".csv"), CsvPreference.STANDARD_PREFERENCE);
				listReader.getHeader(true);
				final CellProcessor[] processors = getProcessors();
				List<Object> userList;
				
				while((userList = listReader.read(processors))!= null) {
					
					String postID = "";
					try {
						postID = userList.get(1).toString().trim();
	//					System.out.println(postID);
						if(postID != null && !existingFiles.contains(postID) && !duplicateCheck.contains(postID)) {
//							System.out.println(year);
							System.out.println("Yes, I am saving....");
	//						Thread.sleep((long)(Math.random() * 4000));
							duplicateCheck.add(postID);
							
							fileWriter = new FileWriter(location+postID+".txt");
							printInFile = new PrintWriter(fileWriter);
							
							String webURL = "https://stackoverflow.com/posts/"+postID+"/revisions";
													
							
							URL url = new URL(webURL);
							HttpURLConnection con = (HttpURLConnection) url.openConnection();
							con.setRequestMethod("GET");
							InputStream is = con.getInputStream();
							
							BufferedReader br = new BufferedReader(new InputStreamReader(is));
							System.out.println(br);
							Thread.sleep((long)(Math.random() * 5000));
							
							String line = null;
							List<String> temp = new ArrayList<String>();
							while((line = br.readLine()) != null) {
								String tempLine = line;
								temp.add(tempLine);
									
							}
							String pageContent = String.join("\n", temp);
							
							Document doc = Jsoup.parse(pageContent);
							Elements content = doc.select("div.js-revisions");
							printInFile.write(content.toString());
							
							printInFile.close();
							
						}
						else {
							System.out.println("No. Either duplicate or existing..");
						}
									
						
					} catch (Exception e) {
						e.printStackTrace();
					}			
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
	    		new Optional(),
	    		new Optional()
	    };
	    
	    return processors;
	}
}