import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Document doc = null;
        try {
        	System.out.println("Fetching data....");
            doc = Jsoup.connect("http://www.scotsman.com/news/politics/top-stories/glasgow-2014-could-be-catalyst-for-euro-2024-bid-1-3496315").get();
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
            
            HtmlPage page = webClient.getPage("http://www.scotsman.com/news/politics/top-stories/glasgow-2014-could-be-catalyst-for-euro-2024-bid-1-3496315");
            
            List<?> divs =page.getByXPath("//*[@class=\"pluck-comm-ReplyLevel-1\"]");            
            HtmlDivision div = (HtmlDivision) divs.get(0);
            
            DomNode commentCountNode = div.getFirstChild().getFirstChild();
            String commentNumber = commentCountNode.asText();
            
            commentNumber = commentNumber.substring(0, commentNumber.indexOf(" "));
            int commentCount = Integer.parseInt(commentNumber); 
            		
            
            List<DomNode> commentNodes= (List<DomNode>)div.getByXPath("//div[contains(@class, 'pluck-comm-single-comment-top')]");
            for(DomNode node : commentNodes){
            	List<DomNode> tempNodes = (List<DomNode>)  node.getByXPath(".//h4[contains(@class, 'pluck-comm-username-url pluck-comm-username-display')]");
            	//String hasReply = "";
            	boolean hasReply = false;
            	if(tempNodes.size() > 0){
            		String commentUserName = tempNodes.get(0).asText();
            		
            		tempNodes = (List<DomNode>)  node.getByXPath(".//span[contains(@class, 'pluck-score-downvotes-display')]");
            		String downVotes = tempNodes.get(0).asText();
            		
            		NamedNodeMap attributes = node.getAttributes();
            		
            		if(attributes.item(attributes.getLength() - 1).toString().contains("pluck-comm-hasReplies")){
            			if(tempNodes.size() > 0)hasReply = true;
            		}
            		
            		tempNodes = (List<DomNode>)  node.getByXPath(".//span[contains(@class, 'pluck-score-upvotes-display')]");
            		String upVotes = tempNodes.get(0).asText();
            		
            		tempNodes = (List<DomNode>)  node.getByXPath(".//p[contains(@class, 'pluck-comm-timestamp')]");
                	String commentTimeStamp = tempNodes.get(0).asText();
                	
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
            		
            		
            		String commentPageLoc = "http://community.scotsman.com/ver1.0/JP-Standard/sys/jsonp?widget_path=pluck/comments/list&contentType=Html&plckCommentOnKeyType=article&plckCommentOnKey=ELM.1.3496315&plckParentCommentPath=%2F" + parentID +"&plckSort=TimeStampAscending&plckOnPage=1&plckItemsPerPage=15&plckFilter=&plckLevel=2&plckParentHtmlId=pluck_comments_57197389&plckFindCommentKey=&clientUrl=http%3A%2F%2Fwww.scotsman.com%2Fnews%2Fpolitics%2Ftop-stories%2Fglasgow-2014-could-be-catalyst-for-euro-2024-bid-1-3496315&cb=plcb0u0";
                	//JavaScriptPage commentPage = webClient.getPage(commentPageLoc);
                	
                	//commentPage.
                	
                	//List<DomNode> innerCommentNodes = commentPage.
                	JsonResponseWebWrapper jrww = new JsonResponseWebWrapper(webClient);
                	
                	WebRequest req = new WebRequest(new URL(commentPageLoc));
                	
                	WebResponse resp = jrww.getResponse(req);
                    
        			//System.out.println("Usual Response:" + commentPage.getWebResponse().getContentAsString());
                	String response = jrww.jsonResponse;
                	//String response = commentPage.getWebResponse().getContentAsString();
                	response = response.substring(response.indexOf("<"), response.lastIndexOf(">")) + ">";
                	//System.out.println("JSON Response:" + response);
                	
                	Document replyDoc = Jsoup.parse(response);
                	List<Element> userNames = replyDoc.select("h4.pluck-comm-username-url");
                	ArrayList<HashMap<String, String>> commentList = new ArrayList<HashMap<String, String>>();
                	
                	for(Element elem: userNames){
                		//System.out.println("Withheld Position:" + elem.text().indexOf("Name withheld<\\/span>"));
                		if(elem.text().indexOf("Name withheld<\\/span>") == 0)continue;
                		//if(elem.text().contains("Name withheld<\\/span>"))continue;
                		String text = elem.text();
                		String userName = text.substring(0, text.indexOf(" <\\/h4>"));
                		//System.out.println("UserName:" + userName);
                		
                		String otherText = text.substring(text.indexOf(" <\\/h4>") + 8);
                		//System.out.println("other text:" + otherText);
                		
                		String date = otherText.substring(0, otherText.indexOf("<\\/p>"));
                		
                		//System.out.println("Date:" + date);
                		
                		otherText = otherText.substring(otherText.indexOf("<\\/p> ") + 6); 
                		//System.out.println("other text2:" + otherText);
                		
                		otherText = otherText.substring(otherText.indexOf("<\\/p> ") + 6); 
                		//System.out.println("other text2:" + otherText);
                		
                		String replyText = otherText.substring(otherText.indexOf(" <\\/p> ") + 7, otherText.indexOf("<\\/p> We limit the number"));
                		//System.out.println("Reply Text:" + replyText);
                		HashMap<String, String> tempHashMap = new HashMap<String, String>();
                		tempHashMap.put("userName", userName);
                		tempHashMap.put("timeStamp", date);
                		tempHashMap.put("replyText", replyText);
                		commentList.add(tempHashMap);
                		//Hide Details<\/a> <\/p> 
                		//System.out.println("Clean UserName:" + Jsoup.parse(elem.text()));
                	}
                	
                	List<Element> downList = replyDoc.select("span.pluck-thumb-down");
                	int count = 0;
                	for(Element elem: downList){
                		String rateString = elem.text();
                		String down = rateString.substring(0, rateString.indexOf("<\\/"));
                		//System.out.println("Down:" + down);
                		rateString = rateString.substring(rateString.indexOf("<\\/"));
                		//System.out.println("Rate String:" + rateString);
                		String up = rateString.substring(rateString.indexOf("span> ") + 6, rateString.indexOf(")<\\/span>") + 1);
                		//System.out.println("Up:" + up);
                		commentList.get(count).put("downVote", down);
                		commentList.get(count).put("upVote", up);
                		count++;
                	}
                	
                	System.out.println("Reply:-----------");
                	for(HashMap tmpHashMap: commentList){
                		System.out.println("UserName:" + tmpHashMap.get("userName") + ", TimeStamp:" + tmpHashMap.get("timeStamp")+ ", Reply Text:" + tmpHashMap.get("replyText") + ", DownVote:" + tmpHashMap.get("downVote") + ", UpVote:" + tmpHashMap.get("upVote") );
                	}
                	
                	/*List<Element> upList = replyDoc.select("span.pluck-thumb-up");
                	for(Element elem: upList){
                		System.out.println("Up:" + elem.text());
                	}*/
                	
                	/*List<Element> replyDivs = replyDoc.getElementsByClass("pluck-comm-ReplyLevel-2");
                	List<Element> innerReplyDivs = replyDivs.get(0).getElementsByClass("pluck-comm-single-comment-top");
                	System.out.println("Reply Size:" + innerReplyDivs.size());
                	for(int i = 0; i < innerReplyDivs.size(); i++){
                		Element replyDiv = innerReplyDivs.get(i);
                		//System.out.println("Reply Div:" + replyDiv);
                		List<Element> uNames = replyDiv.getElementsByTag("h4");
                		for(Element tElement: uNames){
                			if(tElement.hasClass("pluck-comm-username-url")){
                				String userName = tElement.text();
                        		System.out.println("UserName:" + userName);
                			}
                		}
                		
                		/*List<DomNode> replyCommentNodes = (List<DomNode>)replyDiv.getByXPath("//div[contains(@class, 'pluck-comm-single-comment-top')]");
                		for(DomNode replyNode : replyCommentNodes){
                			System.out.println("Here....");
                        	List<DomNode> innertempNodes = (List<DomNode>)  replyNode.getByXPath(".//h4[contains(@class, 'pluck-comm-username-url pluck-comm-username-display')]");
                        	if(innertempNodes.size() > 0){
                        		String commentUserName = tempNodes.get(0).asText();
                        		
                        		tempNodes = (List<DomNode>)  replyNode.getByXPath(".//span[contains(@class, 'pluck-score-downvotes-display')]");
                        		String downVotes = tempNodes.get(0).asText();
                        		
                        		tempNodes = (List<DomNode>)  replyNode.getByXPath(".//span[contains(@class, 'pluck-score-upvotes-display')]");
                        		String upVotes = tempNodes.get(0).asText();
                        		
                        		tempNodes = (List<DomNode>)  replyNode.getByXPath(".//p[contains(@class, 'pluck-comm-timestamp')]");
                            	String commentTimeStamp = tempNodes.get(0).asText();
                            	
                            	tempNodes = (List<DomNode>)  replyNode.getByXPath(".//p[contains(@class, 'pluck-comm-body')]");
                            	String commentBody = tempNodes.get(0).asText();
                            	
                            	System.out.println("Reply UserName:" + commentUserName + ", Reply TimeStamp:" + commentTimeStamp + ", Reply UpVote:" + upVotes + ", Reply DownVote:" + downVotes);
                        	}
                		}*/
                	//}
                	
                	
					//System.out.println("Reply HTML Doc:");
					//System.out.println(replyDoc.html());
                	/*Reader stringReader = new StringReader(response);
                	HTMLEditorKit htmlKit = new HTMLEditorKit();
                	HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
                	try {
						htmlKit.read(stringReader, htmlDoc, 0);
						System.out.println("Reply HTML Doc:");
						StringWriter writer = new StringWriter();
						htmlKit.write(writer, htmlDoc, 0, htmlDoc.getLength());
						String s = writer.toString();
						System.out.println(s);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
                	
                	/*JSONObject jsonObj = new JSONObject(jrww.jsonResponse);
                	jsonObj.getJSONArray("")*/
            	}
            	System.out.println("---------------------------------------------------------------------------------------");
            }
            
            
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
    	/*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  
        {  
            builder = factory.newDocumentBuilder();  
            org.w3c.dom.Document document = builder.parse( new InputSource( new StringReader( xml ) ) );
            System.out.println("XML Document\n" + document.toString());
        } catch (Exception e) {  
            e.printStackTrace();  
        }*/
    	returnString = xml.substring(0, xml.indexOf("\n"));
    	returnString = returnString.substring(returnString.indexOf("\"/") + 2, returnString.lastIndexOf("\""));
    	return returnString;
    	
    }
}
