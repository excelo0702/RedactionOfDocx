package com.mkyong.poi.word;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ICell;
import org.apache.poi.xwpf.usermodel.IRunElement;
import org.apache.poi.xwpf.usermodel.ISDTContent;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFieldRun;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFSDT;
import org.apache.poi.xwpf.usermodel.XWPFSDTCell;
import org.apache.poi.xwpf.usermodel.XWPFSDTContent;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.MatchType;
import com.sun.tools.javac.code.Attribute.Array;

public class UpdateDocument {

	// facing issue with phone Number

	
	static ArrayList<String> redactWordList;
	static String replaceWord = "@@@@@@@@@@";
	static String phoneNumber = "";
	static Set<String> splittedWordList;

	static ArrayList<String> allResume;
	static ArrayList<MetaData> allResumeMetaData;

	public static void main(String[] args) throws Exception {			
		
		UpdateDocument obj = new UpdateDocument();

		allResume = new ArrayList<>(); init();

		final File folder = new File("C:\\Users\\tanish.yadav\\OneDrive - Spectraforce Technologies\\Desktop\\tanish\\docx_resumes"); 
		listFilesForFolder(folder);

		for(MetaData metadata:allResumeMetaData) { 
			//search for this file inallResume 
			for(String file:allResume) { 
				if(file.equals(metadata.getResume())){
					fillRedactList(metadata);
					System.out.print(metadata.getFirstName()+" : "); 
					String out = "C:\\\\Users\\\\tanish.yadav\\\\eclipse-workspace\\\\"+file;
					splitRedactList();
					// System.out.println(file); 
					obj.updateDocument(file,out); break; 
				}
			} 
		}

		

//	
//		redactWordList = new ArrayList<>();
//		redactWordList.add("Jarod");
//		redactWordList.add(".");
//		redactWordList.add("904 - 994 - 4790");
//		redactWordList.add("c12328e5-6819-4880-88db-d0769f7c05d4.docx");
//		phoneNumber = "904 - 994 - 4790";
//		String input = "c12328e5-6819-4880-88db-d0769f7c05d4.docx";
//		splitRedactList();
//		// if(validMobileNumber("904-318-5257","+19043185257")){
//		// System.out.print("true");
//		// }else {
//		// System.out.print("false");
//		//
//		// }
//		obj.updateDocument(input, "C:\\Users\\tanish.yadav\\eclipse-workspace\\output1.docx");
//					 
	}

	private static void fillRedactList(MetaData metadata) {
		redactWordList = new ArrayList<>(); 
		if(metadata.getCandidateGuid()!=null && metadata.getCandidateGuid().trim().length()>0)
			redactWordList.add(metadata.getCandidateGuid());
		if(metadata.getCandidateId()!=null &&
				metadata.getCandidateId().trim().length()>0)
			redactWordList.add(metadata.getCandidateId());

		if(metadata.getCity()!=null && metadata.getCity().trim().length()>0)
			redactWordList.add(metadata.getCity());

		if(metadata.getFormattedNumber()!=null &&
				metadata.getFormattedNumber().trim().length()>0)
			redactWordList.add(metadata.getFormattedNumber());

		if(metadata.getEmailId()!=null && metadata.getEmailId().trim().length()>0)
			redactWordList.add(metadata.getEmailId());

		if(metadata.getLocation()!=null && metadata.getLocation().trim().length()>0)
			redactWordList.add(metadata.getLocation());

		if(metadata.getMobile()!=null && metadata.getMobile().trim().length()>0) {
			phoneNumber = metadata.getMobile(); redactWordList.add(metadata.getMobile());
		}

		if(metadata.getPhone()!=null && metadata.getPhone().trim().length()>0) {
			phoneNumber = metadata.getPhone(); redactWordList.add(metadata.getPhone()); }

		if(metadata.getLastName()!=null && metadata.getLastName().trim().length()>0)
			redactWordList.add(metadata.getLastName());

		if(metadata.getFirstName()!=null &&
				metadata.getFirstName().trim().length()>0)
			redactWordList.add(metadata.getFirstName());

		if(metadata.getResume()!=null && metadata.getResume().trim().length()>0)
			redactWordList.add(metadata.getResume()); System.out.println(redactWordList);

		
	}

	public static boolean validMobileNumber(String number, String mobileNumber) {
		PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
		MatchType mt = pnu.isNumberMatch(number, mobileNumber);
		if (mt == MatchType.NSN_MATCH || mt == MatchType.EXACT_MATCH) {
			return true;
		}
		return false;
	}

