package mir.gla.ac.uk.gms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A basic testing class for all crawlers...
 * 
 * @author 	Md. Sadek Ferdous
 * @version 1.0
 * @since 	11/08/2014
 *
 */
public class CrawlerTest {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ScotsmanCrawler scotsCrawler = new ScotsmanCrawler("http://www.scotsman.com/news/scotland/glasgow-west");
		ArrayList<GMSNewsDocument> listOfDoc = new ArrayList<GMSNewsDocument>();
		
		//video news: http://www.scotsman.com/what-s-on/music/piping-festival-aims-to-use-glasgow-2014-momentum-1-3502847
		//Tow image news:http://www.scotsman.com/lifestyle/heritage/glasgow-to-recognise-slave-trade-links-1-3497713
		//No comments news: http://www.scotsman.com/what-s-on/music/piping-festival-aims-to-use-glasgow-2014-momentum-1-3502847
		try {
			ArrayList<HashMap<String, String>> urlInfos = scotsCrawler.crawlURLs();
			//ArrayList<HashMap<String, String>> urlInfos = new ArrayList<HashMap<String, String>>();
			/*HashMap<String, String> tmpHashMap = new HashMap<String, String>();
			tmpHashMap.put("url", "http://www.scotsman.com/news/politics/top-stories/glasgow-2014-alex-salmond-hails-gallus-games-1-3497971");
			tmpHashMap.put("timeStamp", "9 Aug 2014 17:32:55");
			tmpHashMap.put("description", "Dummy Description....");
			urlInfos.add(tmpHashMap);*/
			int count = 0;
			System.out.println("Total News Fetched:" + urlInfos.size());
			
			for(HashMap<String, String> tmpHashMap : urlInfos){
				GMSNewsDocument scotDoc = scotsCrawler.crawlNews(tmpHashMap);
				//System.out.println(scotDoc);
				listOfDoc.add(scotDoc);
				scotsCrawler.store();
				//count++;
				//if(count == 2)break;
				/*System.out.println("Title:" + tmpHashMap.get("title"));
				System.out.println("URL:" + tmpHashMap.get("url"));
				System.out.println("Time Stamp:" + tmpHashMap.get("timeStamp"));
				System.out.println("Description:" + tmpHashMap.get("description"));*/
			}
			
			System.out.println("Finished fethcing and storing all news from the homepage, now fetching news from all related stories:"); 
			for(GMSNewsDocument tmpScotDoc : listOfDoc){
				ArrayList<HashMap<String, String>> tmpRelatedStories = tmpScotDoc.getRelatedStories();
				if(tmpRelatedStories != null && tmpRelatedStories.size() > 0){
					for(HashMap<String, String> tmpHashMap : tmpScotDoc.getRelatedStories()){
						String tmpTitle = tmpHashMap.get("title");
						String tmpURL = tmpHashMap.get("url");
						if(tmpURL.contains("youtube.com"))continue;
						ArrayList<HashMap<String, String>> tmpList = scotsCrawler.crawlRelatedStory(tmpTitle, tmpURL);
						if(tmpList != null){
							GMSNewsDocument scotDoc = scotsCrawler.crawlNews(tmpHashMap);
							//System.out.println(scotDoc);
							listOfDoc.add(scotDoc);
							scotsCrawler.store();
						}
					}
				}
				
			}
			//GMSNewsDocument scotDoc = scotsCrawler.crawlNews(urlInfos.get(0));
			
			//System.out.println(scotDoc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
