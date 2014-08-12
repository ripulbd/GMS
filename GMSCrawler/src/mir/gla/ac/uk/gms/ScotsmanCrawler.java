package mir.gla.ac.uk.gms;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.LogFactory;
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

	private ArrayList<HashMap<String, String>> urlInfos, relatedStories;
	private GMSNewsDocument scotsmanNews;
	//private String outputFolder = "/home/ripul/images/scotsman/";
	private String outputFolder = "/Users/ripul/images/scotman";
	private DBUtils dbUtils;
	
	public ScotsmanCrawler(String masterURL) {
		// TODO Auto-generated constructor stub
		super(masterURL);
		urlInfos = new ArrayList<HashMap<String, String>>();
		relatedStories = new ArrayList<HashMap<String, String>>();
		dbUtils = new DBUtils();
	}
	
	@Override
	public ArrayList<HashMap<String, String>> crawlURLs() throws IOException {
		// TODO Auto-generated method stub
		Document doc = null;
        System.out.println("Fetching RSS Link......");
        doc = Jsoup.connect(getMasterURL()).get();
        /*    
		Element rssLinkElement = doc.select("a.rss").first();
		String rssLink = "http://www.scotsman.com" + rssLinkElement.attr("href");

		Document rssDoc = Jsoup.connect(rssLink).get();

		for (Element item : rssDoc.select("item")) {
			String title = item.select("title").first().text();
			
			if (title.contains("<![CDATA[")) title = title.substring(title.indexOf("<![CDATA[") + "<![CDATA[".length(), title.lastIndexOf("]]>"));
			String URL = item.select("link").first().nextSibling().toString().trim(); // select 'link' (-1-)

			Document descr = Jsoup.parse(StringEscapeUtils.unescapeHtml4(item.select("description").first().toString()));
			String timeStamp = item.select("pubDate").first().text();
			
			if(timeStamp.contains(" +"))timeStamp = timeStamp.substring(timeStamp.indexOf(", ") + 2, timeStamp.lastIndexOf(" +"));
			else {
				timeStamp = timeStamp.substring(timeStamp.indexOf(", ") + 2);
				timeStamp = timeStamp.substring(0, timeStamp.lastIndexOf(":"));
				ArrayList<String> tokenizedTime = new ArrayList<String>();
				StringTokenizer st = new StringTokenizer(timeStamp);
				while(st.hasMoreElements()){
					tokenizedTime.add("" + st.nextElement());
				}
				
			}
			//System.out.println(timeStamp);
			HashMap<String, String> info = new HashMap<String, String>();
			info.put("title", title);
			info.put("url", URL);
			info.put("description", descr.text());
			info.put("timeStamp", timeStamp);
			urlInfos.add(info);
		}
		System.out.println("RSS link fetch finished......");*/
		
		System.out.println("Fetching links from the home page......");
		Elements articleLead = doc.select("article.lead-story");
		
		for(Element elem : articleLead){
			int size = elem.select("a").size();
			//System.out.println("Elem Size:" + size);
			Element anchor = elem.select("a").get(1);
			String title = anchor.text();
			String URL = anchor.attr("href");
			if(title.length() == 0)title = elem.select("a").get(2).text();
			//System.out.println("Title:" + title);
			//System.out.println("Link:" + URL);
			if(!alreadyInList(title))crawURLsFromHomePage(title, URL);
			else System.out.println("Title - " + title + " already in the list.");
		}
		
		Elements articleElement = doc.select("article.teaser");
		
		for(Element elem : articleElement){
			int size = elem.select("a").size();
			//System.out.println("Elem Size:" + size);
			Element anchor = elem.select("a").get(1);
			String title = anchor.text();
			String URL = anchor.attr("href");
			if(title.length() == 0)title = elem.select("a").get(2).text();
			//System.out.println("Title:" + title);
			//System.out.println("Link:" + URL);
			if(!alreadyInList(title))crawURLsFromHomePage(title, URL);
			else System.out.println("Title - " + title + " already in the list.");
		}
		
		System.out.println("Fetching links from the home page finished......");
		return urlInfos;
	}
	
	private void crawURLsFromHomePage(String title, String URL){
		
        Document doc; 
        String description = "", timeStamp = "";
		try {
			doc = Jsoup.connect(URL).get();
			description = doc.select("div.article-content").first().text();
			description = description.substring(0, description.indexOf("."));
			//System.out.println("Title:" + description);
			timeStamp = doc.select("div.Updated").first().text();
			timeStamp = timeStamp.substring(timeStamp.indexOf(" ") + 1);
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
	}
	
	private boolean alreadyInList(String title){
		for(HashMap<String, String> tmpMap : urlInfos){
			if(tmpMap.get("title").equals(title))return true;
		}
		return false;
	}
	
	@Override
	public GMSNewsDocument crawlNews(HashMap<String, String> info) throws IOException{
		// TODO Auto-generated method stub
		String URL = info.get("url");
		System.out.println("The link being retrieved is:" + URL);
		scotsmanNews = new GMSNewsDocument(URL);
		scotsmanNews.setCategory("news");
		scotsmanNews.setDate(info.get("timeStamp"));
		scotsmanNews.setDescription(info.get("description"));
		scotsmanNews.setSource("http://www.scotsman.com");
		scotsmanNews.setTitle(info.get("title"));
		
		Document doc = Jsoup.connect(URL).get();
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
        /*Element story = article.getElementsByTag("div").get(article.getElementsByTag("div").size() - 1);
        
        String totalStory = story.toString();
        returnString = totalStory.substring(totalStory.indexOf("</p>") + 4);*/
        //returnString = returnString.substring(0, returnString.lastIndexOf("</div>"));
        String relatedStoriesText = "";
        boolean seeAlso = false;
        if(returnString.contains("<p><strong>SEE ALSO</strong></p>")){
        	relatedStoriesText = returnString.substring(returnString.indexOf("<p><strong>SEE ALSO</strong></p>") + "<p><strong>SEE ALSO</strong></p>".length());
        	System.out.println("Related Stories Found:" + relatedStoriesText);
        	returnString = returnString.substring(0, returnString.indexOf("<p><strong>SEE ALSO</strong></p>"));
        	seeAlso = true;
        } 
        
        if(!seeAlso){
        	if(returnString.contains("<p><strong>SEE ALSO:</strong></p>")){
            	relatedStoriesText = returnString.substring(returnString.indexOf("<p><strong>SEE ALSO:</strong></p>") + "<p><strong>SEE ALSO:</strong></p>".length());
            	returnString = returnString.substring(0, returnString.indexOf("<p><strong>SEE ALSO:</strong></p>"));
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
            	tempRelated.put("url", relatedStoriesLink);
            	tempRelated.put("title", relatedStoriesTitle);
            	System.out.println("Related Storied URL:" + relatedStoriesLink + ", Related Stories Title:" + relatedStoriesTitle);
            	this.relatedStories.add(tempRelated);
            }
        }
        
		return returnString;
	}
	
	private ArrayList<HashMap<String, String>> retrieveImageAndStore(Document doc){
		ArrayList<HashMap<String, String>> imageLocAndCaption = new ArrayList<HashMap<String, String>>();
		
		Element article = doc.getElementsByClass("article").first();
		Element image = article.getElementsByTag("img").first();
		Element captionElement = article.select("p.flt-l").first();
		if(image != null){
			String caption = captionElement.text();
			if(caption.contains(" Picture"))caption = caption.substring(0, caption.indexOf(" Picture"));
	        String imageName = image.attr("src");
	        String imageLocation = "http://www.scotsman.com" + imageName;
	        
	        imageName = imageName.substring(imageName.lastIndexOf("/") + 1);
	        Response resultImageResponse = null;
			try {
				resultImageResponse = Jsoup.connect(imageLocation).ignoreContentType(true).execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(saveImage(resultImageResponse, imageLocAndCaption, imageName, caption)){
				//System.out.println("main image Saved and caption added!");
			}
			
			/**
			 * The following code is to test if there is another image...
			 */
			Element anotherImageDiv = article.getElementsByClass("left").first();
			
			if(anotherImageDiv != null){
				//System.out.println("Second Image found!");
				Element anotherImage = anotherImageDiv.getElementsByTag("img").first();
				String anotherImageName = anotherImage.attr("src");
				String anotherImageLocation = "http://www.scotsman.com" + anotherImageName;
				anotherImageName = anotherImageName.substring(anotherImageName.lastIndexOf("/") + 1);
				//System.out.println("Another Image Location:" + anotherImageLocation + ", Another Image Name:" + anotherImageName);
				Response anotherImageResponse = null;
				try {
					anotherImageResponse = Jsoup.connect(anotherImageLocation).ignoreContentType(true).execute();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(saveImage(anotherImageResponse, imageLocAndCaption, anotherImageName, "")){
					System.out.println("another image Saved and caption added!");
				}
			}

		} else {
			System.out.println("There is no image, probably a video in this news.");
		}
		
		
        return imageLocAndCaption;
	}
	
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
        //System.out.println("CommentCountFromHTML:" + commentCount + ", totalLoop:" + totalLoop);
        processComment(div, doc, webClient, commentList, elemKey);
        for(int i = 1; i <= totalLoop; i++){
        	WebClient client2 = new WebClient();
        	client2.getOptions().setThrowExceptionOnScriptError(false);
        	client2.getOptions().setThrowExceptionOnFailingStatusCode(false);
        	System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        	page = client2.getPage(URL + "?plckOnPage=" + (i + 1));
        	//System.out.println("Location:" + URL + "?plckOnPage=" + (i + 1));
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
	
	private void processComment(HtmlDivision div, Document doc, WebClient webClient, ArrayList<CommentDocument> comments, String elemKey){
		List<DomNode> commentNodes= (List<DomNode>)div.getByXPath("//div[contains(@class, 'pluck-comm-single-comment-top')]");
        for(DomNode node : commentNodes){
        	CommentDocument comment = new CommentDocument();
        	List<DomNode> tempNodes = (List<DomNode>)  node.getByXPath(".//h4[contains(@class, 'pluck-comm-username-url pluck-comm-username-display')]");
        	//String hasReply = "";
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
            	
            	
            	//System.out.println("Main Comment - UserName:" + commentUserName + ", TimeStamp:" + commentTimeStamp + ", UpVote:" + upVotes + ", DownVote:" + downVotes);
        	}
        	if(hasReply){
        		processCommentReplies(node, webClient, comment, elemKey);
        	    //System.out.println("---------------------------------------------------------------------------------------");
            }
        	comments.add(comment);
	}
	
	}
	
	private void processCommentReplies(DomNode node, WebClient webClient, CommentDocument comment, String elemKey){
		DomNode tempNode = node.getFirstChild();
		String parentID = returnAttribute(tempNode.asXml());
		//String elemKey = 
		ArrayList<CommentDocument> replyList = new ArrayList<CommentDocument>();
		JavaScriptPage commentPage = null;
		String commentPageLoc = "http://community.scotsman.com/ver1.0/JP-Standard/sys/jsonp?widget_path=pluck/comments/list&contentType=Html&plckCommentOnKeyType=article&plckCommentOnKey=" + elemKey + "&plckParentCommentPath=%2F" + parentID +"&plckSort=TimeStampAscending&plckOnPage=1&plckItemsPerPage=15&plckFilter=&plckLevel=2&plckParentHtmlId=pluck_comments_57197389&plckFindCommentKey=&clientUrl=http%3A%2F%2Fwww.scotsman.com%2Fnews%2Fpolitics%2Ftop-stories%2Fglasgow-2014-could-be-catalyst-for-euro-2024-bid-1-3496315&cb=plcb0u0";
    	//System.out.println("CommentPageLoc:" + commentPageLoc);
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
                		//System.out.println("UserName:" + userName + ", TimeStamp:" + date + ", Reply Text:" + replyText);
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
        	
        	
        	/*List<Element> downList = replyDoc.select("span.pluck-thumb-down");
        	count = 0;
        	String tempRateString = "";
        	boolean flag = false;
        	System.out.println("Down List Size:" + downList.size());
        	for(Element elem: downList){
        		String rateString = elem.text();
        		System.out.println("Rate String:\n" + rateString);
        		if(count == 0){
        			tempRateString = rateString;
        		}
        		if(rateString.length() > 0){
        			
            		String down = rateString.substring(0, rateString.indexOf("<\\/"));
            		down = down.substring(1, down.indexOf(")"));
            		rateString = rateString.substring(rateString.indexOf("<\\/"));
            		String up = rateString.substring(rateString.indexOf("span> ") + 6, rateString.indexOf(")<\\/span>") + 1);
            		up = up.substring(1, up.indexOf(")"));
            		commentList.get(count).put("downVote", down);
            		commentList.get(count).put("upVote", up);
            		System.out.println("Reply Comment Up Vote:" + up);
            		System.out.println("Reply Comment down Vote:" + down);
            		replyList.get(count).setUpVote(Integer.parseInt(up));
            		replyList.get(count).setDownVote(Integer.parseInt(down));
            		count++;
        		} else{
        			flag = true;
        		}
        	}
        	
        	if(flag){
        		int firstIndex = tempRateString.lastIndexOf("Please wait while we perform your request.<\\/div><\\/div> <\\/div> ") + "Please wait while we perform your request.<\\/div><\\/div> <\\/div> ".length();
        		String firstSubString = tempRateString.substring(firstIndex);
        		int lastIndex = firstSubString.indexOf("<\\/span><\\/a><\\/span>You voted<\\/span>");
        		System.out.println("First Index:" + firstIndex + ", lastIndex:" + lastIndex);
        		System.out.println("First Sub String:" + firstSubString);
        		String lastSubString = firstSubString.substring(0, lastIndex);
        		System.out.println("Last Sub String:" + lastSubString);
        		
        		String down = lastSubString.substring(1, lastSubString.indexOf(")"));
        		String up = lastSubString.substring(lastSubString.lastIndexOf("(") + 1, lastSubString.lastIndexOf(")"));
        		commentList.get(count).put("downVote", down);
        		commentList.get(count).put("upVote", up);
        		replyList.get(count).setUpVote(Integer.parseInt(up));
        		replyList.get(count).setDownVote(Integer.parseInt(down));
    		}*/
    	}
    	if(replyList.size()>0){
    		comment.setReplies(replyList);
    	}
	}
	
	private ArrayList<HashMap<String,String>> parseRateString(String string){
		ArrayList<HashMap<String, String>> listString = new ArrayList<HashMap<String, String>>();
		String pattern = "<\\/span><\\/a><\\/span> (";
		boolean flag = true;
		while(flag){
			HashMap<String, String> tmpMap = new HashMap<String, String>();
			int loc = string.indexOf(pattern);
			//System.out.println("Location:" + loc);
			if(loc >= 0){
				/*String firstNumber = string.substring(1, loc);
				String secondNumber = string.substring(loc + pattern.length(), );*/
				String subString = string.substring(loc - 3,  loc + pattern.length() + 2);
				//System.out.println(subString);
				//System.out.println("Number:" + subString.charAt(1) + " - " + subString.charAt(subString.length() - 2));
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

	@Override
	public void store() {
		// TODO Auto-generated method stub
		if(!dbUtils.find("title", scotsmanNews.getTitle()))dbUtils.addDocument(scotsmanNews);
		else {
			System.out.println("The news is already in the database.");
		}
	}

}
