package test;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class testing {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		
		URL url = new URL ("http://news.stv.tv/west-central/284720-haggis-and-shortbread-sales-in-scotland-soar-over-commonwealths/");
		//Document doc = Jsoup.parse(url, 30000); 
		Document doc = Jsoup.connect("http://news.stv.tv/west-central/284725-sierra-leone-cyclist-mohamed-tholley-missing-after-ebola-scare/").get();
		//System.out.println(doc.html());
		
		// get first image
		
		Elements img1 = doc.select("div.entry-media");
		
		for (Element im:img1){
		String image_name= im.select("img").attr("src");
		System.out.println("name is" +image_name);
		/*
		int indexname = image_name.lastIndexOf("/");
          if (indexname == image_name.length()){
            image_name= image_name.substring(1, indexname);
        }
         indexname = image_name.lastIndexOf("/");
         String name1= image_name.substring(indexname+1,image_name.length());
         System.out.println(name1);
         */
		
         String caption= im.select("figcaption").text();
         //int pos = caption.lastIndexOf("STV");
		 // caption= caption.substring(0, pos-1);
          System.out.println("caption is:" +caption);
		}
          
        /*  
		String [] arrayimg;
          int count_images =0;
          Elements img12 = doc.select("div.esp-body");
                    
          for (org.jsoup.nodes.Element e1:img1){
      		
        	  String image_name1= img1.select("img").attr("src");
      		System.out.println("name is" +image_name1);
      		  
      		}
      		
		
/*

				
		
		
		//for (org.jsoup.nodes.Element e1:img1.select("img")){
         //String image_name= e1.attr("src");
		   // String caption= e1.select("figcaption").text();
         //           System.out.println("name is" +image_name);
          // System.out.println("caption is" +caption);
		
           
		}//
		
		
	           
			
		  
		 
          
		
            /*
            URL url2 = new URL (name);
            InputStream in = url2.openStream();
            OutputStream out = new BufferedOutputStream (new FileOutputStream(name1));
            for (int b; (b = in.read()) != -1;){
                out.write(b);
            }
            
            //document.put("image:", name1);
            out.close();
            in.close();
            System.out.println(name1);
	}
	*/

	}
}

