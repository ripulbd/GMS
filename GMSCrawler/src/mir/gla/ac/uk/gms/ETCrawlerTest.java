package mir.gla.ac.uk.gms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		System.out.println("##############-[ET crawling starts at:" + dateFormat.format(cal.getTime()) + "]-##############");
		
		try {
			/*String tmpURL = "http://www.eveningtimes.co.uk/news/u/michelle-mone-banned-for-six-months-after-speeding-on-m77.1408708353";
			
			HashMap<String, String> tmpHashMap = new HashMap<String, String>();
			tmpHashMap.put("url", tmpURL);
			tmpHashMap.put("title", "Michelle Mone banned for six months after speeding on M77");
			
			System.out.println("Added URL:" + tmpHashMap.get("url"));
			GMSNewsDocument etDoc = (GMSNewsDocument) etCrawler.crawlNews(tmpHashMap);
			System.out.println(etDoc);
			etCrawler.store(etDoc);*/
			
			ArrayList<HashMap<String, String>> urlInfos = etCrawler.crawlURLs();
			System.out.println("Total News Fetched:" + urlInfos.size());
			int pauseCount = 0;
			for(HashMap<String, String> tmpHashMap : urlInfos){
				System.out.println("Added URL:" + tmpHashMap.get("url"));
				GMSNewsDocument etDoc = (GMSNewsDocument) etCrawler.crawlNews(tmpHashMap);
				//System.out.println(etDoc);
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
			}
			
			/**
			 * Now, copy the newly saved images from the java image folder to the Go-Lang image folder 
			 */
			if(urlInfos.size() > 0) {
				File srcFolder = new File("/home/ripul/images/et/");
		    	File destFolder = new File("/home/ripul/resources/images/");
		    	
		    	System.out.println("Copying is starting...");
		    	//make sure source exists
		    	if(!srcFolder.exists()){
		 
		           System.out.println("Directory does not exist.");
		           //just exit
		           System.exit(0);
		 
		        }else{
		 
		           try{
		        	copyFolder(srcFolder,destFolder);
		           }catch(IOException e){
		        	e.printStackTrace();
		        	//error, just exit
		                System.exit(0);
		           }
		        }
		    	System.out.println("Coying is Done!");
			}
		} catch (HttpStatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal = Calendar.getInstance();
		System.out.println("##############-[ET crawling ends at:" + dateFormat.format(cal.getTime()) + "]-##############");
	}
	
	public static void copyFolder(File src, File dest)
	    	throws IOException{
	 
	    	if(src.isDirectory()){
	 
	    		//if directory not exists, create it
	    		if(!dest.exists()){
	    		   dest.mkdir();
	    		   System.out.println("Directory copied from " 
	                              + src + "  to " + dest);
	    		}
	 
	    		//list all the directory contents
	    		String files[] = src.list();
	 
	    		for (String file : files) {
	    		   //construct the src and dest file structure
	    		   File srcFile = new File(src, file);
	    		   File destFile = new File(dest, file);
	    		   //recursive copy
	    		   copyFolder(srcFile,destFile);
	    		}
	 
	    	}else{
	    		//if file, then copy it
	    		//Use bytes stream to support all file types
	    		InputStream in = new FileInputStream(src);
	    	        OutputStream out = new FileOutputStream(dest); 
	 
	    	        byte[] buffer = new byte[1024];
	 
	    	        int length;
	    	        //copy the file content in bytes 
	    	        while ((length = in.read(buffer)) > 0){
	    	    	   out.write(buffer, 0, length);
	    	        }
	 
	    	        in.close();
	    	        out.close();
	    	        //System.out.println("File copied from " + src + " to " + dest);
	    	}
	    }

}
