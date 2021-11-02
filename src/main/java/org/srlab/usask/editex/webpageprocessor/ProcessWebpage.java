package org.srlab.usask.editex.webpageprocessor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

public class ProcessWebpage {
	public static void main(String[] args) {
//		FindEditData objEditData = new FindEditData();
//		objEditData.start();
		
		ExtractData.dataExtractor();
	}
}

class ExtractData extends Thread{
		
	public static void dataExtractor() {
		
		Scanner myScanner;
		ICsvListReader listReader = null;
		ICsvListWriter csvWriter = null;
		try {
			listReader = new CsvListReader(new FileReader("E:/Projects/SORejectedEdits/EMSE/Database/"), CsvPreference.STANDARD_PREFERENCE);
			listReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();
			List<Object> editList;
			
			csvWriter = new CsvListWriter(new FileWriter("E:/Projects/SORejectedEdits/EMSE/Database/WebpageProcessedData/"),CsvPreference.STANDARD_PREFERENCE);
			csvWriter.write("postId","guiId","currentRevisionNo","rollBackRevisionNo","editStatus","preEditText","postEditText","rollbackUserId","rollbackUserName","rollbackDateTime","prevEditUserId","prevEditUserName","prevEditDateTime");

			while((editList = listReader.read(processors))!= null) {

				String postId = "-9999";
				String rollbackGuiId = "-9999";
				String rollbacktoGuiId = "-9999";
				Document doc = null;

				try {
					postId = editList.get(0).toString().trim();
					System.out.println(postId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					rollbackGuiId = editList.get(2).toString().trim();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					rollbacktoGuiId = editList.get(5).toString().trim();
					String[] rollbacktoGui = rollbacktoGuiId.split(" ");
					rollbacktoGuiId= rollbacktoGui[2].substring(1, rollbacktoGui[2].length()-1);					

				} catch (Exception e) {
					e.printStackTrace();
				}
								
				try {
					String fileLocation = "E:/Projects/SORejectedEdits/EMSE/Database/Webpages/AcceptedEdits/"+postId+".txt";
					File file = new File(fileLocation);
					if(file.exists()) {
						myScanner = new Scanner(file);
						myScanner = myScanner.useDelimiter("\\Z");
						doc = Jsoup.parse(myScanner.next());
						myScanner.close();
					}
					else {
						continue;
					}
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				
				String currentRevisionNo = "-9999";
				String rollBackRevisionNo = "-9999";
				String editStatus = "-9999";
				String rollbacktoEditStatus = "edited";
				String rollbackDateTime = "-9999";
				String rollbackUserName = "-9999";
				String rollbackUserId = "-9999";
				String preEditText = "-9999";
				String postEditText = "-9999";
				String prevEditDateTime = "-9999";
				String prevEditUserName = "-9999";
				String prevEditUserId = "-9999";
				
				int nextElement = 0;
				
				for(Element element : doc.select("div.mb12.js-revision")){
					
					String tempContent = element.toString();
					Pattern rollbackEditPattern = Pattern.compile(rollbackGuiId);
					Matcher rollbackEditMatcher = rollbackEditPattern.matcher(tempContent);
										
					if(rollbackEditMatcher.find() && nextElement==0) {
						
						nextElement = 1;
						doc = Jsoup.parse(element.toString());
						
						//Find current revision number
						try {
							Elements revisionNumber = doc.select("div.grid--cell.w48.fs-headline1.fw-bold.ta-left");
							currentRevisionNo = revisionNumber.text().toString();
						}catch (Exception e) {
							e.printStackTrace();
						}
						
						//Find rollback revision number
						try {
							Elements rollbackRevision = doc.select("span.js-revision-comment");
							String[] rollBackRevisionText = rollbackRevision.text().toString().split(" ");
							rollBackRevisionNo = rollBackRevisionText[3];
							
						}catch (Exception e) {
							e.printStackTrace();
						}
							
						//Edit Status
						try {
							Elements editPrivilege = doc.select("div.s-user-card--time");
							String[] statusText = editPrivilege.text().toString().trim().split(" ");
							editStatus = statusText[0];
							
							if(editStatus.equals("edit")) editStatus="edit approved";
						}catch (Exception e) {
							e.printStackTrace();
						}
			
						
						//Rollback datetime
						try {
							if(editStatus.equals("edited")) {
								
								Elements time = doc.select("div.s-user-card--time>time>span.relativetime");
								String dateTime = time.attr("title");
								String[] editTime = dateTime.split(":");
								rollbackDateTime = editTime[0]+":"+editTime[1];
								//System.out.println(rollbackDateTime);
							}
							if(editStatus.equals("edit approved")) {
								
								Elements time = doc.select("div.s-user-card--time>a>span.relativetime");
								String dateTime = time.attr("title");
								String[] editTime = dateTime.split(":");
								prevEditDateTime = editTime[0]+":"+editTime[1];
								//System.out.println(prevEditDateTime);
							}
						}catch (Exception e) {
							e.printStackTrace();
						}
						
						//Rollback user name
						try {
							Elements name = doc.select("div.s-user-card.s-user-card__minimal>div.s-user-card--info>a.s-user-card--link");
							rollbackUserName = name.text().toString();
													
							if(rollbackUserName.isEmpty() || rollbackUserName.equals("")) {
								Elements inactiveUser = doc.select("div.s-user-card.s-user-card__minimal.s-user-card__deleted>div.s-user-card--info>span.s-user-card--link");
								rollbackUserName = inactiveUser.text().toString();
								
								rollbackUserId = "inactive";
							}else {
								String userID = name.attr("href");
								String[] id = userID.split("/");
								rollbackUserId = id[id.length-2].toString();
							}
							//Thread.sleep(500);
							//System.out.println(postId+" "+rollbackUserName);

						} catch (Exception e) {
							e.printStackTrace();
						}

						
						try {
							int flag=0;
							
							for(Element e : doc.select("div.s-prose.js-post-body")){
								//System.out.println(postId+" "+flag);
							
								if(flag==1) {
									preEditText = e.toString();
									//System.out.println(postId+"\n"+preEditText);
								}
								if(flag==2) {
									postEditText = e.toString();
									//System.out.println(postId+"\n"+postEditText);
								}
								flag++;
							}
							
							//Thread.sleep(1000);
							
						} catch (Exception e) {
							e.printStackTrace();
						}											
					}
													
/************************ Previous Edit's Information  ********************************/
					
					else if(nextElement == 1) {
						nextElement = 2;
						doc = Jsoup.parse(element.toString());	
						
						//Edit Status
						try {
							Elements editPrivilege = doc.select("div.s-user-card--time");
							String[] statusText = editPrivilege.text().toString().trim().split(" ");
							rollbacktoEditStatus = statusText[0];
							
							if(rollbacktoEditStatus.equals("edit")) rollbacktoEditStatus="edit approved";
							//System.out.println(rollbacktoEditStatus);
						}catch (Exception e) {
							e.printStackTrace();
						}
						
						//Rollback to datetime
						try {
							
							if(rollbacktoEditStatus.equals("edited") || rollbacktoEditStatus.equals("asked") || rollbacktoEditStatus.equals("created")) {
								
								Elements time = doc.select("div.s-user-card--time>time>span.relativetime");
								String dateTime = time.attr("title");
								String[] editTime = dateTime.split(":");
								prevEditDateTime = editTime[0]+":"+editTime[1];
								//System.out.println(prevEditDateTime);
							}
							
							if(rollbacktoEditStatus.equals("edit approved")) {
								
								Elements time = doc.select("div.s-user-card--time>a>span.relativetime");
								String dateTime = time.attr("title");
								String[] editTime = dateTime.split(":");
								prevEditDateTime = editTime[0]+":"+editTime[1];
								//System.out.println(prevEditDateTime);
							}
							
						}catch (Exception e) {
							e.printStackTrace();
						}
						
						//Rollback user name
						try {
							Elements name = doc.select("div.s-user-card.s-user-card__minimal>div.s-user-card--info>a.s-user-card--link");
							prevEditUserName = name.text().toString();
													
							if(prevEditUserName.isEmpty() || prevEditUserName.equals("")) {
								Elements inactiveUser = doc.select("div.s-user-card.s-user-card__minimal.s-user-card__deleted>div.s-user-card--info>span.s-user-card--link");
								prevEditUserName = inactiveUser.text().toString();
								
								prevEditUserId = "inactive";
							}else {
								String userID = name.attr("href");
								String[] id = userID.split("/");
								prevEditUserId = id[id.length-2].toString();
							}
							
							//System.out.println(prevEditUserName);

						} catch (Exception e) {
							e.printStackTrace();
						}
										
					}
					
				}
				
				if(!postId.equals("-9999")) {
					csvWriter.write(postId,rollbackGuiId,currentRevisionNo,rollBackRevisionNo,editStatus,preEditText,postEditText,rollbackUserId,rollbackUserName,rollbackDateTime,prevEditUserId,prevEditUserName,prevEditDateTime);
				}
			}
			csvWriter.close();
			System.out.println("Write Successful!!");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
		
	private static CellProcessor[] getProcessors() {
        
	    final CellProcessor[] processors = new CellProcessor[] {
	    		new Optional(),
	    		new Optional(),
	    		new Optional(),
	    		new Optional(),
	    		new Optional(),
	    		new Optional(),
	    		new Optional(),
	    		new Optional(),
	    		new Optional()
	    };
	    
	    return processors;
	}
}
