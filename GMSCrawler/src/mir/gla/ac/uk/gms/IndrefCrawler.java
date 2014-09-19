package mir.gla.ac.uk.gms;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;
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
 * This class implements the crawler for the www.independent.co.uk/news/uk/scottish-independence/
 * @author 	Md. Sadek Ferdous
 * @version 1.0
 * @since 	11/08/2014
 *
 */
public class IndrefCrawler extends AbstractCrawler {

	/**
	 * urlInfos is a list containing URLs to be crawled.relatedStories is a list of relatedStories in each news document.
	 */
	private ArrayList<HashMap<String, String>> urlInfos, relatedStories;
	private GMSNewsDocument indNews; 							//a news document...
	//private String outputFolder = "C:/Users/soumc/indref/"; 	//where the images will be stored...
	private String outputFolder = "/home/ripul/images/indref/";
	private DBUtils dbUtils;										//the database utility class
	private Queue<String> urlQueue;	
	
	
	public IndrefCrawler (String masterURL) {
		// TODO Auto-generated constructor stub
		super(masterURL);
		urlInfos = new ArrayList<HashMap<String, String>>();
		
		dbUtils = new DBUtils();
		urlQueue = new LinkedList<String>();
		urlQueue.add(masterURL);
		
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
	public ArrayList<HashMap<String, String>> crawlURLs() throws IOException, HttpStatusException {
		// TODO Auto-generated method stub
		System.out.println("URLs being fetched....");
		int count = 0;
		//while(!urlQueue.isEmpty()){
		Document doc = null;

	
	while (!urlQueue.isEmpty()){
		
		String currentURL = urlQueue.remove();
		 doc = Jsoup.connect(currentURL).get();
			
		 Elements links= doc.select("div.text").select("a");
			for (Element l1:links){
				String URL=l1.attr("abs:href");
				String title=l1.text();
				if(!alreadyInList(URL)){
			    	if(!dbUtils.find("url", URL)){
			      	HashMap<String, String> info = new HashMap<String, String>();
					info.put("url", URL);
					info.put("title", title);
					urlInfos.add(info);
									  	
			    	}
			    	}
			}
				
				String nextpage=doc.select("div.more").select("a").attr("abs:href");
				if (!nextpage.isEmpty()){
					//System.out.println(lp);
					urlQueue.add(nextpage);
					}
			
	}
		 
			System.out.println("URL fetching finished");
 			return urlInfos;
 			}

 /**
	 * The method checks if a URL is already in the list of the URLs that need to be crawled
	 * @param URL	
	 * @return	TRUE if it is in the list, false otherwise
	 */
	private boolean alreadyInList(String URL){
		for(HashMap<String, String> tmpMap : urlInfos){
			if(tmpMap.get("url").equals(URL))return true;
		}
		return false;
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
	indNews = new GMSNewsDocument(URL);
	indNews.setCategory("news");
	
	indNews.setSource("http://www.independent.co.uk/");
	
	Document doc = Jsoup.connect(URL).get();
	String description = "", timeStamp = "";
	
	
	String title = doc.select("h1.title").text();
	indNews.setTitle(title);
	
	description = doc.select("span.storyTop").text();
	indNews.setDescription(description);
	
	String timeStamp1 = doc.select("meta[property=article:published_time]").attr("content");
		String first=timeStamp1.substring(0,4);
		String sec=timeStamp1.substring(5,7);
		String thi=timeStamp1.substring(8,10);
        timeStamp=thi+"/"+sec+"/"+first;
        indNews.setDate(timeStamp);
	 	
	 	

	String mainStory = retrieveMainStory(doc);
	indNews.setMainStory(mainStory);
	
	ArrayList<HashMap<String, String>> imageNameAndCaption = retrieveImageAndStore(doc);
	if(imageNameAndCaption.size() > 0)indNews.setImageNameCaption(imageNameAndCaption);
	
	int pauseCount = 0;
	if(pauseCount % 10 == 0){
		try {
		    Thread.sleep(20000);                 
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}

	return indNews;

}



/**
 * Retrieves the main story of the of the news along with the related stories
 * @param doc
 * @return
 */
private String retrieveMainStory(Document doc){
	String returnString = "";
	
	Elements article1=doc.select("div.body").select("p");
	for (Element s1:article1){
		returnString += s1+ "\n";
	}
		
	return returnString;
}
		

	
/**
 * Retrieve images from the news and store them with captions in the local storage.
 * @param doc
 * @return
 * @throws IOException 
 */
private ArrayList<HashMap<String, String>> retrieveImageAndStore(Document doc) throws IOException{
ArrayList<HashMap<String, String>> imageLocAndCaption = new ArrayList<HashMap<String, String>>();

	Elements images=doc.select("img.FirstImage");
	for (Element i1:images)
	{
		String imageName1=i1.attr("src");
		String imageLocation =  imageName1;	
	//System.out.println("first set of location is" +imageName1);
		int indexname = imageName1.lastIndexOf("/");
    String imageName = "tel_"+ imageName1.substring(indexname+1,imageName1.length());
	//System.out.println("first set name is..."+imageName);
		String caption= doc.select("div.widget").select("img").attr("data-title");
		//System.out.println("first set caption is" +caption);
		 boolean imageFlag = false;
         Response resultImageResponse = null;
         try {
  			resultImageResponse = Jsoup.connect(imageLocation).ignoreContentType(true).execute();
  			imageFlag = true;
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
           
           if(imageFlag && saveImage(resultImageResponse, imageLocAndCaption, imageName, caption)){
  				//System.out.println("main image Saved and caption added!");
  			}
		
	}
	
			
		String images2=doc.select("div[class=esi-gallery]").select("a").attr("abs:href");
		//System.out.println(images2);
		if (!images2.isEmpty()){
	 			Document doc1 = Jsoup.connect(images2).get();
		//System.out.println(doc1);
	
		Elements images1=doc1.select("div[id=slideshow-6138694]").select("img");
		
	
		for (Element i1:images1)
		{
			String imageName1=i1.attr("src");
			String imageLocation =  imageName1;	
			//System.out.println("image location is: " +imageName1);
			int indexname = imageName1.lastIndexOf("/");
			String imageName = "tel_"+ imageName1.substring(indexname+1,imageName1.length());
			//System.out.println("image name is: " +imageName);
			String caption= i1.attr("alt");
			//System.out.println("image caption is: " +caption);
			boolean imageFlag = false;
	         Response resultImageResponse = null;
	         try {
	  			resultImageResponse = Jsoup.connect(imageLocation).ignoreContentType(true).execute();
	  			imageFlag = true;
	  		} catch (IOException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}
	           
	           if(imageFlag && saveImage(resultImageResponse, imageLocAndCaption, imageName, caption)){
	  				//System.out.println("main image Saved and caption added!");
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
	if(!dbUtils.find("title", indNews.getTitle()))dbUtils.addDocument(indNews);
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



	