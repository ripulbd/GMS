package dailyr;
import java.io.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class links {

	public static void main(String[] args) throws IOException {
		String title;
        String description;
        String date;
        String story;
        String [] capsarr;
        ArrayList all_links = new ArrayList();
        all_links.clear();
		
		// page 1
    
       
        URL url = new URL ("http://www.dailyrecord.co.uk/all-about/glasgow?all=true");
		 //Document doc = Jsoup.connect("http://www.dailyrecord.co.uk/all-about/glasgow?pageNumber=2&all=true").get();
		 Document doc = Jsoup.parse(url,3000);
		// System.out.println(doc.select("a[href]"));
	       
       //System.out.println(doc);
    
   // get all the links
		 
    Elements links1 = doc.getElementsByTag("h2");
    Elements link2 = links1.select("a");
      
       for (Element i: link2)
       {
           //all_links.clear();
    	   String link11= i.attr("href");
           all_links.add (link11);
          //System.out.println(link11);
        // getinfo (all_links);
         
       }
       
    
       
   
        // page 2
       
    URL url2 = new URL ("http://www.dailyrecord.co.uk/all-about/glasgow?pageNumber=2&all=true");
	Document doc2 = Jsoup.parse(url2,3000);
		// System.out.println(doc.select("a[href]"));
	       
     //System.out.println(doc);
  
 // get all the links for page 1
		 
  Elements links2 = doc2.getElementsByTag("h2");
  Elements link3 = links2.select("a");
    
     for (Element i1: link3)
     {
         //all_links.clear();
  	   String link12= i1.attr("href");
         all_links.add (link12);
         //System.out.println(link11);
           
     }
         
   
     
     // page 2
     
    URL url3 = new URL ("http://www.dailyrecord.co.uk/all-about/glasgow?pageNumber=3&all=true");
	Document doc3 = Jsoup.parse(url3,3000);
		// System.out.println(doc.select("a[href]"));
	       
    //System.out.println(doc);
 
		 
 Elements links3 = doc3.getElementsByTag("h2");
 Elements link4 = links3.select("a");
   
    for (Element i: link4)
    {
        //all_links.clear();
 	   String link13= i.attr("href");
        all_links.add (link13);
        //System.out.println(link11);
        
    }
        
      //checking array list
        int n = all_links.size();
           for(int i1 = 0; i1 < n ; i1++) {
            //System.out.println( all_links.get( i1 ) );
       }
	

           getinfo(all_links);
	}



/*
URL url1 = new URL(link11);
Document doc1 = Jsoup.parse(url1,3000);
String img1 = doc1.select("div.image-credit-wrapper").select("img").attr("src");
System.out.println("image is" +img1);
Elements cap1= doc1.getElementsByClass("inline-image");
Elements cap2 = cap1.select("figcaption");
                 
System.out.println("caption is:" +cap2.text());

*/


	public static void getinfo (ArrayList list) throws IOException{
	
	int n = list.size();
    for(int i = 0; i < n ; i++) {
    	
    	String link = list.get(i).toString();
    	Document doc1 = Jsoup.connect(link).get();
    	//System.out.println(link);
              
	// get date 
    	
    	String date = doc1.select("meta[property=article:published_time").attr("content");
		int pos=date.indexOf("T");
		date= date.substring(0, pos);
		System.out.println(date);
    	
    	//get title 
    	
    	
    	 Elements t= doc1.select("h1[class=entry-title mbn]");
         String title = t.text();
         //System.out.println (" \ntitle is:" + title);
    	
    	

        
        // get description for the title
      
        Elements a12= doc1.select("meta[name=description]");
        String description = a12.attr("content");
        //System.out.println (" \ndescription is:" +description);

     // get the complete news story
        
        Elements b1= doc1.select("div.body");
        String story=b1.text();
       // System.out.println("story is:" +story);
        
		
	// get all images
		
        Elements img1 = doc1.select("figure.inline-image");
		
		for (Element im:img1){
		String image_name= im.select("img").attr("src");
			
	//System.out.println("name is" +image_name);
		
		int indexname = image_name.lastIndexOf("/");
          if (indexname == image_name.length()){
            image_name= image_name.substring(1, indexname);
        }
         indexname = image_name.lastIndexOf("/");
         String name1= image_name.substring(indexname+1,image_name.length());
         System.out.println(name1);
         		
         String caption= im.select("figcaption").text();
         //int pos = caption.lastIndexOf("STV");
		 // caption= caption.substring(0, pos-1);
          System.out.println("caption is:" +caption);
		
          URL url = new URL(link);
          InputStream in = url.openStream();
          OutputStream out = new BufferedOutputStream(new FileOutputStream( name1));

          for (int b; (b = in.read()) != -1;) {
              out.write(b);
          }
          out.close();
          in.close();
		
		
		} 
	     
		
		/*

		}
          
     */
        
 //   } //

    
    
}

}
	}

           
  
       
 