	public static void splitRedactList() {
		splittedWordList = new HashSet<String>();
		for (String word : redactWordList) {
			String[] array = word.split("[\\s.,]+");
			for (String w : array) {
				if (w.trim().length() > 3) {
					splittedWordList.add(w.toLowerCase().trim());
					// System.out.print(word+" ");
				}
			}
			if(word.trim().length()>2)
				splittedWordList.add(word.toLowerCase().trim());
		}
		for(String word: splittedWordList) {
			System.out.println(word);
		}
		System.out.println("\n\n\n");

	}

	public static void listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				if (!fileEntry.getName().equals("metadata.json"))
					allResume.add(fileEntry.getName());
			}
		}
	}

	private static void init() throws FileNotFoundException, IOException, ParseException {
		allResumeMetaData = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArray = (JSONArray) jsonParser.parse(new FileReader(
				"C:\\Users\\tanish.yadav\\OneDrive - Spectraforce Technologies\\Desktop\\tanish\\docx_resumes\\metadata.json"));

		for (int n = 0; n < jsonArray.size(); n++) {
			JSONObject item = (JSONObject) jsonArray.get(n);
			// System.out.println(item.get("CandidateId").toString());
			String candidateId = item.get("CandidateId") != null ? item.get("CandidateId").toString() : null;
			String city = item.get("city") != null ? item.get("city").toString() : null;
			String formattedNumbers = item.get("FormattedNumbers") != null ? item.get("FormattedNumbers").toString()
					: null;
			String candidateguid = item.get("candidateguid") != null ? item.get("candidateguid").toString() : null;
			String emailId = item.get("EmailId") != null ? item.get("EmailId").toString() : null;
			String location = item.get("Location") != null ? item.get("Location").toString() : null;
			String mobile = item.get("Mobile") != null ? item.get("Mobile").toString() : null;
			String phone = item.get("Phone") != null ? item.get("Phone").toString() : null;
			String lastName = item.get("LastName") != null ? item.get("LastName").toString() : null;
			String firstName = item.get("FirstName") != null ? item.get("FirstName").toString() : null;
			String resume = item.get("resume") != null ? item.get("resume").toString() : null;

			MetaData object = new MetaData(candidateId, city, formattedNumbers, candidateguid, emailId, location,
					mobile, phone, lastName, firstName, resume);
			allResumeMetaData.add(object);
			// do some stuff....
		}

	}

	static void traverseRunElements(List<IRunElement> runList) throws Exception {
		if (runList != null) {
			for (int i = 0; i < runList.size(); i++) {
				IRunElement runElement = runList.get(i);
				if (runElement instanceof XWPFHyperlinkRun) {

					int j = i;
					String text = "";
					while (j < runList.size() && j < i + 10 && runElement instanceof XWPFHyperlinkRun) {

						XWPFHyperlinkRun run = (XWPFHyperlinkRun) runElement;

						if (run!=null && run.getText(0)!=null) {
							text += run.getText(0);
							// search for text
							// SEARCHING WORDS BY ADDING CONSECUTIVE RUNS
					//		System.out.println(text + " :::hyper ");
							if (validMobileNumber(text, phoneNumber)) {
								int k = i;
								while (k <= j) {
									XWPFHyperlinkRun tempRun = (XWPFHyperlinkRun) runList.get(k);
									tempRun.setText("%%%", 0);
									k++;
								}
							} else if (splittedWordList.contains(text.toLowerCase())) {
								// replace all Runs
								// System.out.println(" replaced ");
								int k = i;
								while (k <= j) {
									XWPFHyperlinkRun tempRun = (XWPFHyperlinkRun) runList.get(k);
									tempRun.setText("@@@", 0);
									k++;
								}
							}
							// SEARCHING WORDS BY SPLITTING CURRENT TEXT WITH SPACES
							String[] array = text.split("[\\s,]+");
							int idx = 0;
							while (idx < array.length) {
								if (validMobileNumber(array[idx], phoneNumber)) {

									int k = i;
									int arrayIndex = 0;
									while (k <= j) {
										XWPFHyperlinkRun tempRun = (XWPFHyperlinkRun) runList.get(k);
										String tempText = tempRun.getText(0);
										int flag=0;
										if (tempText != null) {

											int tempIndex = 0;
											while (tempIndex < tempText.length() && arrayIndex < array[idx].length()
													&& tempText.charAt(tempIndex) == array[idx].charAt(arrayIndex)) {
												tempText = tempText.substring(0, tempIndex) + '%' + tempText
														.substring(Math.min(tempIndex + 1, tempText.length() - 1));
												arrayIndex++;
												tempIndex++;
												flag=1;
											}
											if(flag==0) {
												tempText = tempText.replace(array[idx], "%%%");
											}
										}
										
										tempRun.setText(tempText, 0);

										k++;
									}
								} else if (splittedWordList.contains(array[idx].toLowerCase())) {
									// System.out.println(array[idx]+" yo ");
									int k = i;
									int arrayIndex=0;
									while (k <= j) {
										XWPFHyperlinkRun currentRun = (XWPFHyperlinkRun) runList.get(k);
										String currentText = currentRun.getText(0);
										int flag=0;
										if (currentText != null) {
											//		System.out.println(currentText);
											//		System.out.println(array[idx]);
											int currentIndex=0;
											while(currentIndex<currentText.length() && arrayIndex<array[idx].length() && currentText.charAt(currentIndex)==array[idx].charAt(arrayIndex)) {
												currentText = currentText.substring(0, currentIndex) + '@' + currentText.substring(currentIndex + 1);		
												currentIndex++;
												arrayIndex++;
												flag=1;
											}
											if(flag==0) {
												currentText = currentText.replace(array[idx], "@@@");
											}

											//		currentText = currentText.replace(array[idx], "@@@");
											currentRun.setText(currentText, 0);
										}
										k++;
									}
								}
								idx++;
							} 
						}
						j++;
						if (j >= runList.size())
							break;
						runElement = runList.get(j);


					}
					// System.out.println(text+" hyperlink ");
				} else if (runElement instanceof XWPFRun) {

					int j = i;
					String text = "";
					while (j < runList.size() && j < i + 7 && runElement instanceof XWPFRun) {

						XWPFRun run = (XWPFRun) runElement;

						if (run!=null && run.getText(0)!=null) {
							text += run.getText(0);
							// search for text
							// SEARCHING WORDS BY ADDING CONSECUTIVE RUNS
							System.out.println(text + " :::run ");
							if (validMobileNumber(text, phoneNumber)) {
								int k = i;
								while (k <= j) {
									XWPFRun tempRun = (XWPFRun) runList.get(k);
									tempRun.setText("%%%", 0);
									k++;
								}
							} else if (splittedWordList.contains(text.toLowerCase().trim())) {
								// replace all Runs
								// System.out.println(" replaced ");
								int k = i;
								while (k <= j) {
									XWPFRun tempRun = (XWPFRun) runList.get(k);
									tempRun.setText("@@@", 0);
									k++;
								}
							}
							// SEARCHING WORDS BY SPLITTING CURRENT TEXT WITH SPACES
							String[] array = text.split("[\\s,]+");
							int idx = 0;
							while (idx < array.length) {
							//	System.out.println(array[idx] + " :::run ");
								if (validMobileNumber(array[idx], phoneNumber)) {

									int k = i;
									int arrayIndex = 0;
									while (k <= j) {
										XWPFRun tempRun = (XWPFRun) runList.get(k);
										String tempText = tempRun.getText(0);
										int flag=0;
										if (tempText != null) {

											int tempIndex = 0;
											while (tempIndex < tempText.length() && arrayIndex < array[idx].length()
													&& tempText.charAt(tempIndex) == array[idx].charAt(arrayIndex)) {
												tempText = tempText.substring(0, tempIndex) + '%' + tempText
														.substring(Math.min(tempIndex + 1, tempText.length() - 1));
												arrayIndex++;
												tempIndex++;
												flag=1;
											}
											if(flag==0) {

												tempText = tempText.replace(array[idx], "%%%");
											}
										}

										tempRun.setText(tempText, 0);

										k++;
									}
								} else if (splittedWordList.contains(array[idx].toLowerCase().trim())) {
								//	System.out.println(array[idx]+" yo ");
									int k = i;
									int arrayIndex=0;
									while (k <= j) {
										XWPFRun currentRun = (XWPFRun) runList.get(k);
										String currentText = currentRun.getText(0);
										
										int flag=0;
										if (currentText != null) {
											//		System.out.println(currentText);
											//		System.out.println(array[idx]);
											int currentIndex=0;
											while(currentIndex<currentText.length() && arrayIndex<array[idx].length() && currentText.charAt(currentIndex)==array[idx].charAt(arrayIndex)) {
												currentText = currentText.substring(0, currentIndex) + '@' + currentText.substring(currentIndex + 1);		
												currentIndex++;
												arrayIndex++;
												flag=1;
											}
											if(flag==0) {
												currentText = currentText.replace(array[idx], "@@@");
											}


											//		currentText = currentText.replace(array[idx], "@@@");
											currentRun.setText(currentText, 0);
										}
										k++;
									}
								}
								idx++;
							} 
						}
						j++;
						if (j >= runList.size())
							break;
						runElement = runList.get(j);


					}
					// System.out.println("\n\n");
				}else if(runElement instanceof XWPFSDT) {
//					XWPFSDT run = (XWPFSDT)runElement;
//			
//					//	String text = run.toString();
//				//	System.out.print(text);
//			//		List<PackagePart> list	=	sdtContent.getAllEmbeddedParts();
//				//	System.out.println(list);
//					XWPFDocument doc = run.getDocument();
//					List<IBodyElement> list = doc.getBodyElements();
//					System.out.println(list.size()+" list size ");
//					for(IBodyElement element: list) {
//						if(element instanceof XWPFParagraph) {
//							XWPFParagraph para = (XWPFParagraph)element;
//							System.out.println(para.getText() +" p ");
//							List<IRunElement> list2 = para.getIRuns();
//							System.out.println(list2.size()+" list2 size ");
//							for(IRunElement ele:list2) {
//								if(ele instanceof XWPFHyperlinkRun) {
//									System.out.println(" link ");
//								}else if(ele instanceof XWPFRun){
//									System.out.println(" run ");
//								}else {
//									System.out.println(" dont know which run ");
//								}
//							}
//							
//						}else if(element instanceof XWPFSDT){
//							System.out.println(" sdt ");
//						}else if(element instanceof XWPFTable) {
//							System.out.println(" table ");
//
//							XWPFTable table = (XWPFTable)element;
//							System.out.println(table.getRows().size()+" size3 ");
//							for (XWPFTableRow tableRow : table.getRows()) {
//								System.out.println(tableRow.getCell(0).getText()+" tableRow ");
//
////								traverseTableCells(tableRow.getTableICells());
//							}
//						//	traverseTableRows(table.getRows());
//						}
//					}
					
				//	replaceHeaderText(sdtContent);

					
					
				}
			}
		}

		/*
		 *
		 *
		 * STEVEN SEMEL ssemel@nyc.rr.com (917)734-3817
		 *
		 */
	}

	static void traverseBodyElements(List<IBodyElement> bodyElements) throws Exception {
		for (IBodyElement bodyElement : bodyElements) {
			if (bodyElement instanceof XWPFParagraph) {
				XWPFParagraph paragraph = (XWPFParagraph) bodyElement;
				traverseRunElements(paragraph.getIRuns());
			} else if (bodyElement instanceof XWPFSDT) {
				XWPFSDT sDT = (XWPFSDT) bodyElement;
				// ToDo: The SDT may have traversable content too.
			} else if (bodyElement instanceof XWPFTable) {
				XWPFTable table = (XWPFTable) bodyElement;
				traverseTableRows(table.getRows());
			}
		}
	}

	static void traverseTableCells(List<ICell> tableICells) throws Exception {
		for (ICell tableICell : tableICells) {
			if (tableICell instanceof XWPFSDTCell) {
				// Dont think we need this
				// XWPFSDTCell sDTCell = (XWPFSDTCell) tableICell;
				// System.out.println(sDTCell + " 13 ");
				// ToDo: The SDTCell may have traversable content too.
			} else if (tableICell instanceof XWPFTableCell) {
				XWPFTableCell tableCell = (XWPFTableCell) tableICell;
				// System.out.println(tableCell + " 14 ");
				traverseBodyElements(tableCell.getBodyElements());
			}
		}
	}

	static void traverseTableRows(List<XWPFTableRow> tableRows) throws Exception {
		for (XWPFTableRow tableRow : tableRows) {
			traverseTableCells(tableRow.getTableICells());
		}
	}

	private void updateDocument(String input, String output) throws Exception {

		// XWPFDocument provides high level APIs to create and edit .docx file
		try (InputStream is = getFileFromResource(input) ) {
			XWPFDocument doc = new XWPFDocument(is);
			List<IBodyElement> bodyElements = doc.getBodyElements();
			replaceHeaderText(doc);

			for (IBodyElement bodyElement : bodyElements) {
				if (bodyElement instanceof XWPFParagraph) {
					// Paragraph Element
					// System.out.println(" 1 \n\n");
					XWPFParagraph paragraph = (XWPFParagraph) bodyElement;
					traverseRunElements(paragraph.getIRuns());
				} else if (bodyElement instanceof XWPFTable) {
					// Table Element
					// System.out.println(" 2 ");
					XWPFTable table = (XWPFTable) bodyElement;
					traverseTableRows(table.getRows());
				} else {
					System.out.print(" dont know " + bodyElement + "  ");
				}
			}


			// save the docs
			try (FileOutputStream out = new FileOutputStream(output)) {
				doc.write(out);
			}

		}

	}

	private static void replaceHeaderText(XWPFDocument doc) throws Exception {
		System.out.println(" Header ");
		List<XWPFHeader> headers = doc.getHeaderList();
		for (XWPFHeader h : headers) {
			for (XWPFParagraph paragraph : h.getParagraphs()) {
				traverseRunElements(paragraph.getIRuns());
			}
		}

	}

	// get file from the resource folder.
	private InputStream getFileFromResource(String fileName) {
		return getClass().getClassLoader().getResourceAsStream(fileName);
	}

}

/*
 * 
 * HWPF - horrible word processor format for .doc file XWPF - XML word processor
 * format for .docx
 * 
 * 
 */