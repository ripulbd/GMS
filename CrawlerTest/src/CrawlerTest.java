import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.lang3.StringEscapeUtils;






import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.JavaScriptPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.javascript.host.Node;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLButtonElement;


public class CrawlerTest {

	private static String outputFolder = "/home/ripul/images/scotsman/";
	private static int totalCommentCount = 0;
	static String elemKey = "";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Document doc = null;
		ArrayList<Comment> comments = new ArrayList<Comment>();
		String link = "http://www.scotsman.com/news/politics/top-stories/glasgow-2014-could-be-catalyst-for-euro-2024-bid-1-3496315";
		//String link = "http://www.scotsman.com/news/scotland/glasgow-west";
		//String link = "http://www.scotsman.com/news/politics/top-stories/labour-councils-used-public-cash-for-awards-trip-1-3498342";
		//String link = "http://www.scotsman.com/news/politics/top-stories/plan-b-or-not-plan-b-salmond-rejects-the-question-1-3501684";
        try {
        	System.out.println("Fetching data....");
            doc = Jsoup.connect(link).get();
            
            //Element rssLinkElement = doc.select("a.rss").first();
            //String rssLink = "http://www.scotsman.com" + rssLinkElement.attr("href");
            //System.out.println("RSSLink:" + rssLink);
            
            //Document rssDoc = Jsoup.connect(rssLink).get();
            
            //rssDoc = Jsoup.parse(rssDoc.html());
            //rssDoc = Jsoup.parse(rssDoc.html());
            //System.out.println(StringEscapeUtils.unescapeHtml3(rssDoc.html()));
            //System.out.println(rssDoc.html());
            
            /*for( Element item : rssDoc.select("item") ){
            	String title = item.select("title").first().text(); // select the 'title' of the item
            	if(title.contains("<![CDATA["))title = title.substring(title.indexOf("<![CDATA[") + "<![CDATA[".length(), title.lastIndexOf("]]>"));
                String URL = item.select("link").first().nextSibling().toString().trim(); // select 'link' (-1-)

                Document descr = Jsoup.parse(StringEscapeUtils.unescapeHtml4(item.select("description").first().toString()));
                String timeStamp = item.select("pubDate").first().text(); // select 'link' (-1-)
                timeStamp = timeStamp.substring(timeStamp.indexOf(", ") + 2, timeStamp.lastIndexOf(" +"));
                System.out.println("-----------------------------------------");
                System.out.println("Title:" + title);
                System.out.println("Link:" + URL);
                System.out.println("Description:" + descr.text());
                System.out.println("PubDate:" + timeStamp);
                //System.out.println();
            }*/
            
            //retrieveMainStory(doc);
            //retrieveImageAndStore(doc);
            
            String docText = doc.html();
            elemKey = docText.substring(docText.indexOf("plckCommentOnKey: \"ELM.1") + "plckCommentOnKey: ".length() + 1);
            elemKey = elemKey.substring(0, elemKey.indexOf("\""));
            System.out.println("ElemKey:" + elemKey);
            
            //System.out.println("Whole Store:" + mainStory);
            /*System.out.println("----------------------------------------------------------------------");
            System.out.println("Whole Document");
            System.out.println(doc);
            System.out.println("----------------------------------------------------------------------");*/
            
            /**
             * code for retrieving the comments...........
             */
            
            
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
            
            
            HtmlPage page = webClient.getPage(link + "?plckOnPage=1");
            
            List<?> divs =page.getByXPath("//*[@class=\"pluck-comm-ReplyLevel-1\"]");            
            HtmlDivision div = (HtmlDivision) divs.get(0);
            
            FileOutputStream out;
    		try {
    			out = (new FileOutputStream(new java.io.File("/home/ripul/git/response.txt")));
    			out.write(page.asXml().getBytes());
    	        out.close();
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

            DomNode commentCountNode = div.getFirstChild().getFirstChild();
            String commentNumber = commentCountNode.asText();
            
            commentNumber = commentNumber.substring(0, commentNumber.indexOf(" "));
            int commentCount = Integer.parseInt(commentNumber); 
            
            int totalLoop = commentCount / 15;
            System.out.println("CommentCountFromHTML:" + commentCount + ", totalLoop:" + totalLoop);
            processComment(div, doc, webClient, comments);
            for(int i = 1; i <= totalLoop; i++){
            	WebClient client2 = new WebClient();
            	client2.getOptions().setThrowExceptionOnScriptError(false);
            	client2.getOptions().setThrowExceptionOnFailingStatusCode(false);
            	System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            	page = client2.getPage(link + "?plckOnPage=" + (i + 1));
            	System.out.println("Location:" + link + "?plckOnPage=" + (i + 1));
        		try {
        			out = (new FileOutputStream(new java.io.File("/home/ripul/git/response2.txt")));
        			out.write(page.asXml().getBytes());
        	        out.close();
        		} catch (FileNotFoundException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            	divs = page.getByXPath("//*[@class=\"pluck-comm-ReplyLevel-1\"]");
                if(divs.size() > 0){
                	div = (HtmlDivision) divs.get(0);
                	processComment(div, doc, client2, comments);
                }
                
                
            }
            
            
            /**
             * Code for retrieving image....
             */
            
            /*
            Element image = article.getElementsByTag("img").first();
            String imageName = image.attr("src");
            String imageLocation = "http://www.scotsman.com" + imageName;
            System.out.println("Image Loc: " + imageLocation);
            
            String onlyImageName = imageName.substring(imageName.lastIndexOf("/") + 1);
            System.out.println("Image Name: " + onlyImageName);
            
          //Open a URL Stream
            Response resultImageResponse = Jsoup.connect(imageLocation).ignoreContentType(true).execute();

            // output here
            FileOutputStream out = (new FileOutputStream(new java.io.File(outputFolder + onlyImageName)));
            out.write(resultImageResponse.bodyAsBytes());           // resultImageResponse.body() is where the image's contents are.
            out.close();*/
            
            
        }catch (IOException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }/*catch (IndexOutOfBoundsException ie){
        	
        }*/
        for(Comment comment: comments){
        	System.out.println(comment);
        }
        
        System.out.println("Total comments:" + comments.size());
        System.out.println("Fetching finished....");
        
	}
	
	static void retrieveImageAndStore(Document doc){
		Element article = doc.getElementsByClass("article").first();
		Element image = article.getElementsByTag("img").first();
		Element captionElement = article.select("p.flt-l").first();
		String caption = captionElement.text();
		if(caption.contains(" Picture"))caption = caption.substring(0, caption.indexOf(" Picture"));
        String imageName = image.attr("src");
        String imageLocation = "http://www.scotsman.com" + imageName;
        System.out.println("Image Loc: " + imageLocation);
        
        imageName = imageName.substring(imageName.lastIndexOf("/") + 1);
        System.out.println("Image Name: " + imageName);
        System.out.println("Image Caption: " + caption);
        
        Response resultImageResponse = null;
		try {
			resultImageResponse = Jsoup.connect(imageLocation).ignoreContentType(true).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        FileOutputStream out;
		try {
			out = (new FileOutputStream(new java.io.File(outputFolder + imageName)));
			out.write(resultImageResponse.bodyAsBytes());
	        out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	static public String retrieveMainStory(Document doc){
		String returnString = "";
		
		Element article = doc.getElementsByClass("article").first();
        Element story = article.getElementsByTag("div").get(article.getElementsByTag("div").size() - 1);
        
        String totalStory = story.toString();
        String mainStory = totalStory.substring(totalStory.indexOf("</p>") + 4);
        mainStory = mainStory.substring(0, mainStory.lastIndexOf("</div>"));
        String relatedStories = "";
        if(mainStory.contains("<p><strong>SEE ALSO</strong></p>")){
        	relatedStories = mainStory.substring(mainStory.indexOf("<p><strong>SEE ALSO</strong></p>") + "<p><strong>SEE ALSO</strong></p>".length());
        	mainStory = mainStory.substring(0, mainStory.indexOf("<p><strong>SEE ALSO</strong></p>"));
        }
        
        System.out.println("Main Story:");
        
        System.out.println(mainStory);
        
        if(relatedStories.length() > 3){
        	System.out.println("Related Stories:");
            System.out.println(relatedStories);
            Document related = Jsoup.parse(relatedStories);
            Elements relatedStoriesLinks = related.select("a");
            for(Element elem: relatedStoriesLinks){
            	String relatedStoriesLink = elem.attr("href");
            	String relatedStoriesTitle = elem.text();
            	System.out.println("Related Stories - Link:" + relatedStoriesLink + ", Title:" + relatedStoriesTitle);
            }
        }
        
		return returnString;
	}

	static public String returnAttribute(String xml){
    	String returnString = "";
    	returnString = xml.substring(0, xml.indexOf("\n"));
    	returnString = returnString.substring(returnString.indexOf("\"/") + 2, returnString.lastIndexOf("\""));
    	return returnString;
    }
	
	static public String returnElemKey(String xml){
		String returnString = "";
		
		return returnString;
	}
	
	static String timeStamp(String timeDate){
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
	
	static public void processComment(HtmlDivision div, Document doc, WebClient webClient, ArrayList<Comment> comments){
		List<DomNode> commentNodes= (List<DomNode>)div.getByXPath("//div[contains(@class, 'pluck-comm-single-comment-top')]");
        for(DomNode node : commentNodes){
        	Comment comment = new Comment();
        	totalCommentCount++;
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
        		//System.out.println("Main Comment Up Vote:" + upVotes);
        		//System.out.println("Main Comment down Vote:" + downVotes);
        		
        		tempNodes = (List<DomNode>)  node.getByXPath(".//p[contains(@class, 'pluck-comm-timestamp')]");
            	String commentTimeStamp = timeStamp(tempNodes.get(0).asText());
            	
            	tempNodes = (List<DomNode>)  node.getByXPath(".//p[contains(@class, 'pluck-comm-body')]");
            	String commentBody = tempNodes.get(0).asText();
            	comment.setUserName(commentUserName);
            	comment.setTimeStamp(commentTimeStamp);
            	comment.setUpVote(Integer.parseInt(upVotes));
            	comment.setDownVote(Integer.parseInt(downVotes));
            	comment.setCommentBody(commentBody);
            	
            	//System.out.println("UserName:" + commentUserName + ", TimeStamp:" + commentTimeStamp + ", UpVote:" + upVotes + ", DownVote:" + downVotes);
        	}
        	if(hasReply){
        		processCommentReplies(node, webClient, comment);
        	    //System.out.println("---------------------------------------------------------------------------------------");
            }
        	comments.add(comment);
	}
	
	}
	
	static public void processCommentReplies(DomNode node, WebClient webClient, Comment comment){
		DomNode tempNode = node.getFirstChild();
		String parentID = returnAttribute(tempNode.asXml());
		//String elemKey = 
		ArrayList<Comment> replyList = new ArrayList<Comment>();
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
    	
    	//commentPage.
    	
    	//List<DomNode> innerCommentNodes = commentPage.
    	/*JsonResponseWebWrapper jrww = new JsonResponseWebWrapper(webClient);
    	
    	WebRequest req;
    	WebResponse resp;
		try {
			req = new WebRequest(new URL(commentPageLoc));
			resp = jrww.getResponse(req);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	
    	String response = commentPage.getContent();
    	//System.out.println("Response:" + response);
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
        		Comment replyComment = new Comment();
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
        	List<Element> downList = replyDoc.select("span.pluck-thumb-down");
        	count = 0;
        	String tempRateString = "";
        	boolean flag = false;
        	for(Element elem: downList){
        		String rateString = elem.text();
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
            		//System.out.println("Reply Comment Up Vote:" + up);
            		//System.out.println("Reply Comment down Vote:" + down);
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
        		String lastSubString = firstSubString.substring(0, lastIndex);
        		
        		String down = lastSubString.substring(1, lastSubString.indexOf(")"));
        		String up = lastSubString.substring(lastSubString.lastIndexOf("(") + 1, lastSubString.lastIndexOf(")"));
        		commentList.get(count).put("downVote", down);
        		commentList.get(count).put("upVote", up);
        		replyList.get(count).setUpVote(Integer.parseInt(up));
        		replyList.get(count).setDownVote(Integer.parseInt(down));
        		//System.out.println("Reply Comment Up Vote:" + up);
        		//System.out.println("Reply Comment down Vote:" + down);
    		}
        	
        	/*if(commentList.size()>0)System.out.println("Reply:-----------");
        	for(HashMap tmpHashMap: commentList){
        		System.out.println("UserName:" + tmpHashMap.get("userName") + ", TimeStamp:" + tmpHashMap.get("timeStamp")+ ", Reply Text:" + tmpHashMap.get("replyText") + ", DownVote:" + tmpHashMap.get("downVote") + ", UpVote:" + tmpHashMap.get("upVote") );
        	}*/
        	
        	
        	//comments.add(comment);
    	}
    	if(replyList.size()>0){
    		//System.out.println("Reply:-----------");
    		comment.setReplies(replyList);
    	}
	}
}
