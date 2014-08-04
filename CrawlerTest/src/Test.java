import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Document doc = null;
        try {
        	System.out.println("Fetching data....");
            doc = Jsoup.connect("http://www.scotsman.com.dynamic.feedsportal.com/pf/610141/www.scotsman.com/rss/cmlink/1.3009129").get();
            //System.out.println("Top Link:" + doc.select("link").outerHtml());
            for (Element e1 : doc.select("item")) {
            	String title = e1.select("title").text();
            	String wholeElement = e1.toString();
            	//String URL = e1.select("link").html();
            	
            	String URL = wholeElement.substring(wholeElement.indexOf("<link />") + 8, wholeElement.indexOf("<description>"));
            	
            	String description = e1.select("description").text();
            	description = Jsoup.parse(description.substring(description.indexOf("<p>") + 3, description.indexOf("</p>"))).text();
            	String date = e1.select("pubDate").text();
            	System.out.println("Title:" + title + ", URL:" + URL + ", Description:" + description + ", date:" + date +"\n");
            }
        }catch (IOException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
        System.out.println("Fetching finished....");
	}

}
