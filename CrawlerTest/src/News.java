import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.NamedNodeMap;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Node;


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
            
            /*System.out.println("----------------------------------------------------------------------");
            System.out.println("Whole Document");
            System.out.println(doc);
            System.out.println("----------------------------------------------------------------------");*/
            
            /**
             * code for retrieving the comments...........
             */
            
            /*Element outerSectionElement = doc.getElementById("comments");
            
            System.out.println("Outer Section: " + outerSectionElement);*/
            
            //WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24);
            //HtmlPage page = webClient.getPage("http://www.scotsman.com/news/politics/top-stories/glasgow-2014-could-be-catalyst-for-euro-2024-bid-1-3496315");
            
         // turn off htmlunit warnings
            java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
            java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

            WebClient webClient = new WebClient();
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            HtmlPage page = webClient.getPage("http://www.scotsman.com/news/politics/top-stories/glasgow-2014-could-be-catalyst-for-euro-2024-bid-1-3496315");
            //System.out.println(page.getTitleText());
            //List<?> divs = page.getByXPath("//*[@id=\"pluck_comments_list_53837001\"]/div[2]/div");
            List<?> divs =page.getByXPath("//*[@class=\"pluck-comm-ReplyLevel-1\"]");
            
            HtmlDivision div = (HtmlDivision) divs.get(0);
            
            DomNode commentCountNode = div.getFirstChild().getFirstChild();
            String commentNumber = commentCountNode.asText();
            
            commentNumber = commentNumber.substring(0, commentNumber.indexOf(" "));
            int commentCount = Integer.parseInt(commentNumber); 
            		
            //System.out.println("Comments Number:" + commentCount);
            
            //DomNodeList<HtmlElement> commentNodes = div.getElementsByTagName("div");
            //int childNodes = div.getChildElementCount();
            
            /*for(HtmlElement element: commentNodes){
            	List<?> childElements = element.getByXPath("//*[@class=\"pluck-comm-single-comment-top\"]");
            }*/
            List<DomNode> commentNodes= (List<DomNode>)div.getByXPath("//div[contains(@class, 'pluck-comm-single-comment-top')]");
            for(DomNode node : commentNodes){
            	List<DomNode> tempNodes = (List<DomNode>)  node.getByXPath(".//h4[contains(@class, 'pluck-comm-username-url pluck-comm-username-display')]");
            	String hasReply = "";
            	if(tempNodes.size() > 0){
            		String commentUserName = tempNodes.get(0).asText();
            		
            		tempNodes = (List<DomNode>)  node.getByXPath(".//span[contains(@class, 'pluck-score-downvotes-display')]");
            		String downVotes = tempNodes.get(0).asText();
            		
            		NamedNodeMap attributes = node.getAttributes();
            		
            		if(attributes.item(attributes.getLength() - 1).toString().contains("pluck-comm-hasReplies")){
            			//System.out.println("Got it:" + attributes.item(attributes.getLength() - 1));
            			if(tempNodes.size() > 0)hasReply = "has replies";
            		}
            		
            		//if(tempNodes.size() > 0)hasReply = "has replies";
            		
            		/*tempNodes = (List<DomNode>)  node.getByXPath(".//div[contains(@class, 'pluck-comm-show-hide-replies')]");
            		if(tempNodes.size() > 0)hasReply = "has Reply";*/
            		
            		tempNodes = (List<DomNode>)  node.getByXPath(".//span[contains(@class, 'pluck-score-upvotes-display')]");
            		String upVotes = tempNodes.get(0).asText();
            		
            		tempNodes = (List<DomNode>)  node.getByXPath(".//p[contains(@class, 'pluck-comm-timestamp')]");
                	String commentTimeStamp = tempNodes.get(0).asText();
                	
                	tempNodes = (List<DomNode>)  node.getByXPath(".//p[contains(@class, 'pluck-comm-body')]");
                	String commentBody = tempNodes.get(0).asText();
                	
                	System.out.println("UserName:" + commentUserName + ", TimeStamp:" + commentTimeStamp + ", UpVote:" + upVotes + ", DownVote:" + downVotes + ", " + hasReply);
                	
                	System.out.println("---------------------------------------------------------------------------------------");
            	}
            }
            
            HtmlAnchor a = page.getAnchorByHref("#");
    		HtmlPage page3 = a.click();
    		System.out.println(page3.getWebResponse().getContentAsString());
          //*[@id="pluck_comments_list_54160346"]/div[2]
            
            //div[contains(@class, 'header')]
            System.out.println("Comments Nodes:" + commentNodes.size());
            
            
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

}
