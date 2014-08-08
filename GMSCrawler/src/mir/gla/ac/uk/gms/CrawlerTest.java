package mir.gla.ac.uk.gms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CrawlerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ScotsmanCrawler scotsCrawler = new ScotsmanCrawler("http://www.scotsman.com/news/scotland/glasgow-west");
		
		try {
			ArrayList<HashMap<String, String>> urlInfos = scotsCrawler.crawlURLs();
			GMSNewsDocument scotDoc = scotsCrawler.crawlNews(urlInfos.get(0));
			System.out.println(scotDoc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
