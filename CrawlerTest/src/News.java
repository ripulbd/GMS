import java.io.File;
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
import com.gargoylesoftware.htmlunit.javascript.host.Node;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLButtonElement;


public class News {

	private static String outputFolder = "/home/ripul/images/scotsman/";
	private static ArrayList<Comment> comments = new ArrayList<Comment>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Document doc = null;
		String link = "http://www.scotsman.com/news/politics/top-stories/glasgow-2014-could-be-catalyst-for-euro-2024-bid-1-3496315";
        try {
        	System.out.println("Fetching data....");
            doc = Jsoup.connect(link).get();
            Element article = doc.getElementsByClass("article").first();
            Element image = article.getElementsByTag("img").first();
            Element story = article.getElementsByTag("div").get(article.getElementsByTag("div").size() - 1);
            
            String totalStory = story.toString();
            String mainStory = totalStory.substring(totalStory.indexOf("</p>") + 4);
            mainStory = mainStory.substring(0, mainStory.lastIndexOf("</div>"));
            
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
            
            //HtmlPage page = webClient.getPage("http://community.scotsman.com/ver1.0/JP-Standard/sys/jsonp?widget_path=pluck/comments/list&contentType=Html&plckCommentOnKeyType=article&plckCommentOnKey=ELM.1.3496315&plckParentCommentPath=%2F8D17C6D732AE490983&plckSort=TimeStampAscending&plckOnPage=1&plckItemsPerPage=15&plckFilter=&plckLevel=2&plckParentHtmlId=pluck_comments_57197389&plckFindCommentKey=&clientUrl=http%3A%2F%2Fwww.scotsman.com%2Fnews%2Fpolitics%2Ftop-stories%2Fglasgow-2014-could-be-catalyst-for-euro-2024-bid-1-3496315&cb=plcb0u0");
            
            //JavaScriptPage page = webClient.getPage("http://community.scotsman.com/ver1.0/JP-Standard/sys/jsonp?widget_path=pluck/comments/list&contentType=Html&plckCommentOnKeyType=article&plckCommentOnKey=ELM.1.3496315&plckParentCommentPath=%2F8D17C6D732AE490983&plckSort=TimeStampAscending&plckOnPage=1&plckItemsPerPage=15&plckFilter=&plckLevel=2&plckParentHtmlId=pluck_comments_57197389&plckFindCommentKey=&clientUrl=http%3A%2F%2Fwww.scotsman.com%2Fnews%2Fpolitics%2Ftop-stories%2Fglasgow-2014-could-be-catalyst-for-euro-2024-bid-1-3496315&cb=plcb0u0");
            
            HtmlPage page = webClient.getPage(link + "?plckOnPage=" + 2);
            
            List<?> divs =page.getByXPath("//*[@class=\"pluck-comm-ReplyLevel-1\"]");            
            HtmlDivision div = (HtmlDivision) divs.get(0);
            
            DomNode commentCountNode = div.getFirstChild().getFirstChild();
            String commentNumber = commentCountNode.asText();
            
            commentNumber = commentNumber.substring(0, commentNumber.indexOf(" "));
            int commentCount = Integer.parseInt(commentNumber); 
            
            int totalLoop = commentCount / 15;
            if((commentCount % 15) != 0)totalLoop++;
            processComment(div, doc, webClient);
            System.out.println("Total comments:" + commentCount + ", Comment Loop:" + totalLoop);
            /*for(int i = 0; i < totalLoop; i++){
            	if(i == 0)continue;
            	System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            	page = webClient.getPage(link + "?plckOnPage=" + (i + 1));       
                divs =page.getByXPath("//*[@class=\"pluck-comm-ReplyLevel-1\"]");            
                div = (HtmlDivision) divs.get(0);
                //processComment(div, doc, webClient);
            }*/
            
            
            
            /**
             * Code for retrieving image....
             */
            
            /*String imageName = image.attr("src");
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
        }
        System.out.println("Fetching finished....");
        
	}

	static public String returnAttribute(String xml){
    	String returnString = "";
    	returnString = xml.substring(0, xml.indexOf("\n"));
    	returnString = returnString.substring(returnString.indexOf("\"/") + 2, returnString.lastIndexOf("\""));
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
	
	static public void processComment(HtmlDivision div, Document doc, WebClient webClient){
		List<DomNode> commentNodes= (List<DomNode>)div.getByXPath("//div[contains(@class, 'pluck-comm-single-comment-top')]");
        for(DomNode node : commentNodes){
        	List<DomNode> tempNodes = (List<DomNode>)  node.getByXPath(".//h4[contains(@class, 'pluck-comm-username-url pluck-comm-username-display')]");
        	//String hasReply = "";
        	boolean hasReply = false;
        	if(tempNodes.size() > 0){
        		String commentUserName = tempNodes.get(0).asText();
        		
        		tempNodes = (List<DomNode>)  node.getByXPath(".//span[contains(@class, 'pluck-score-downvotes-display')]");
        		String downVotes = tempNodes.get(0).asText().substring(1, 2);
        		
        		NamedNodeMap attributes = node.getAttributes();
        		
        		if(attributes.item(attributes.getLength() - 1).toString().contains("pluck-comm-hasReplies")){
        			if(tempNodes.size() > 0)hasReply = true;
        		}
        		
        		tempNodes = (List<DomNode>)  node.getByXPath(".//span[contains(@class, 'pluck-score-upvotes-display')]");
        		String upVotes = tempNodes.get(0).asText().substring(1, 2);
        		
        		tempNodes = (List<DomNode>)  node.getByXPath(".//p[contains(@class, 'pluck-comm-timestamp')]");
            	String commentTimeStamp = timeStamp(tempNodes.get(0).asText());
            	
            	tempNodes = (List<DomNode>)  node.getByXPath(".//p[contains(@class, 'pluck-comm-body')]");
            	String commentBody = tempNodes.get(0).asText();
            	
            	System.out.println("UserName:" + commentUserName + ", TimeStamp:" + commentTimeStamp + ", UpVote:" + upVotes + ", DownVote:" + downVotes);
        	}
        	if(hasReply){
        		DomNode tempNode = node.getFirstChild();
        		//Node attribute = (Node) tempNode.getAttributes().getNamedItem("threadpath");
        		//System.out.println(tempNode.asXml());
        		//DomElement element = tempNode.
        		String parentID = returnAttribute(tempNode.asXml());
        		
        		JavaScriptPage commentPage = null;
        		String commentPageLoc = "http://community.scotsman.com/ver1.0/JP-Standard/sys/jsonp?widget_path=pluck/comments/list&contentType=Html&plckCommentOnKeyType=article&plckCommentOnKey=ELM.1.3496315&plckParentCommentPath=%2F" + parentID +"&plckSort=TimeStampAscending&plckOnPage=1&plckItemsPerPage=15&plckFilter=&plckLevel=2&plckParentHtmlId=pluck_comments_57197389&plckFindCommentKey=&clientUrl=http%3A%2F%2Fwww.scotsman.com%2Fnews%2Fpolitics%2Ftop-stories%2Fglasgow-2014-could-be-catalyst-for-euro-2024-bid-1-3496315&cb=plcb0u0";
            	try {
					commentPage = webClient.getPage(commentPageLoc);
					//System.out.println(commentPage.getContent());
					//List<?> divs = ((DomNode) commentPage).getByXPath("//*[@class=\"pluck-comm-ReplyLevel-1\"]");   
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
            	
            	
                
    			//System.out.println("Usual Response:" + commentPage.getWebResponse().getContentAsString());
            	//String response = jrww.jsonResponse;
            	String response = commentPage.getContent();
            	//String response = commentPage.getWebResponse().getContentAsString();
            	response = response.substring(response.indexOf("<"), response.lastIndexOf(">")) + ">";
            	response = response.substring(0, response.lastIndexOf(" <script type=\"text/javascript\">"));
            	//System.out.println("JSON Response:" + response);
            	
            	//Document replyDoc = Jsoup.parse(response);
            	Document replyDoc = Jsoup.parse(response);
            	//System.out.println("Reply Doc:" + replyDoc);
            	//List<Element> userNames = replyDoc.select("h4.pluck-comm-username-url");
            	List<Element> userNames1 = replyDoc.select("div.pluck-comm-single-comment-main");
            	//System.out.println(userNames);
            	Element tempElement = userNames1.get(0);
            	List<Element> userNames = tempElement.select("h4.pluck-comm-username-url");
            	//System.out.println(userNames);
            	ArrayList<HashMap<String, String>> commentList = new ArrayList<HashMap<String, String>>();
            	//System.out.println("Size of List:" + userNames.size());
            	int count = 0;
            	String tempText = "";
            	for(Element elem: userNames){
            		String userName = "", date = "", replyText = "";
            		//System.out.println("Elem Text:" + elem.text());
            		if(count == 0){
            			tempText = elem.text();
            		}
            		count++;
            		//if(elem.text().contains("This comment was left by a user who has been blocked by our staff"))continue;
            		if(elem.text().indexOf("Name withheld") == 0){
            			//if(elem.text().contains("This comment was left by a user who has been blocked by our staff"))continue;
            			String text = elem.text();
                		text = text.substring(text.indexOf(" <\\/h4> ") + 8);
                		if(text.contains(" <\\/h4>")){
                			String tempDate = "";
                			try{
                				
                			
                			userName = text.substring(0, text.indexOf(" <\\/h4>"));
                			if(userName.contains("This comment was left by a user who has been blocked by our staff"))continue;
                			//System.out.println("UserName:" + userName);
                			String otherText = text.substring(text.indexOf(" <\\/h4>") + 8);
                    		//System.out.println("other text:" + otherText);
                    		tempDate = otherText.substring(0, otherText.indexOf("<\\/p>"));
                    		date = timeStamp(otherText.substring(0, otherText.indexOf("<\\/p>")));
                    		
                    		//System.out.println("Date:" + date);
                    		
                    		otherText = otherText.substring(otherText.indexOf("<\\/p> ") + 6); 
                    		//System.out.println("other text2:" + otherText);
                    		
                    		otherText = otherText.substring(otherText.indexOf("<\\/p> ") + 6); 
                    		//System.out.println("other text2:" + otherText);
                    		
                    		replyText = otherText.substring(otherText.indexOf(" <\\/p> ") + 7, otherText.indexOf("<\\/p> We limit the number"));
                    		//System.out.println("Reply Text:" + replyText);
                			}catch (StringIndexOutOfBoundsException ie){
                				
                				//int firstIndex = tempText.lastIndexOf("Hide Details<\\/a> <\\/p>") + "Hide Details<\\/a> <\\/p>".length();
                				//int lastIndex = tempText.lastIndexOf("<\\/p> We limit the number of reactions an individual");
                				//replyText = tempText.substring(firstIndex, lastIndex);
                				//System.out.println("UserName:" + userName + ", Date:" + tempDate);
                				String queryText = "<\\/h4> " + userName +" <\\/h4> " + tempDate + "<\\/p> This comment is hidden because you have chosen to ignore " + userName + ".<\\/em> Show Details<\\/a>Hide Details<\\/a> <\\/p> This comment is hidden because you have submitted an abuse report against it.<\\/em> Show Details<\\/a>Hide Details<\\/a> <\\/p>";
                				//System.out.println("QueryText:" + queryText);
                				//System.out.println("TempText:" + tempText);
                				String subStringText = tempText.substring(tempText.indexOf(queryText) + queryText.length());
                				//System.out.println("SubString Text:" + subStringText);
                				replyText = subStringText.substring(0, subStringText.indexOf("<\\/p> We limit the number of reactions an individual"));
                				//System.out.println("SubString Text2:" + subStringText);
                				//System.out.println("Reply Text:" + replyText);
                			}
                		}
                		HashMap<String, String> tempHashMap = new HashMap<String, String>();
                		tempHashMap.put("userName", userName);
                		tempHashMap.put("timeStamp", date);
                		tempHashMap.put("replyText", replyText);
                		commentList.add(tempHashMap);
            		}
            		
            		
            		
            		
            		
            		//System.out.println("Withheld Position:" + elem.text().indexOf("Name withheld<\\/span>"));
            		/*System.out.println("Elem Text:" + elem.text());
            		if(elem.text().indexOf("Name withheld<\\/span>") == 0)continue;
            		String text = elem.text();
            		String userName = text.substring(0, text.indexOf(" <\\/h4>"));
            		//System.out.println("UserName:" + userName);
            		
            		String otherText = text.substring(text.indexOf(" <\\/h4>") + 8);
            		//System.out.println("other text:" + otherText);
            		
            		String date = timeStamp(otherText.substring(0, otherText.indexOf("<\\/p>")));
            		
            		//System.out.println("Date:" + date);
            		
            		otherText = otherText.substring(otherText.indexOf("<\\/p> ") + 6); 
            		//System.out.println("other text2:" + otherText);
            		
            		otherText = otherText.substring(otherText.indexOf("<\\/p> ") + 6); 
            		System.out.println("other text2:" + otherText);
            		
            		String replyText = otherText.substring(otherText.indexOf(" <\\/p> ") + 7, otherText.indexOf("<\\/p> We limit the number"));
            		//System.out.println("Reply Text:" + replyText);*/
            	}
            	System.out.println("CommentListSize:" + commentList.size());
            	List<Element> downList = replyDoc.select("span.pluck-thumb-down");
            	count = 0;
            	String tempRateString = "";
            	for(Element elem: downList){
            		String rateString = elem.text();
            		System.out.println("RateString:" + rateString);
            		if(count == 0){
            			tempRateString = rateString;
            		}
            		if(rateString.length() > 0){
            			
                		String down = rateString.substring(0, rateString.indexOf("<\\/"));
                		System.out.println("Down:" + down);
                		rateString = rateString.substring(rateString.indexOf("<\\/"));
                		//System.out.println("Rate String:" + rateString);
                		String up = rateString.substring(rateString.indexOf("span> ") + 6, rateString.indexOf(")<\\/span>") + 1);
                		System.out.println("Up:" + up);
                		commentList.get(count).put("downVote", down);
                		commentList.get(count).put("upVote", up);
                		count++;
                		System.out.println("Count:" + count);
            		}
            	}
            	
            	/*if(!commentList.get(count).containsKey("downKey")){
            		System.out.println("TempRateString:" + tempRateString);
        			String tryString = tempRateString.substring(tempRateString.indexOf("Please wait while we perform your request.<\\/div><\\/div> <\\/div> ") + "Please wait while we perform your request.<\\/div><\\/div> <\\/div> ".length(), tempRateString.lastIndexOf(")>") + 1);
        			System.out.println("TryString:" + tryString);
        		}*/
            	
            	System.out.println("Reply:-----------");
            	for(HashMap tmpHashMap: commentList){
            		System.out.println("UserName:" + tmpHashMap.get("userName") + ", TimeStamp:" + tmpHashMap.get("timeStamp")+ ", Reply Text:" + tmpHashMap.get("replyText") + ", DownVote:" + tmpHashMap.get("downVote") + ", UpVote:" + tmpHashMap.get("upVote") );
            	}
        	}
        	System.out.println("---------------------------------------------------------------------------------------");
        }
	}
}
