package mir.gla.ac.uk.gms;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.LogFactory;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NamedNodeMap;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;


public class First_gud {

	public static ArrayList<HashMap<String, String>> urlInfos;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
	 WebClient webClient;
	 HtmlAnchor anchor1;
	//String masterURL = "http://discussion.theguardian.com/discussion/p/4xnz9?orderby=oldest&commentpage=1&per_page=50";
	
	 	Queue<String> urlQueue;
		urlQueue = new LinkedList<String>();
		//urlQueue.add(masterURL);
		
		webClient = new WebClient();
		webClient.getOptions().setThrowExceptionOnScriptError(false);
	    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	    webClient.getOptions().setJavaScriptEnabled(true);
	    webClient.getOptions().setCssEnabled(true);
	    webClient.getOptions().setActiveXNative(true);
	    webClient.setJavaScriptTimeout(200000);
	    webClient.getOptions().setTimeout(200000);
	    webClient.setAjaxController(new NicelyResynchronizingAjaxController());
	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
	

        	for(int i = 1; i <= 4; i++){
        		String masterURL = "http://discussion.theguardian.com/discussion/p/4xnz9?orderby=oldest&commentpage=" + i + "&per_page=50&noposting=true&json=true&tab=all&forcetab=true";
        		//http://discussion.theguardian.com/discussion/p/4xnz9?orderby=oldest&per_page=50&commentpage=1&noposting=true&json=true&tab=all&forcetab=true
        		Page page =  webClient.getPage(new java.net.URL(masterURL));
        		File file = new File("filename" + i + ".txt");
        		System.out.println("page=" + page.getWebResponse().getContentAsString()); 
    			// if file doesnt exists, then create it
    			if (!file.exists()) {
    				file.createNewFile();
    			}
     
    			FileWriter fw = new FileWriter(file.getAbsoluteFile());
    			BufferedWriter bw = new BufferedWriter(fw);
    			bw.write(page.getWebResponse().getContentAsString());
    			bw.close();
    			try {
				    Thread.sleep(5000);                 
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
        	}
           	
	        //System.out.println("page=" + page);
	    	
           	/*List<HtmlAnchor> anchors = page.getAnchors();
	    	//System.out.println("anchor text is" +anchors.size());
	    	       	    
	    		   int counter=0;
	    	       for (HtmlAnchor anchor:anchors ){
	    	        	
	    	        	String anchor2= anchor.asText();
	    	        	//i++;
	    	        	 //System.out.println(anchor2);
	    	        	 
	    	        	if (anchor2.contains("Next")) 
	    	        	{
	    	        		    break;
	    	        	}
	    	        	//System.out.println(anchor2);
	    	        	counter++;
	    	        	        	    }
	    	          	       
	    	        System.out.println(counter-1);
	    	        anchor1= anchors.get(counter-1);
	    	       	System.out.println(anchor1);
		    	     HtmlPage page1 = anchor1.click();
		    	     String commentResponse = page1.getWebResponse().getContentAsString();
		    	     System.out.println(commentResponse);
		*/
    }
	}	    	        
		       
		    	         
	    	      
	         
	               
			
			
			
	

		
	     
   
	
		 
