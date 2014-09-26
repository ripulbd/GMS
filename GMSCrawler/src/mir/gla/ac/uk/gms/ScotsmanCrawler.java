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
public class ScotsmanCrawler extends AbstractCrawler {

	/**
	 * urlInfos is a list containing URLs to be crawled.relatedStories is a list of relatedStories in each news document.
	 */
	private ArrayList<HashMap<String, String>> urlInfos, relatedStories;
	private GMSNewsDocument scotsmanNews; 							//a news document...
	private String outputFolder = "/home/ripul/images/scotsman/"; 	//where the images will be stored...
	//private String outputFolder = "/Users/ripul/images/scotman/";
	private DBUtils dbUtils;										//the database utility class
	private Queue<String> urlQueue;									//queue of the URLs to be crawled...
	
	public ScotsmanCrawler(String masterURL) {
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
	        //if(currentURL.equals("http://www.scotsman.com/video/News/catalans-rallying-cry-for-independence-3779918781001"))continue;
	        if(currentURL.equals("http://www.scotsman.com/scottish-independence/polls"))continue;
	        if(currentURL.contains("http://www.scotsman.com/video/"))continue;
	        
	        
	        System.out.println("Current URL:" + currentURL);
	        doc = Jsoup.connect(currentURL).get();
			
			//System.out.println("Fetching links from the home page......");
			Elements articleLead = doc.select("article.lead-story");
			if(currentURL.equals("http://www.scotsman.com/news/scotland/glasgow-west")){
				for(Element elem : articleLead){
					int size = elem.select("a").size();
					Element anchor = elem.select("a").get(1);
					String title = anchor.text();
					String URL = anchor.attr("href");
					if(title.length() == 0)title = elem.select("a").get(2).text();
					if(!dbUtils.find("url", URL)){
						urlQueue.add(URL);
						HashMap<String, String> info = new HashMap<String, String>();
						info.put("url", URL);
						urlInfos.add(info);
						retrieveRelatedStory(URL);
					}
				}
				
				Elements articleElement = doc.select("article.teaser");
				
				for(Element elem : articleElement){
					int size = elem.select("a").size();
					Element anchor = elem.select("a").get(1);
					String title = anchor.text();
					String URL = anchor.attr("href");
					if(title.length() == 0)title = elem.select("a").get(2).text();
					if(!dbUtils.find("url", URL)){
						urlQueue.add(URL);
						crawURLsFromHomePage(title, URL, count);
						retrieveRelatedStory(URL);
						count++;
					}
				}
			} else {
				if(!alreadyInList(currentURL)){
					HashMap<String, String> tempRelated = new HashMap<String, String>();
					tempRelated.put("url", currentURL);
					urlInfos.add(tempRelated);
					retrieveRelatedStory(currentURL);
				}
				retrieveRelatedStory(currentURL);
			}
			
		}
		
		
		System.out.println("URLs fetching finished......");
		return urlInfos;
	}
	
	/**
	 * The method is used for retrieving infos regarding a URL from that URL.
	 * @param title	The title of each news
	 * @param URL	The URL of each news
	 */
	private void crawURLsFromHomePage(String title, String URL, int count){
		
        Document doc; 
        String description = "", timeStamp = "";
		try {
			doc = Jsoup.connect(URL).get();
			description = doc.select("div.article-content").first().text();
			if(description.contains("."))description = description.substring(0, description.indexOf("."));
			timeStamp = doc.select("div.Updated").first().text();
			timeStamp = timeStamp.substring(timeStamp.indexOf(" ") + 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(StringIndexOutOfBoundsException e){
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
		scotsmanNews = new GMSNewsDocument(URL);
		scotsmanNews.setCategory("news");
		
		scotsmanNews.setSource("http://www.scotsman.com");
		
		Document doc = Jsoup.connect(URL).get();
		
		
		String description = "", timeStamp = "";
		
		
		String title = doc.select("h1#content").first().text();
		scotsmanNews.setTitle(title);
		
		description = doc.select("div.article-content").first().text();
		if(description.contains("."))description = description.substring(0, description.indexOf("."));
		scotsmanNews.setDescription(description);
		
		timeStamp = doc.select("div.Updated").first().text();
		timeStamp = timeStamp.substring(timeStamp.indexOf(" ") + 1);
		scotsmanNews.setDate(timeStamp);
		
		String mainStory = retrieveMainStory(doc);
		
		scotsmanNews.setMainStory(mainStory);
		if(relatedStories.size() > 0){
			scotsmanNews.setRelatedStories(relatedStories);
		}
		ArrayList<HashMap<String, String>> imageNameAndCaption = retrieveImageAndStore(doc);
		if(imageNameAndCaption.size() > 0)scotsmanNews.setImageNameCaption(imageNameAndCaption);
		
		ArrayList<CommentDocument> commentList = processComments(URL, doc);
		scotsmanNews.setComments(commentList);
		
		return scotsmanNews;
	}
	
	/**
	 * Retrieves related stories from a URL and inserts them in to the relatedStories list.  
	 * @param URL	the URL from which the related stories need to be fetched.
	 */
	private void retrieveRelatedStory(String URL){
		
		Document doc = null;
		String returnString = "";
		try {
			doc = Jsoup.connect(URL).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element article = doc.getElementsByClass("article").first();
		Elements paragraph = article.getElementsByTag("p");
		for(Element elem: paragraph){
			if(elem.hasClass("flt-l"))continue;
			if(elem.hasClass("cld"))continue;
			if(elem.hasClass("del"))continue;
			if(elem.hasClass("ico"))continue;
			if(elem.toString().contains("PSTYLE=$ID/[No paragraph style]-->"))continue;
			returnString += elem + "\n";
		}
        String relatedStoriesText = "";
        boolean seeAlso = false;
        if(returnString.contains("SEE ALSO")){
        	if(returnString.contains("<p><strong>SEE ALSO</strong></p>")){
            	relatedStoriesText = returnString.substring(returnString.indexOf("<p><strong>SEE ALSO</strong></p>") + "<p><strong>SEE ALSO</strong></p>".length());
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
            if(!seeAlso){
            }
            if(relatedStoriesText.length() > 3){
                Document related = Jsoup.parse(relatedStoriesText);
                Elements relatedStoriesLinks = related.select("a");
                for(Element elem: relatedStoriesLinks){
                	String relatedStoriesLink = elem.attr("href");
                	if(relatedStoriesLink.equals("http://www.scotsman.com/sport/commonwealth-games/top-stories"))continue;
                	if(!relatedStoriesLink.contains("google-hangouts") && relatedStoriesLink.contains("scotsman.com")){
                		if(!alreadyInList(relatedStoriesLink)){
                			if(!dbUtils.find("url", URL))urlQueue.add(relatedStoriesLink);
                		}
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
		
		Element article = doc.getElementsByClass("article").first();
		Elements paragraph = article.getElementsByTag("p");
		for(Element elem: paragraph){
			if(elem.hasClass("flt-l"))continue;
			if(elem.hasClass("cld"))continue;
			if(elem.hasClass("del"))continue;
			if(elem.hasClass("ico"))continue;
			if(elem.toString().contains("PSTYLE=$ID/[No paragraph style]-->"))continue;
			returnString += elem + "\n";
		}
        String relatedStoriesText = "";
        boolean seeAlso = false;
        if(returnString.contains("<p><strong>SEE ALSO</strong></p>")){
        	relatedStoriesText = returnString.substring(returnString.indexOf("<p><strong>SEE ALSO</strong></p>") + "<p><strong>SEE ALSO</strong></p>".length());
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
		return returnString;
	}
	
	/**
	 * Retrieve images from the news and store them with captions in the local storage.
	 * @param doc
	 * @return
	 */
	private ArrayList<HashMap<String, String>> retrieveImageAndStore(Document doc){
		ArrayList<HashMap<String, String>> imageLocAndCaption = new ArrayList<HashMap<String, String>>();
		
		Element article = doc.getElementsByClass("article").first();
		Element image = article.getElementsByTag("img").first();
		Element captionElement = article.select("p.flt-l").first();
		if(image != null){
			String caption = "";
			if(captionElement != null) caption = captionElement.text();
			if(caption.contains(" Picture"))caption = caption.substring(0, caption.indexOf(" Picture"));
	        String imageName = image.attr("src");
	        String imageLocation = "http://www.scotsman.com" + imageName;
	        
	        imageName = "scot_" + imageName.substring(imageName.lastIndexOf("/") + 1);
	        boolean imageFlag = false;
	        Response resultImageResponse = null;
			try {
				resultImageResponse = Jsoup.connect(imageLocation).ignoreContentType(true).execute();
				imageFlag = true;
			} catch (HttpStatusException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(imageFlag && saveImage(resultImageResponse, imageLocAndCaption, imageName, caption)){
				//System.out.println("main image Saved and caption added!");
			}
			
			/**
			 * The following code is to test if there is another image...
			 */
			Element anotherImageDiv = article.getElementsByClass("left").first();
			
			if(anotherImageDiv != null){
				Element anotherImage = anotherImageDiv.getElementsByTag("img").first();
				String anotherImageName = anotherImage.attr("src");
				String anotherImageLocation = "http://www.scotsman.com" + anotherImageName;
				anotherImageName = anotherImageName.substring(anotherImageName.lastIndexOf("/") + 1);
				Response anotherImageResponse = null;
				boolean anotherImageFlag = false;
				try {
					anotherImageResponse = Jsoup.connect(anotherImageLocation).ignoreContentType(true).execute();
					anotherImageFlag = true;
				} catch (HttpStatusException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(anotherImageFlag && saveImage(anotherImageResponse, imageLocAndCaption, anotherImageName, "")){
				}
			}

		} else {
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
	 * Processes comments in the news...
	 * 
	 * @param URL
	 * @param doc
	 * @return
	 * @throws IOException
	 */
	private ArrayList<CommentDocument> processComments(String URL, Document doc) throws IOException{
		ArrayList<CommentDocument> commentList = new ArrayList<CommentDocument>();
		
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
        
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
        
        WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.setJavaScriptTimeout(100000);
        webClient.getOptions().setTimeout(100000);
        
        String docText = doc.html();
        String elemKey = docText.substring(docText.indexOf("plckCommentOnKey: \"ELM.1") + "plckCommentOnKey: ".length() + 1);
        elemKey = elemKey.substring(0, elemKey.indexOf("\""));
        
        HtmlPage page = webClient.getPage(URL + "?plckOnPage=1");
        
        List<?> divs =page.getByXPath("//*[@class=\"pluck-comm-ReplyLevel-1\"]");
        if(divs.size() <= 0){
        	/**
        	 * No comments for this page, so no need to execute the following code and return with a null arraylist.
        	 * 
        	 */
        	return null;
        }
        HtmlDivision div = (HtmlDivision) divs.get(0);
        
        DomNode commentCountNode = div.getFirstChild().getFirstChild();
        String commentNumber = commentCountNode.asText();
        
        commentNumber = commentNumber.substring(0, commentNumber.indexOf(" "));
        int commentCount = Integer.parseInt(commentNumber); 
        
        int totalLoop = commentCount / 15;
        processComment(div, doc, webClient, commentList, elemKey);
        for(int i = 1; i <= totalLoop; i++){
        	WebClient client2 = new WebClient();
        	client2.getOptions().setThrowExceptionOnScriptError(false);
        	client2.getOptions().setThrowExceptionOnFailingStatusCode(false);
        	System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        	page = client2.getPage(URL + "?plckOnPage=" + (i + 1));
        	divs = page.getByXPath("//*[@class=\"pluck-comm-ReplyLevel-1\"]");
            if(divs.size() > 0){
            	div = (HtmlDivision) divs.get(0);
            	processComment(div, doc, client2, commentList, elemKey);
            }
        }
		return commentList;
	}
	
	private String returnAttribute(String xml){
    	String returnString = "";
    	returnString = xml.substring(0, xml.indexOf("\n"));
    	returnString = returnString.substring(returnString.indexOf("\"/") + 2, returnString.lastIndexOf("\""));
    	return returnString;
    }
	
	/**
	 * Converts a time stamp string into a specific format.
	 * @param timeDate
	 * @return
	 */
	private String timeStamp(String timeDate){
		String returnString = "";
		String time = timeDate.substring(0, timeDate.indexOf(" "));
		String date = timeDate.substring(timeDate.indexOf("on ") + 3);
		String hour = time.substring(0, time.indexOf(":"));
		String minute = time.substring(time.indexOf(":") + 1);
		String newHour = "";
		boolean am = timeDate.contains("AM");
		if(!am){
			returnString = hour + ":" + minute + " " + date;
			if(!hour.contains("12")){
				newHour = "" + (Integer.parseInt(hour) + 12);
				returnString = newHour + ":" + minute + " " + date;
			}
		}else {
			returnString = hour + ":" + minute + " " + date;
		}
		return returnString;
	}
	
	/**
	 * Processes a single comment with its replies
	 * @param div
	 * @param doc
	 * @param webClient
	 * @param comments
	 * @param elemKey
	 */
	private void processComment(HtmlDivision div, Document doc, WebClient webClient, ArrayList<CommentDocument> comments, String elemKey){
		List<DomNode> commentNodes= (List<DomNode>)div.getByXPath("//div[contains(@class, 'pluck-comm-single-comment-top')]");
        for(DomNode node : commentNodes){
        	CommentDocument comment = new CommentDocument();
        	List<DomNode> tempNodes = (List<DomNode>)  node.getByXPath(".//h4[contains(@class, 'pluck-comm-username-url pluck-comm-username-display')]");
        	boolean hasReply = false;
        	if(tempNodes.size() > 0){
        		String commentUserName = tempNodes.get(0).asText();
        		
        		tempNodes = (List<DomNode>)  node.getByXPath(".//span[contains(@class, 'pluck-score-downvotes-display')]");
        		String downTempNodesString = tempNodes.get(0).asText();
        		String downVotes = downTempNodesString.substring(1, downTempNodesString.indexOf(")"));
        		
        		NamedNodeMap attributes = node.getAttributes();
        		
        		if(attributes.item(attributes.getLength() - 1).toString().contains("pluck-comm-hasReplies")){
        			if(tempNodes.size() > 0)hasReply = true;
        		}
        		
        		tempNodes = (List<DomNode>)  node.getByXPath(".//span[contains(@class, 'pluck-score-upvotes-display')]");
        		String upTempNodesString = tempNodes.get(0).asText();
        		String upVotes = upTempNodesString.substring(1, upTempNodesString.indexOf(")"));
        		
        		
        		tempNodes = (List<DomNode>)  node.getByXPath(".//p[contains(@class, 'pluck-comm-timestamp')]");
            	String commentTimeStamp = timeStamp(tempNodes.get(0).asText());
            	
            	tempNodes = (List<DomNode>)  node.getByXPath(".//p[contains(@class, 'pluck-comm-body')]");
            	String commentBody = tempNodes.get(0).asText();
            	comment.setUserName(commentUserName);
            	comment.setTimeStamp(commentTimeStamp);
            	comment.setUpVote(Integer.parseInt(upVotes));
            	comment.setDownVote(Integer.parseInt(downVotes));
            	comment.setCommentBody(commentBody);
        	}
        	if(hasReply){
        		processCommentReplies(node, webClient, comment, elemKey);
            }
        	comments.add(comment);
	}
	
	}
	
	/**
	 * Processes the comment replies of a comment.
	 * @param node
	 * @param webClient
	 * @param comment
	 * @param elemKey
	 */
	private void processCommentReplies(DomNode node, WebClient webClient, CommentDocument comment, String elemKey){
		DomNode tempNode = node.getFirstChild();
		String parentID = returnAttribute(tempNode.asXml());
		ArrayList<CommentDocument> replyList = new ArrayList<CommentDocument>();
		JavaScriptPage commentPage = null;
		String commentPageLoc = "http://community.scotsman.com/ver1.0/JP-Standard/sys/jsonp?widget_path=pluck/comments/list&contentType=Html&plckCommentOnKeyType=article&plckCommentOnKey=" + elemKey + "&plckParentCommentPath=%2F" + parentID +"&plckSort=TimeStampAscending&plckOnPage=1&plckItemsPerPage=15&plckFilter=&plckLevel=2&plckParentHtmlId=pluck_comments_57197389&plckFindCommentKey=&clientUrl=http%3A%2F%2Fwww.scotsman.com%2Fnews%2Fpolitics%2Ftop-stories%2Fglasgow-2014-could-be-catalyst-for-euro-2024-bid-1-3496315&cb=plcb0u0";
		try {
			commentPage = webClient.getPage(commentPageLoc);
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	String response = commentPage.getContent();
    	if(response.length() > 10){
    		response = response.substring(response.indexOf("<"), response.lastIndexOf(">")) + ">";
        	response = response.substring(0, response.lastIndexOf(" <script type=\"text/javascript\">"));
        	
        	Document replyDoc = Jsoup.parse(response);
        	List<Element> userNames1 = replyDoc.select("div.pluck-comm-single-comment-main");
        	Element tempElement = userNames1.get(0);
        	List<Element> userNames = tempElement.select("h4.pluck-comm-username-url");
        	ArrayList<HashMap<String, String>> commentList = new ArrayList<HashMap<String, String>>();
        	int count = 0;
        	String tempText = "";
        	for(Element elem: userNames){
        		CommentDocument replyComment = new CommentDocument();
        		String userName = "", date = "", replyText = "";
        		if(count == 0){
        			tempText = elem.text();
        		}
        		count++;
        		if(elem.text().indexOf("Name withheld") == 0){
        			String text = elem.text();
            		text = text.substring(text.indexOf(" <\\/h4> ") + 8);
            		if(text.contains(" <\\/h4>")){
            			String tempDate = "";
            			try{
            			userName = text.substring(0, text.indexOf(" <\\/h4>"));
            			if(userName.contains("This comment was left by a user who has been blocked by our staff"))continue;
            			String otherText = text.substring(text.indexOf(" <\\/h4>") + 8);
                		tempDate = otherText.substring(0, otherText.indexOf("<\\/p>"));
                		date = timeStamp(otherText.substring(0, otherText.indexOf("<\\/p>")));
                		otherText = otherText.substring(otherText.indexOf("<\\/p> ") + 6); 
                		
                		otherText = otherText.substring(otherText.indexOf("<\\/p> ") + 6); 
                		
                		replyText = otherText.substring(otherText.indexOf(" <\\/p> ") + 7, otherText.indexOf("<\\/p> We limit the number"));
            			}catch (StringIndexOutOfBoundsException ie){
            				String queryText = "<\\/h4> " + userName +" <\\/h4> " + tempDate + "<\\/p> This comment is hidden because you have chosen to ignore " + userName + ".<\\/em> Show Details<\\/a>Hide Details<\\/a> <\\/p> This comment is hidden because you have submitted an abuse report against it.<\\/em> Show Details<\\/a>Hide Details<\\/a> <\\/p>";
            				String subStringText = tempText.substring(tempText.indexOf(queryText) + queryText.length());
            				replyText = subStringText.substring(0, subStringText.indexOf("<\\/p> We limit the number of reactions an individual"));
            			}
            		}
            		if(userName.length() > 0){
            			HashMap<String, String> tempHashMap = new HashMap<String, String>();
                		tempHashMap.put("userName", userName);
                		tempHashMap.put("timeStamp", date);
                		tempHashMap.put("replyText", replyText);
                		commentList.add(tempHashMap);
                		replyComment.setUserName(userName);
                		replyComment.setTimeStamp(date);
                		replyComment.setCommentBody(replyText);
                		replyList.add(replyComment);
            		}
        		}
        	}
        	List<Element> downList = replyDoc.select("div.pluck-comm-rate-controls");
        	count = 0;
        	if(downList.size() > 0) {
        		String rateString = downList.get(0).text();
        		ArrayList<HashMap<String, String>> rateList = parseRateString(rateString);
            	for(HashMap<String, String> tmpMap : rateList){
            		replyList.get(count).setUpVote(Integer.parseInt(tmpMap.get("up")));
            		replyList.get(count).setDownVote(Integer.parseInt(tmpMap.get("down")));
            	}
        	}
    	}
    	if(replyList.size()>0){
    		comment.setReplies(replyList);
    	}
	}
	
	/**
	 * Parse rating data from the rating string. The rating string contains the HTML output of a jQuery request.
	 * @param string	HTML response of the jQuery request
	 * @return			an array list containing the upvote and the downvote of a comment
	 */
	private ArrayList<HashMap<String,String>> parseRateString(String string){
		ArrayList<HashMap<String, String>> listString = new ArrayList<HashMap<String, String>>();
		String pattern = "<\\/span><\\/a><\\/span> (";
		boolean flag = true;
		while(flag){
			HashMap<String, String> tmpMap = new HashMap<String, String>();
			int loc = string.indexOf(pattern);
			if(loc >= 0){
				String subString = string.substring(loc - 3,  loc + pattern.length() + 2);
				String down = "" + subString.charAt(1);
				String up = "" + subString.charAt(subString.length() - 2);
				tmpMap.put("up", up);
				tmpMap.put("down", down);
				string = string.substring(loc + pattern.length() + 2);
				listString.add(tmpMap);
			} else flag = false;
		}
		return listString;
	}
	
	/**
	 * Stores a news document in the db.
	 */
	public void store() {
		// TODO Auto-generated method stub
		if(!dbUtils.find("title", scotsmanNews.getTitle()))dbUtils.addDocument(scotsmanNews);
		else {
			System.out.println("The news is already in the database.");
		}
	}

}
