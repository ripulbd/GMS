package cwg;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class first {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		URL url = new URL ("http://www.bbc.co.uk/sport/commonwealth-games/2014");
		//Document doc = Jsoup.parse(url, 30000); 
		Document doc = Jsoup.connect("http://www.bbc.co.uk/sport/commonwealth-games/2014").get();
		//System.out.println(doc.select("a[href]"));
		//System.out.println(doc.select("div.article__media").select("a[href]"));
		Elements e = doc.select("a.article__read-more");
		
				for (Element i: e)
		{
					
					System.out.println(i.attr("abs:href"));
		}
	
	}

}
