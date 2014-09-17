package mir.gla.ac.uk.gms;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.LogFactory;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NamedNodeMap;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.JavaScriptPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



/**
 * This class implements the crawler for the www.scotsman.com.
 * @author 	Md. Sadek Ferdous
 * @version 1.0
 * @since 	11/08/2014
 *
 */
public class DrCrawler extends AbstractCrawler {

	/**
	 * urlInfos is a list containing URLs to be crawled.relatedStories is a list of relatedStories in each news document.
	 */
	private ArrayList<HashMap<String, String>> urlInfos, relatedStories;
	private GMSNewsDocument DrNews; 							//a news document...
	//private String outputFolder = "C:/Users/soumc/Downloads"; 	//where the images will be stored...
	private String outputFolder = "/home/ripul/images/dr/";
	private DBUtils dbUtils;										//the database utility class
	private Queue<String> urlQueue;	
	private int count; //queue of the URLs to be crawled...
	
	
	public DrCrawler(String masterURL) {
		// TODO Auto-generated constructor stub
		super(masterURL);
		urlInfos = new ArrayList<HashMap<String, String>>();
		relatedStories = new ArrayList<HashMap<String, String>>();
		dbUtils = new DBUtils();
		urlQueue = new LinkedList<String>();
		urlQueue.add(masterURL);
		WebClient webClient;
		webClient = new WebClient();
		webClient.getOptions().setThrowExceptionOnScriptError(false);
	    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	    webClient.getOptions().setJavaScriptEnabled(true);
	    webClient.getOptions().setCssEnabled(true);
	    webClient.getOptions().setActiveXNative(true);
	    webClient.setJavaScriptTimeout(200000);
	    webClient.getOptions().setTimeout(200000);
	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
	    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
	    
	    LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
	    java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
	}
	
	/**
	 * This method crawls all URLs from the master URL using the queue.
	 * 
	 * Each news link is retrieved from the master URL at first and put into the queue. In addition, the related stories in each link
	 * is retrieved and put into the queue for checking. Then each link is removed from the queue and is checked if the link is already
	 * in the database. If no, it is inserted into the list.
	 * 
	 * @return	an array list containing the URLs to be crawled
	 * 
	 */
	@Override
	
	public ArrayList<HashMap<String, String>> crawlURLs() throws IOException {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, String>> urlInfos = new ArrayList<HashMap<String, String>>();
		Document doc = null;
		Document doc1 = null;
		Document doc2 = null;
		
		doc = Jsoup.connect(masterURL).get();
		String URL= doc.select("div.teaser-info").select("h3").select("a").attr("abs:href");
		String title = doc.select("div.teaser-info").select("h3").select("a").text();
		if(!dbUtils.find("url", URL)){
			HashMap<String, String> tmpMap = new HashMap<String, String>();
			tmpMap.put("title", title);
			tmpMap.put("url", URL);
			urlInfos.add(tmpMap);
			count++;
		}
				
		Elements links1 = doc.getElementsByTag("h2");
	    Elements mainlinks = links1.select("a");
	    for(Element elem : mainlinks){
			int size = elem.select("a").size();
			title = elem.text();
			URL = elem.attr("href");
					
			if(!dbUtils.find("url", URL)){
				HashMap<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("title", title);
				tmpMap.put("url", URL);
				urlInfos.add(tmpMap);
				count++;
			}
			
			//if(elem.hasClass("views-row-last"))break;
		}
	    
	    doc1 = Jsoup.connect("http://www.dailyrecord.co.uk/all-about/glasgow?pageNumber=2&all=true").get();
		Elements links2 = doc1.getElementsByTag("h2");
	    Elements mainlinks1 = links2.select("a");
	    for(Element elem : mainlinks1){
			int size = elem.select("a").size();
			 title = elem.text();
		URL = elem.attr("href");
					
			if(!dbUtils.find("url", URL)){
				HashMap<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("title", title);
				tmpMap.put("url", URL);
				urlInfos.add(tmpMap);
				count++;
			}
			
			//if(elem.hasClass("views-row-last"))break;
		}
	    
	    doc2 = Jsoup.connect("http://www.dailyrecord.co.uk/all-about/glasgow?pageNumber=3&all=true").get();
	  		Elements links3 = doc2.getElementsByTag("h2");
	  	    Elements mainlinks2 = links3.select("a");
	  	    for(Element elem : mainlinks2){
	  			int size = elem.select("a").size();
	  			 title = elem.text();
	  			 URL = elem.attr("href");
	  					
	  			if(!dbUtils.find("url", URL)){
	  				HashMap<String, String> tmpMap = new HashMap<String, String>();
	  				tmpMap.put("title", title);
	  				tmpMap.put("url", URL);
	  				urlInfos.add(tmpMap);
	  				count++;
	  			}
	  			
	  			//if(elem.hasClass("views-row-last"))break;
	  		}
	  	    
		
		return urlInfos;
	}
	
