package mir.gla.ac.uk.gms;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * This class implements the crawler for the www.scotsman.com.
 * @author 	Md. Sadek Ferdous
 * @version 1.0
 * @since 	11/08/2014
 *
 */
public class BBCCrawler extends AbstractCrawler {

	/**
	 * urlInfos is a list containing URLs to be crawled.relatedStories is a list of relatedStories in each news document.
	 */
	private ArrayList<HashMap<String, String>> urlInfos, relatedStories;
	private GMSNewsDocument bbcNews; 							//a news document...
	//private String outputFolder = "C:/Users/soumc/Downloads"; 	//where the images will be stored...
	private String outputFolder = "/home/ripul/images/bbc/";
	private DBUtils dbUtils;										//the database utility class
	private Queue<String> urlQueue;									//queue of the URLs to be crawled...
	
	public BBCCrawler(String masterURL) {
		// TODO Auto-generated constructor stub
		super(masterURL);
		urlInfos = new ArrayList<HashMap<String, String>>();
		relatedStories = new ArrayList<HashMap<String, String>>();
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
				
		while(!urlQueue.isEmpty()){
				Document doc = null;
			//System.out.println("Fetching RSS Link......");
	        String currentURL = urlQueue.remove();
	        //System.out.println("Current URL:" + currentURL);
	        doc = Jsoup.connect(currentURL).get();
			System.out.println("Fetching links from the home page......");
			
	        if(currentURL.equals("http://www.bbc.co.uk/news/scotland/glasgow_and_west/"))
	        {
	   
	        
	        	String URL=doc.select("div [id=top-story]").select("a.story").first().attr("abs:href");
	        	String title = doc.select("div [id=top-story]").select("a.story").first().text();
	        	//System.out.println (URL);
	        	if(!dbUtils.find("url", URL)){
	        		urlQueue.add(URL);
	        	
	        	//if(!alreadyInList(URL)){
				HashMap<String, String> info = new HashMap<String, String>();
				info.put("url", URL);
				info.put("title", title);
				urlInfos.add(info);
				retrieveRelatedStory(URL);
				  	
	        	}
				        	        
			
	        	URL= doc.select("div [id=second-story]").select("a.story").first().attr("abs:href");
				title=  doc.select("div [id=second-story]").select("a.story").first().text();
				//System.out.println (URL);
				if(!dbUtils.find("url", URL)){ urlQueue.add(URL);
				//if(!alreadyInList(URL)){
					HashMap<String, String> info = new HashMap<String, String>();
					info.put("url", URL);
					info.put("title", title);
					urlInfos.add(info);
					retrieveRelatedStory(URL);
				}
		        
				
				URL=doc.select("div [id=third-story]").select("a.story").first().attr("abs:href");
				title=  doc.select("div [id=third-story]").select("a.story").first().text();
				//System.out.println (URL);
				if(!dbUtils.find("url", URL)){ urlQueue.add(URL);
				//if(!alreadyInList(URL)){
				HashMap<String, String> info = new HashMap<String, String>();
				info.put("url", URL);
				info.put("title", title);
				urlInfos.add(info);
				retrieveRelatedStory(URL);
			}
				
	        	Elements eltop = doc.select ("div[id=other-top-stories]").select("a.story");
				for (Element a:eltop){
					 URL= a.attr("abs:href");
					//System.out.println (other);
				 title= a.text();
					if(!dbUtils.find("url", URL)){ urlQueue.add(URL);
					//if(!alreadyInList(URL)){
						HashMap<String, String> info = new HashMap<String, String>();
						info.put("url", URL);
						info.put("title", title);
						urlInfos.add(info);
						retrieveRelatedStory(URL);
					}
			       
				}  
				
				    
			
				Elements elsport = doc.select ("div.featured-site-top-stories").select("ul").select ("li").select("a.story");
				for (Element a:elsport){
					  URL= a.attr("abs:href");
					  title= a.text();
					 System.out.println(URL);
					
					 if(!dbUtils.find("url", URL) ){
					 // if(!alreadyInList(URL)){
						 urlQueue.add(URL);
						HashMap<String, String> info = new HashMap<String, String>();
						info.put("url", URL);
						info.put("title", title);
						urlInfos.add(info);
						retrieveRelatedStory(URL);
					//}
			        
				}
				}
	       
	        }
	        
	        /*
	        else {
	        	
	        	if(!alreadyInList(currentURL)){
					HashMap<String, String> tempRelated = new HashMap<String, String>();
					tempRelated.put("url", currentURL);
					urlInfos.add(tempRelated);
					retrieveRelatedStory(currentURL);
				}
				retrieveRelatedStory(currentURL);
	        }
	        */
	        }
	       
		
		//System.out.println(urlInfos);	
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
 * The method is used for retrieving infos regarding a URL from that URL.
 * @param title	The title of each news
 * @param URL	The URL of each news
 */
/**
 * The method is used for retrieving infos regarding a URL from that URL.
 * @param title	The title of each news
 * @param URL	The URL of each news
 */
	/*
private void crawURLsFromHomePage(String title, String URL){
	int count =0;
    Document doc; 
    String description = "", timeStamp = "";
	try {
		doc = Jsoup.connect(URL).get();
		description = doc.select("div.story-body").select("p.introduction").text();
		//description = description.substring(0, description.indexOf("."));
		//System.out.println("Title:" + description);
		 	timeStamp = doc.select("span.date").text();
			
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
	/*
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
	bbcNews = new GMSNewsDocument(URL);
	bbcNews.setCategory("news");
	
	bbcNews.setSource("http://www.bbc.co.uk");
	
	Document doc = Jsoup.connect(URL).get();
	String description = "", timeStamp = "";
	
	
	String title = doc.select("h1.story-header").text();
	bbcNews.setTitle(title);
	
	description = doc.select("div.story-body").select("p.introduction").text();
	bbcNews.setDescription(description);
	
	timeStamp = doc.select("span.date").text();
	bbcNews.setDate(timeStamp);
	
	String mainStory = retrieveMainStory(doc);
	bbcNews.setMainStory(mainStory);
	
	ArrayList<HashMap<String, String>> imageNameAndCaption = retrieveImageAndStore(doc);
	if(imageNameAndCaption.size() > 0)bbcNews.setImageNameCaption(imageNameAndCaption);
	
	
	/*
	if(relatedStories.size() > 0){
		scotsmanNews.setRelatedStories(relatedStories);
	}
	
	/*
	ArrayList<CommentDocument> commentList = processComments(URL, doc);
	scotsmanNews.setComments(commentList);
	*/
	return bbcNews;

}


private void retrieveRelatedStory(String URL){
	
	Document doc = null;
	String returnString = "";
	try {
		doc = Jsoup.connect(URL).get();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	Elements relatedsport = doc.select("div [id=also-related-links]").select("ul").select("a.link");
	String relsporttext=doc.select("div [id=also-related-links]").select("h3").text();
	//System.out.println(related);
	Elements relatednews = doc.select("div.story-related").select("a.story");
	//System.out.println(related);
	int x1=0;
	String reltext=doc.select("div.see-also").select("h3").text();
	
	if (relatedsport!=null && relsporttext.equals("Also related to this story"))
	{
		for (Element e:relatedsport)
		{
			//HashMap<String, String> tempRelated = new HashMap<String, String>();
			String relatedlink=e.attr("abs:href");
			String relatedtitle=e.text();
			//relatedlink= "www.bbc.co.uk" +relatedlink;
			
			// if(relatedlink.equals("http://www.bbc.co.uk/sport/live/rugby-union/27447986"))continue;
			 //if(relatedlink.equals("http://www.bbc.co.uk/sport/live/rugby-union/27604419"))continue;
			 //if(relatedlink.equals("http://www.bbc.co.uk/sport/0/get-inspired/23179331"))continue;
			// if(relatedlink.equals("http://www.bbc.co.uk/sport/0/get-inspired/23152583"))continue;
			//if(!alreadyInList(relatedlink)){
			if(!dbUtils.find("url", relatedlink)){
				urlQueue.add(relatedlink);
				HashMap<String, String> info1 = new HashMap<String, String>();
				info1.put("url", relatedlink);
				info1.put("title", relatedtitle);
				//info1.put(", value)
				urlInfos.add(info1);
				//System.out.println(relatedlink);
			}
						
			//}
		x1++;
		}
	}
		
			
	if (x1==0 && relatednews!=null){
		
	if (reltext.equals("Related Stories"))
			{
		for (Element e:relatednews)
		{
			HashMap<String, String> tempRelated = new HashMap<String, String>();
			String relatedlink=e.attr("abs:href");
			String relatedtitle = e.text();
			//relatedlink= "www.bbc.co.uk" +relatedlink;
			//System.out.println(relatedlink);
			if(!dbUtils.find("url", relatedlink)){
				urlQueue.add(relatedlink);
				// add the links to urlinfo because this is being used in main function to crawl urls
				HashMap<String, String> info1 = new HashMap<String, String>();
				info1.put("url", relatedlink);
				info1.put("title", relatedtitle);
				urlInfos.add(info1);
				//System.out.println ("related link added to queue");
			}
				
		}
		
	}
	}
	}


/**
 * Retrieves the main story of the of the news along with the related stories
 * @param doc
 * @return
 */
private String retrieveMainStory(Document doc){
	String returnString = "";
	String reltext=doc.select("div.see-also").select("h3").text();
	Elements article=doc.select("div.story-body").select("p");
	for (Element e:article){
		if (e.hasClass("caption")) continue;
		if (e.hasClass("page-timestamp")) continue;
		if (e.toString().contains("Please turn on JavaScript"))continue;
		returnString += e + "\n";
		}
	
	Elements relatedsport = doc.select("div [id=also-related-links]").select("ul").select("a.link");
	String relsporttext=doc.select("div [id=also-related-links]").select("h3").text();
	//System.out.println(related);
	Elements relatednews = doc.select("div.story-related").select("a.story");
	//System.out.println(related);
	int x2=0;
	if (relatedsport!=null && relsporttext.equals("Also related to this story") )
	{
		for (Element e:relatedsport)
		{
			HashMap<String, String> tempRelated = new HashMap<String, String>();
			String relatedlink=e.attr("abs:href");
			String relatedtitle=e.text();
			//relatedlink= "www.bbc.co.uk" +relatedlink;
			//System.out.println(relatedlink);
			tempRelated.put("url", relatedlink);
            tempRelated.put("title", relatedtitle);
            //System.out.println("Related Stories URL:" + relatedlink + ", Related Stories Title:" + relatedtitle);
            this.relatedStories.add(tempRelated);
			x2++;
		}
	}
		
			
	if (x2==0 && relatednews!=null)
	{
		if (reltext.equals("Related Stories")){
		for (Element e:relatednews)
		{
			HashMap<String, String> tempRelated = new HashMap<String, String>();
			String relatedlink=e.attr("abs:href");
			String relatedtitle = e.text();
			//relatedlink= "www.bbc.co.uk" +relatedlink;
			//System.out.println(relatedlink);
			tempRelated.put("url", relatedlink);
            tempRelated.put("title", relatedtitle);
            //System.out.println("Related Stories URL:" + relatedlink + ", Related Stories Title:" + relatedtitle);
            this.relatedStories.add(tempRelated);
				
			}
		
		
	}
	}
	
	return returnString;
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
	
/**
 * Retrieve images from the news and store them with captions in the local storage.
 * @param doc
 * @return
 */
private ArrayList<HashMap<String, String>> retrieveImageAndStore(Document doc){
ArrayList<HashMap<String, String>> imageLocAndCaption = new ArrayList<HashMap<String, String>>();

Elements el1 = doc.select("div.story-body").select("div.caption");
Elements el2 = doc.select("div.story-body").select("div.story-feature");
Elements el3 = doc.select("div.story-body").select("div[class=story-feature image full-width]");

int exec =0;

if (el1!=null){
	for (Element im:el1){
		String imageName1= im.select("img").attr("src");
		String imageLocation =  imageName1;	
		//System.out.println("name is" +image_name);
		int indexname = imageName1.lastIndexOf("/");
        String imageName = "bbc_"+ imageName1.substring(indexname+1,imageName1.length());
		 //System.out.println(imageName);
          String caption= im.select("span").text();
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
 				//System.out.println("main image Saved and caption added!");
 			}
          exec ++;     	
	}

 	 }


if (exec==0 && el3!=null){
	
	for (Element im:el3) 
	{
		
		String imageName1= im.select("img").attr("src");
		String imageLocation =  imageName1;
			
	//System.out.println("name is" +image_name);
		int indexname = imageName1.lastIndexOf("/");
        String imageName= "bbc_"+imageName1.substring(indexname+1,imageName1.length());
        String caption= im.select("p.caption").text();
        if (caption.isEmpty()) caption = im.select("img").attr("alt");
        // System.out.println(name1);
           //int pos = caption.lastIndexOf("STV");
		 // caption= caption.substring(0, pos-1);
         // System.out.println("caption is:" +caption);
		
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
	if(!dbUtils.find("title", bbcNews.getTitle()))dbUtils.addDocument(bbcNews);
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
