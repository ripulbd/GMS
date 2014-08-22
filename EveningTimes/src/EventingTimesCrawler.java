import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


public class EventingTimesCrawler {
	//static String currentURL = "http://www.eveningtimes.co.uk/news/students-probed-over-drugs-fraud-and-hoax-177027n.25096799";
	//static String currentURL = "http://www.eveningtimes.co.uk/celtic/celtic-hero-callum-has-parkhead-euro-goal-177083n.25104619";
	static String currentURL = "http://www.eveningtimes.co.uk/news/u/michelle-mone-banned-for-six-months-after-speeding-on-m77.1408708353";
	static String outputFolder = "/home/ripul/images/et/";
	
	public static void main(String[] args) {
		Document doc = null;
		
		/**
		 * The following is to retrieve all news from the homepage of the ET
		 */
		
		/*String masterURL = "http://www.eveningtimes.co.uk/news";
		
		try {
			doc = Jsoup.connect(masterURL).get();
			Elements homeNewsElements = doc.select("li.views-row");
			for(Element elem : homeNewsElements){
				Element anchorElement = elem.select("div.views-field-title").first();
				String title = anchorElement.select("a").first().text();
				String URL = "http://www.eveningtimes.co.uk" + anchorElement.select("a").first().attr("href");
				System.out.println("Title:" + title + ", URL:" + URL);
				if(elem.hasClass("views-row-last"))break;
				//break;
				//<span class="field-content"><a href="/news/merchant-city-make-over-177192n.25110018">Merchant City make-over</a></span>
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
        /**
         * The following code can retrieve a single news and all its related info excluding comments...
         */
		
		System.out.println("Current URL:" + currentURL);
        try {
			/*doc = Jsoup.connect(currentURL).get();
			
			Element titleElement = doc.select("h1.article-title").first();
			String title = titleElement.text();
			Element descriptionElement = doc.select("h2.article-abstract").first();
			String description = descriptionElement.text();
			Element dateElement = doc.select("div.article-date").first();
			String date = dateElement.text();
			date = date.substring(date.lastIndexOf(" ") + 1);
			
			Element imageElement = doc.select("div.article-image").first();
			Element image = imageElement.getElementsByTag("img").first();
			String caption = image.attr("title");
			String imageURL = image.attr("src");
			String imageName = imageURL.substring(imageURL.lastIndexOf("/") + 1);
			String imageNameAndLoc = outputFolder + imageName;
			
			boolean imageFlag = false;
	        Response resultImageResponse = null;
			try {
				resultImageResponse = Jsoup.connect(imageURL).ignoreContentType(true).execute();
				
				imageFlag = true;
				saveImage(resultImageResponse, imageName, caption);
			} catch (HttpStatusException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements mainStoryElements = doc.select("div.article-content").first().getElementsByTag("p");
			String mainStory = "";
			for(Element elem : mainStoryElements){
				if(elem.tagName().equals("p")){
					if(elem.html().length() > 0)
					mainStory += "<p>" + elem.html() + "</p>";
				}
			}
			mainStory = "<p>" + description + "</p>" + mainStory;
			System.out.println("Title:" + title + ", Description:" + description + ", Date:" + date);
			System.out.println("Main Story:===================\n" + mainStory);
			System.out.println("Image Caption:" + caption + ", Image URL:" + imageURL + ", Image Name:" + imageName + ", Image Name & Loc:" + imageNameAndLoc);
			*/
			
			
			//Code for comments, not yet working.....
			
        	//http://disqus.com/embed/comments/?base=default&disqus_version=ede5214f&f=eveningtimes&t_i=http://www.eveningtimes.co.uk/celtic/u/celtic-in-talks-over-serbian-strike-ace.1408617778&t_u=http://www.eveningtimes.co.uk/celtic/u/celtic-in-talks-over-serbian-strike-ace.1408617778
	        String commentURL = "http://disqus.com/embed/comments/?base=default&disqus_version=ede5214f&f=eveningtimes&t_i=" + currentURL + "&t_u=" + currentURL;
	        
        	//String commentURL = "https://disqus.com/api/3.0/threads/listPosts.json?api_key=E8Uh5l5fHZ6gD8U3KycjAIAk46f68Zw7C6eW8WSjZvCLXebZ7p0r1yrYDrLilk2F&thread=2945625675";
        	
        	//doc = Jsoup.connect(commentURL).get();
        	
        	
        	System.out.println("Comment URL:" + commentURL);
			java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
	        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
	        
	        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
	        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
	        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
	        
			WebClient webClient = new WebClient();
	        webClient.getOptions().setThrowExceptionOnScriptError(false);
	        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	        webClient.getOptions().setJavaScriptEnabled(true);
	        webClient.getOptions().setCssEnabled(true);
	        webClient.getOptions().setActiveXNative(true);
	        webClient.setJavaScriptTimeout(200000);
	        webClient.getOptions().setTimeout(200000);
	        
	        Page page = webClient.getPage(commentURL);
	        System.out.println("The whole page:=======================================");
	        
	        String wholePage = page.getWebResponse().getContentAsString();
	        wholePage = wholePage.substring(wholePage.indexOf("\"thread\":"));
	        String thread = wholePage.substring(wholePage.indexOf("\":\"") + 3, wholePage.indexOf("\",\""));
	        
	        commentURL = "https://disqus.com/api/3.0/threads/listPosts.json?api_key=E8Uh5l5fHZ6gD8U3KycjAIAk46f68Zw7C6eW8WSjZvCLXebZ7p0r1yrYDrLilk2F&thread=" + thread;
	        page = webClient.getPage(commentURL);
	        
	        String commentResponse = page.getWebResponse().getContentAsString();
	        
	        WebResponse response = page.getWebResponse();
	        if (response.getContentType().equals("application/json")) {
	            String json = response.getContentAsString();
	            System.out.println("JSON Response:" + json);
	            
	            ArrayList<HashMap<String, String>> commentList = new ArrayList<HashMap<String, String>>();
	            ArrayList<HashMap<String, String>> tempCommentList = new ArrayList<HashMap<String, String>>();
	            
	            JsonParser parser = new JsonParser();
	            JsonObject obj = parser.parse(json).getAsJsonObject();
	            //JsonArray array = parser.fromJson(obj.get("response")).;
	            for (Entry<String, JsonElement> entry : obj.entrySet()) {
	            	String key = entry.getKey();
	                JsonElement value = entry.getValue();
	                if(key.equals("response")){
	                	if(value.isJsonArray()){
	                		System.out.println("Found JSON Array!");
	                		for(JsonElement elem : value.getAsJsonArray()){
	                			JsonObject tempObj = elem.getAsJsonObject();
	                			String upVote = tempObj.get("likes").getAsString();
	                			String totalVote = tempObj.get("points").getAsString();
	                			String downVote = "" + (Integer.parseInt(totalVote) - Integer.parseInt(upVote));
	                			String timeStamp = tempObj.get("createdAt").getAsString();
	                			String mainComment = tempObj.get("raw_message").getAsString();
	                			String parent = "";
	                			if(!tempObj.get("parent").isJsonNull())parent = tempObj.get("parent").getAsString();
	                			String id = tempObj.get("id").getAsString();
	                			
	                			JsonObject authorObject = tempObj.get("author").getAsJsonObject();
	                			String name = authorObject.get("name").getAsString();
	                			
	                			
	                			
	                			HashMap<String, String> tmpMap = new HashMap<String, String>();
	                			tmpMap.put("upVote", upVote);
	                			tmpMap.put("downVote", downVote);
	                			tmpMap.put("commentBody", mainComment);
	                			tmpMap.put("timeStamp", timeStamp);
	                			tmpMap.put("userName", name);
	                			tmpMap.put("id", id);
	                			tmpMap.put("parent", parent);
	                			if(parent.length() > 2)tempCommentList.add(tmpMap);
	                			commentList.add(tmpMap);	                			
	                			System.out.println("UpVote:" + upVote + ", DownVote:" + downVote + ", User Name:" + name +", Comment Body:" + mainComment + ", Time Stamp:" + timeStamp + ", id:" + id + ", Parent:" + parent);
	                		}
	                	}
	                }
	                
	                ArrayList<CommentDocument> commList = new ArrayList<CommentDocument>();
	                
	                for(HashMap<String, String> tmpMap : commentList){
	                	String userName = tmpMap.get("userName");
	                	String timeStamp = tmpMap.get("timeStamp");
	                	String upVote = tmpMap.get("upVote");
	                	String downVote = tmpMap.get("downVote");
	                	String commentBody = tmpMap.get("commentBody");
	                	String id = tmpMap.get("id");
	                	
	                	CommentDocument tempDoc = new CommentDocument();
	                	
	                	tempDoc.setCommentBody(commentBody);
	                	tempDoc.setUpVote(Integer.parseInt(upVote));
	                	tempDoc.setDownVote(Integer.parseInt(downVote));
	                	tempDoc.setTimeStamp(timeStamp);
	                	tempDoc.setUserName(userName);
	                	ArrayList<CommentDocument> replyList = retrieveCommentFromId(id, tempCommentList);
	                	if(replyList.size() > 0){
	                		tempDoc.setReplies(replyList);
	                	}
	                	commList.add(tempDoc);
	                }
	                
	                for(CommentDocument tempDoc : commList){
	                	System.out.println(tempDoc);
	                }
	                //System.out.println("Key:" + key);
	            }
	        }
	        
	        
	        
	        
	        /*FileOutputStream out;
			try {
				out = (new FileOutputStream(new java.io.File("temp.xml")));
				out.write(page.asXml().getBytes());
		        out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	        
			/*List<?> divs =page.getByXPath("//div[@class=\"article-comments\"]");
	        HtmlDivision div = (HtmlDivision) divs.get(0);
	        String commentNumText = div.asText();
	        commentNumText = commentNumText.substring(0, commentNumText.indexOf(" "));
	        int commentNumber = Integer.parseInt(commentNumText);
	        System.out.println("Comment Number:" + commentNumText);*/
	        
	        /*List<?> divs =page.getByXPath("//div[@class=\"article-comments\"]");
	        HtmlDivision div = (HtmlDivision) divs.get(0);
	        String commentNumText = div.asText();
	        commentNumText = commentNumText.substring(0, commentNumText.indexOf(" "));
	        int commentNumber = Integer.parseInt(commentNumText);
	        System.out.println("Comment Number:" + commentNumText);
	        if(commentNumber > 0){
	        	System.out.println("Now retrieving comments!");
	        	List<?> postDivs =page.getByXPath("//*[@class=\"post\"]");
	        	System.out.println("Post Divs Size:" + postDivs.size());
	        	for(int i = 0; i < postDivs.size(); i++){
	        		HtmlListItem eachComment = (HtmlListItem) postDivs.get(i);
	        		HtmlAnchor userNameAnchor = (HtmlAnchor) eachComment.getByXPath(".//a[@data-role=\"username\"]").get(0);
	        		System.out.println("Username:" + userNameAnchor.asText());
	        	}
	        }*/
	        
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static private ArrayList<CommentDocument> retrieveCommentFromId(String id, ArrayList<HashMap<String, String>> tempCommentList){
		
		ArrayList<CommentDocument> replyList = new ArrayList<CommentDocument>();
		
		
		
		for(HashMap<String, String> tmpMap: tempCommentList){
			String parent = tmpMap.get("parent");
			if(parent.length() > 2 && parent.equals(id)){
				CommentDocument tempDoc = new CommentDocument();
				String userName = tmpMap.get("userName");
            	String timeStamp = tmpMap.get("timeStamp");
            	String upVote = tmpMap.get("upVote");
            	String downVote = tmpMap.get("downVote");
            	String commentBody = tmpMap.get("commentBody");
            	
            	tempDoc.setCommentBody(commentBody);
            	tempDoc.setUpVote(Integer.parseInt(upVote));
            	tempDoc.setDownVote(Integer.parseInt(downVote));
            	tempDoc.setTimeStamp(timeStamp);
            	tempDoc.setUserName(userName);
            	replyList.add(tempDoc);
			}
		}
		return replyList;
	}
	
	static private boolean saveImage(Response resultImageResponse, String imageName, String caption){
		boolean flag = false;
        FileOutputStream out;
		try {
			out = (new FileOutputStream(new java.io.File(outputFolder + imageName)));
			out.write(resultImageResponse.bodyAsBytes());
	        out.close();
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
}