	/*
	
	
	
	
	/**
	 * The method is used for retrieving infos regarding a URL from that URL.
	 * @param title	The title of each news
	 * @param URL	The URL of each news
	 */
	private void crawURLsFromHomePage(String title, String URL){
		
        Document doc; 
        String description = "", timeStamp = "";
		try {
			doc = Jsoup.connect(URL).get();
			description = doc.select("div.lead-text").text();
			//description = description.substring(0, description.indexOf("."));
			//System.out.println("Title:" + description);
			 	timeStamp = doc.select("meta[property=article:published_time").attr("content");
				int pos=timeStamp.indexOf("T");
				timeStamp= timeStamp.substring(0, pos);
				//System.out.println("Time stamp:" + timeStamp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, String> info = new HashMap<String, String>();
		info.put("title", title);
		info.put("url", URL);
		info.put("description", description);
		info.put("timeStamp", timeStamp);
		urlInfos.add(info);
		
		/**
		 * The following code implements the politeness policy. It pauses for 20 seconds
		 * after crawling 10 URLs  
		 */
		if(count % 10 == 0){
			try {
			    Thread.sleep(20000);                 
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
	}
	
	

	/**
	 * Crawls contents from a link and builds up a news document which is then returned.
	 * 
	 * @param info	a hashmap containing the URL of the news
	 * @return		a news document with the contents
	 */
	@Override
	
	public GMSNewsDocument crawlNews(HashMap<String, String> info) throws IOException{
		// TODO Auto-generated method stub
		String URL = info.get("url");
		System.out.println("The link being retrieved is:" + URL);
		DrNews = new GMSNewsDocument(URL);
		DrNews.setCategory("news");
		
		DrNews.setSource("http://www.dailyrecord.co.uk");
		
		Document doc = Jsoup.connect(URL).get();
		String description = "", timeStamp = "";
		
		
		String title = doc.select("title").text();
		DrNews.setTitle(title);
		
		description = doc.select("div.lead-text").text();
		DrNews.setDescription(description);
		
		String timeStamp1 = doc.select("meta[property=article:published_time").attr("content");
		String first=timeStamp1.substring(0,4);
		String sec=timeStamp1.substring(5,7);
		String thi=timeStamp1.substring(8,10);
		 timeStamp=thi+"/"+sec+"/"+first;
		DrNews.setDate(timeStamp);
		
		String mainStory = retrieveMainStory(doc);
		DrNews.setMainStory(mainStory);
		
		ArrayList<HashMap<String, String>> imageNameAndCaption = retrieveImageAndStore(doc);
		if(imageNameAndCaption.size() > 0)DrNews.setImageNameCaption(imageNameAndCaption);
		
		/*
		
		if(relatedStories.size() > 0){
			scotsmanNews.setRelatedStories(relatedStories);
		}
		
		
		ArrayList<CommentDocument> commentList = processComments(URL, doc);
		scotsmanNews.setComments(commentList);
		*/
		return DrNews;
	
	}
	
	/**
	 * Retrieves the main story of the of the news along with the related stories
	 * @param doc
	 * @return
	 */
	private String retrieveMainStory(Document doc){
		String returnString = "";
		
		Elements article= doc.select("div.body");
		Elements paras = article.select("p");
		 for(Element elem: paras){
			if(elem.toString().contains("start of widget"))continue;
			if (elem.toString().contains("style type"))continue;
			//System.out.println (elem);
		returnString += elem + "\n";
				 }
      // System.out.println(returnString);
		 /*Element story = article.getElementsByTag("div").get(article.getElementsByTag("div").size() - 1);
        
        String totalStory = story.toString();
        returnString = totalStory.substring(totalStory.indexOf("</p>") + 4);*/
        //returnString = returnString.substring(0, returnString.lastIndexOf("</div>"));
        
		 // There are no related stories for a news in daily record , we have promoted stories only
		 /*
		 String relatedStoriesText = "";
        boolean seeAlso = false;
        if(returnString.contains("<p><strong>SEE ALSO</strong></p>")){
        	relatedStoriesText = returnString.substring(returnString.indexOf("<p><strong>SEE ALSO</strong></p>") + "<p><strong>SEE ALSO</strong></p>".length());
        	//System.out.println("Related Stories Found:" + relatedStoriesText);
        	returnString = returnString.substring(0, returnString.indexOf("<p><strong>SEE ALSO</strong></p>"));
        	seeAlso = true;
        } 
        
        if(!seeAlso){
        	if(returnString.contains("<p><strong>SEE ALSO </strong></p>")){
            	relatedStoriesText = returnString.substring(returnString.indexOf("<p><strong>SEE ALSO </strong></p>") + "<p><strong>SEE ALSO </strong></p>".length());
            	returnString = returnString.substring(0, returnString.indexOf("<p><strong>SEE ALSO </strong></p>"));
            	seeAlso = true;
            }
        }
        if(!seeAlso){
        	if(returnString.contains("<p><strong>SEE ALSO:</strong></p>")){
            	relatedStoriesText = returnString.substring(returnString.indexOf("<p><strong>SEE ALSO:</strong></p>") + "<p><strong>SEE ALSO:</strong></p>".length());
            	returnString = returnString.substring(0, returnString.indexOf("<p><strong>SEE ALSO:</strong></p>"));
            	seeAlso = true;
            }
        } 
        if(!seeAlso){
        	if(returnString.contains("<p><strong>SEE ALSO: </strong></p>")){
            	relatedStoriesText = returnString.substring(returnString.indexOf("<p><strong>SEE ALSO: </strong></p>") + "<p><strong>SEE ALSO: </strong></p>".length());
            	returnString = returnString.substring(0, returnString.indexOf("<p><strong>SEE ALSO: </strong></p>"));
            	seeAlso = true;
            }
        }
        if(!seeAlso){
        	if(returnString.contains("<p>SEE ALSO</p>")){
            	relatedStoriesText = returnString.substring(returnString.indexOf("<p>SEE ALSO</p>") + "<p>SEE ALSO</p>".length());
            	returnString = returnString.substring(0, returnString.indexOf("<p>SEE ALSO</p>"));
            	seeAlso = true;
            }
        }
        if(!seeAlso){
        	if(returnString.contains("<p>SEE ALSO: </p>")){
            	relatedStoriesText = returnString.substring(returnString.indexOf("<p>SEE ALSO: </p>") + "<p>SEE ALSO: </p>".length());
            	returnString = returnString.substring(0, returnString.indexOf("<p>SEE ALSO: </p>"));
            	seeAlso = true;
            }
        }
        if(!seeAlso){
        	if(returnString.contains("<p>SEE ALSO:</p>")){
            	relatedStoriesText = returnString.substring(returnString.indexOf("<p>SEE ALSO:</p>") + "<p>SEE ALSO:</p>".length());
            	returnString = returnString.substring(0, returnString.indexOf("<p>SEE ALSO:</p>"));
            	seeAlso = true;
            }
        }
        
        if(relatedStoriesText.length() > 3){
            Document related = Jsoup.parse(relatedStoriesText);
            Elements relatedStoriesLinks = related.select("a");
            for(Element elem: relatedStoriesLinks){
            	HashMap<String, String> tempRelated = new HashMap<String, String>();
            	String relatedStoriesLink = elem.attr("href");
            	String relatedStoriesTitle = elem.text();
            	if(!relatedStoriesLink.contains("google-hangouts") && relatedStoriesLink.contains("scotsman.com")){
            		tempRelated.put("url", relatedStoriesLink);
                	tempRelated.put("title", relatedStoriesTitle);
                	//System.out.println("Related Stories URL:" + relatedStoriesLink + ", Related Stories Title:" + relatedStoriesTitle);
                	this.relatedStories.add(tempRelated);
            	}
            	
            }
        }
        //System.out.println("Main story:" + returnString);
		*/
		return returnString;
		}
	
	
	
	/**
	 * Retrieve images from the news and store them with captions in the local storage.
	 * @param doc
	 * @return
	 * @throws IOException 
	 * @throws FailingHttpStatusCodeException 
	 */
	private ArrayList<HashMap<String, String>> retrieveImageAndStore(Document doc) throws FailingHttpStatusCodeException, IOException{
	ArrayList<HashMap<String, String>> imageLocAndCaption = new ArrayList<HashMap<String, String>>();
	Elements image = doc.select("figure.inline-image");
	
		for (Element im:image){
		String imageName1= im.select("img").attr("src");
		//System.out.println("name is" +imageName1);
		String imageLocation =  imageName1;	
		
		if (!imageLocation.isEmpty()){
			int indexname = imageName1.lastIndexOf("/");
			String imageName= "dr_"+imageName1.substring(indexname+1,imageName1.length());
         //System.out.println(imageName);
			
			String caption= im.select("figcaption").text();
			//System.out.println("caption is:" +caption);
         		boolean imageFlag = false;
         		Response resultImageResponse = null;
        //Open a URL Stream
	         try {
				resultImageResponse = Jsoup.connect(imageLocation).ignoreContentType(true).execute();
				imageFlag = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         
	         if(imageFlag && saveImage(resultImageResponse, imageLocAndCaption, imageName, caption)){
					System.out.println("main image Saved and caption added!");
				}
		}
		
		}
		
		// embedded images 
		
		String embed = doc.select("figure.embedded-gallery").attr("data-config");
		if (!embed.isEmpty()) {
			
			int n = embed.indexOf("id");
			String el1 = embed.substring(9, n-3);
		//System.out.println(el1);
			URL url = new URL (el1);
			WebClient webClient = new WebClient();
			Page page = webClient.getPage(el1);
			String commentResponse = page.getWebResponse().getContentAsString();
			 WebResponse response = page.getWebResponse();
			 if (response.getContentType().equals("application/json")) {
		            String json = response.getContentAsString();
		            JsonParser parser = new JsonParser();
		            JsonObject obj = parser.parse(json).getAsJsonObject();
		            //JsonArray array = parser.fromJson(obj.get("response")).;
		
		            for (Entry<String, JsonElement> entry : obj.entrySet()) {
		            	
		                JsonElement value = entry.getValue();
		                 
		               // System.out.println(value);
	                	if(value.isJsonArray()){
	                		for(JsonElement elem : value.getAsJsonArray()){
	                			JsonObject tempObj = elem.getAsJsonObject();
	                			JsonArray arr = tempObj.getAsJsonArray("imageMaps");
	                			//System.out.println(arr);
	                			if (arr!=null)
	                			{
	                				for (int i = 0; i < arr.size(); i++) {
	                					
	                					JsonElement elem1 = arr.get(i); 
	                					JsonObject tempObj1 = elem1.getAsJsonObject();
	                					String url1= tempObj1.get("lowResUrl").getAsString();
	                					String imageLocation =  url1;	
	                					int indexname = url1.lastIndexOf("/");
	                					String imageName= "dr_"+url1.substring(indexname+1,url1.length());
	                					
	                					String caption= tempObj1.get("caption").getAsString();
	                					//System.out.println(url1);
	                					//System.out.println(caption);
	                					boolean imageFlag = false;
	                	         		Response resultImageResponse = null;
	                	        //Open a URL Stream
	                		         try {
	                					resultImageResponse = Jsoup.connect(imageLocation).ignoreContentType(true).execute();
	                					imageFlag = true;
	                				} catch (IOException e) {
	                					// TODO Auto-generated catch block
	                					e.printStackTrace();
	                				}
	                		         
	                		         if(imageFlag && saveImage(resultImageResponse, imageLocAndCaption, imageName, caption)){
	                						System.out.println("main image Saved and caption added!");
	                					}
	                					
	                			}
	                		}
	                	}
	                }
		         }
	                	
	         }
		
	}
		
				         
        return imageLocAndCaption;
        
	}
	
	
	/**
	 * Stores the image in the specifica location
	 * @param resultImageResponse
	 * @param imageLocAndCaption
	 * @param imageName
	 * @param caption
	 * @return
	 */
	private boolean saveImage(Response resultImageResponse, ArrayList<HashMap<String, String>> imageLocAndCaption,  String imageName, String caption){
		boolean flag = false;
        FileOutputStream out;
		try {
			out = (new FileOutputStream(new java.io.File(outputFolder + imageName)));
			out.write(resultImageResponse.bodyAsBytes());
	        out.close();
	        HashMap<String, String> tmpHashMap = new HashMap<String, String>();
	        tmpHashMap.put("name", imageName);
	        tmpHashMap.put("caption", caption);
	        imageLocAndCaption.add(tmpHashMap);
	        flag = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flag;
	}
	
	/**
	 * Stores a news document in the db.
	 */
	public void store() {
		// TODO Auto-generated method stub
		if(!dbUtils.find("title", DrNews.getTitle()))dbUtils.addDocument(DrNews);
		else {
			System.out.println("The news is already in the database.");
		}
	}
	
	public void store(GMSNewsDocument doc) {
		// TODO Auto-generated method stub
		if(!dbUtils.find("url", doc.getTitle()))dbUtils.addDocument(doc);
		else {
			System.out.println("The news is already in the database.");
		}
	}
		
	
}
