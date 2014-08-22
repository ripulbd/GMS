package mir.gla.ac.uk.gms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.HttpStatusException;

/**
 * A basic testing class for all crawlers...
 * 
 * @author 	Md. Sadek Ferdous
 * @version 1.0
 * @since 	11/08/2014
 *
 */
public class ETCrawlerTest {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ETCrawler etCrawler = new ETCrawler("http://www.eveningtimes.co.uk/news");
		
		try {
			ArrayList<HashMap<String, String>> urlInfos = etCrawler.crawlURLs();
			System.out.println("Total News Fetched:" + urlInfos.size());
			int pauseCount = 0;
			for(HashMap<String, String> tmpHashMap : urlInfos){
				System.out.println("Added URL:" + tmpHashMap.get("url"));
				GMSNewsDocument etDoc = (GMSNewsDocument) etCrawler.crawlNews(tmpHashMap);
				System.out.println(etDoc);
				etCrawler.store(etDoc);
				pauseCount++;
				/**
				 * The following code implements the politeness policy. It pauses for 10 seconds
				 * after crawling each URL as per the policy of the robots.txt
				 */
				try {
				    Thread.sleep(10000);                 
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
				/*if(pauseCount % 10 == 0){
					try {
					    Thread.sleep(20000);                 
					} catch(InterruptedException ex) {
					    Thread.currentThread().interrupt();
					}
				}*/
			}
		} catch (HttpStatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
