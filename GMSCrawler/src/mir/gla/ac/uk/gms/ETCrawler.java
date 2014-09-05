package mir.gla.ac.uk.gms;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ETCrawler extends AbstractCrawler {
	
	private String outputFolder = "/home/ripul/images/et/"; 	//where the images will be stored...
	private DBUtils dbUtils;
	private String masterURL;
	private WebClient webClient;
	public ETCrawler(String masterURL) {
		// TODO Auto-generated constructor stub
		super(masterURL);
		this.masterURL = masterURL;
		dbUtils = new DBUtils();
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

	@Override
	public ArrayList<HashMap<String, String>> crawlURLs() throws IOException {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, String>> urlInfos = new ArrayList<HashMap<String, String>>();
		Document doc = null;
		
		doc = Jsoup.connect(masterURL).get();
		Elements homeNewsElements = doc.select("li.views-row");
		for(Element elem : homeNewsElements){
			Element anchorElement = elem.select("div.views-field-title").first();
			String title = anchorElement.select("a").first().text();
			String URL = "http://www.eveningtimes.co.uk" + anchorElement.select("a").first().attr("href");
			//System.out.println("Title:" + title + ", URL:" + URL);
			
			if(!dbUtils.find("url", URL)){
				HashMap<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("title", title);
				tmpMap.put("url", URL);
				urlInfos.add(tmpMap);
			}
			
			if(elem.hasClass("views-row-last"))break;
		}
		
		return urlInfos;
	}

	@Override
	public GMSDocument crawlNews(HashMap<String, String> info) throws IOException {
		// TODO Auto-generated method stub
		String URL = info.get("url");
		String title = info.get("title");
		
		GMSNewsDocument etNews = new GMSNewsDocument(URL);
		
		Document doc = null;
		doc = Jsoup.connect(URL).get();
		
		Element descriptionElement = doc.select("h2.article-abstract").first();
		String description = descriptionElement.text();
		Element dateElement = doc.select("div.article-date").first();
		String date = dateElement.text();
		date = date.substring(date.lastIndexOf(" ") + 1);
		
		Elements mainStoryElements = doc.select("div.article-content").first().getElementsByTag("p");
		String mainStory = "";
		for(Element elem : mainStoryElements){
			if(elem.tagName().equals("p")){
				if(elem.html().length() > 0)
				mainStory += "<p>" + elem.html() + "</p>";
			}
		}
		mainStory = "<p>" + description + "</p>" + mainStory;
		
		etNews.setTitle(title);
		etNews.setCategory("news");
		etNews.setSource("Evening Times");
		etNews.setDescription(description);
		etNews.setDate(date);
		etNews.setMainStory(mainStory);
		ArrayList<HashMap<String, String>> imageNameCaption = null;
		try {		
			imageNameCaption = retrieveImageNameCaption(doc);
		}catch(HttpStatusException e){
			
		} catch (IOException e){
			
		}
		String commentNumText = "Loading";
		int loopCounter = 0;
		if(imageNameCaption != null)etNews.setImageNameCaption(imageNameCaption);
		while(commentNumText.equals("Loading")){
			HtmlPage page = webClient.getPage(URL);
			
			List<?> divs =page.getByXPath("//div[@class=\"article-comments\"]");
	        HtmlDivision div = (HtmlDivision) divs.get(0);
	        commentNumText = div.asText();
	        commentNumText = commentNumText.substring(0, commentNumText.indexOf(" "));
	        System.out.println("CommentNumText:" + commentNumText);
	        if(commentNumText.equals("Loading")){
	        	if(loopCounter == 5)break;
	        	System.out.println("Found Loading, trying out again!");
	        	/**
				 * The following code implements the politeness policy. It pauses for 10 seconds
				 * after crawling each URL as per the policy of the robots.txt
				 */
				try {
				    Thread.sleep(10000);                 
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
	        }
		}
		
		int commentNumber = Integer.parseInt(commentNumText);
        System.out.println("Comment Number:" + commentNumText);
        if(commentNumber > 0){
        	processComment(URL, etNews);
        }
		
		return etNews;
	}
	
	private void processComment(String URL, GMSNewsDocument etNews) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		String commentURL = "http://disqus.com/embed/comments/?base=default&disqus_version=ede5214f&f=eveningtimes&t_i=" + URL + "&t_u=" + URL;
		Page page = webClient.getPage(commentURL);
        System.out.println("The whole page:=======================================");
        
        String threadPage = page.getWebResponse().getContentAsString();
        threadPage = threadPage.substring(threadPage.indexOf("\"thread\":"));
        String thread = threadPage.substring(threadPage.indexOf("\":\"") + 3, threadPage.indexOf("\",\""));
        
        commentURL = "https://disqus.com/api/3.0/threads/listPosts.json?api_key=E8Uh5l5fHZ6gD8U3KycjAIAk46f68Zw7C6eW8WSjZvCLXebZ7p0r1yrYDrLilk2F&thread=" + thread;
        page = webClient.getPage(commentURL);
        
        String commentResponse = page.getWebResponse().getContentAsString();
        
        WebResponse response = page.getWebResponse();
        if (response.getContentType().equals("application/json")) {
            String json = response.getContentAsString();
            //System.out.println("JSON Response" + json);
            
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
                		//System.out.println("Found JSON Array!");
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
                			//System.out.println("UpVote:" + upVote + ", DownVote:" + downVote + ", User Name:" + name +", Comment Body:" + mainComment + ", Time Stamp:" + timeStamp + ", id:" + id + ", Parent:" + parent);
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
                
                /*for(CommentDocument tempDoc : commList){
                	System.out.println(tempDoc);
                }*/
                etNews.setComments(commList);
            }
        }
	}
	
private ArrayList<CommentDocument> retrieveCommentFromId(String id, ArrayList<HashMap<String, String>> tempCommentList){
		
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

	private ArrayList<HashMap<String, String>> retrieveImageNameCaption(Document doc) throws HttpStatusException, IOException{
		ArrayList<HashMap<String, String>> imageList = new ArrayList<HashMap<String, String>>();
		
		Element imageElement = doc.select("div.article-image").first();
		if(imageElement != null){
			Element image = imageElement.getElementsByTag("img").first();
			String caption = image.attr("title");
			String imageURL = image.attr("src");
			String imageName = "et_" + imageURL.substring(imageURL.lastIndexOf("/") + 1);
			
	        Response resultImageResponse = null;
			resultImageResponse = Jsoup.connect(imageURL).ignoreContentType(true).execute();
			if(saveImage(resultImageResponse, imageName, caption)){
				HashMap<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("name", imageName);
				tmpMap.put("caption", caption);
				imageList.add(tmpMap);
				return imageList;
			} else return null;
		}
		return null;
	}

	private boolean saveImage(Response resultImageResponse, String imageName, String caption){
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
	
	public void store(GMSNewsDocument doc) {
		// TODO Auto-generated method stub
		if(!dbUtils.find("url", doc.getTitle()))dbUtils.addDocument(doc);
		else {
			System.out.println("The news is already in the database.");
		}
	}

}
