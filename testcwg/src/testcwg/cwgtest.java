package testcwg;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class cwgtest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		URL url = new URL ("http://www.bbc.co.uk/sport/0/commonwealth-games/28632068");
		Document doc = Jsoup.parse(url, 30000); 
		
		//Document doc = Jsoup.connect("http://www.dailyrecord.co.uk/lifestyle/family-relationships/walked-down-aisle-boots-reception-3940668").get();
		//System.out.println(doc.html());
		
		// get story 
		
		String story= doc.select("div.article").select("p").outerHtml();
		//System.out.println(story);
		
	// get related links 
		
		Elements in_link = doc.select("div[id=also-related-links").select("li");
		for (Element i:in_link.select("a"))
		{
			String link= i.attr("abs:href");
			if (link.contains("commonwealth-games")) 
			{
				System.out.println(link);
			}
			
				}
			
		// get images
		
		Elements img1 = doc.select("figure.inline-image");
		
		for (Element im:img1){
		String image_name= im.select("img").attr("src");
			
		System.out.println("name is" +image_name);
		
		int indexname = image_name.lastIndexOf("/");
          if (indexname == image_name.length()){
            image_name= image_name.substring(1, indexname);
        }
         indexname = image_name.lastIndexOf("/");
         String name1= image_name.substring(indexname+1,image_name.length());
       
        
         //Open a URL Stream
         org.jsoup.Connection.Response resultImageResponse = Jsoup.connect(image_name).ignoreContentType(true).execute();

         // output here
         FileOutputStream out = (new FileOutputStream(new java.io.File(name1)));
         out.write(resultImageResponse.bodyAsBytes());           // resultImageResponse.body() is where the image's contents are.
         out.close();
	
	
	}

}
