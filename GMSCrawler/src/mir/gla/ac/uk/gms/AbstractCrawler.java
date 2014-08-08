package mir.gla.ac.uk.gms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractCrawler {
	
	private String masterURL;
	
	private ArrayList<String> urls;
	
	public AbstractCrawler(String URL){
		this.masterURL = URL;
	}
	
	
	public String getMasterURL() {
		return masterURL;
	}


	public void setMasterURL(String masterURL) {
		this.masterURL = masterURL;
	}


	public ArrayList<String> getUrls() {
		return urls;
	}


	public void setUrls(ArrayList<String> urls) {
		this.urls = urls;
	}


	public abstract ArrayList<HashMap<String, String>> crawlURLs() throws IOException;
	public abstract GMSNewsDocument crawlNews(HashMap<String, String> info) throws IOException;
	public abstract void store();
}
