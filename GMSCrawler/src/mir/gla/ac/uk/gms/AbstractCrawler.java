package mir.gla.ac.uk.gms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
* The AbstractCrawler class represents an abstract crawler. Each specific crawler needs to be written for each source and
* each such class needs extend this class.
*
* @author  Md. Sadek Ferdous
* @version 1.0
* @since   11/08/2014
*/
public abstract class AbstractCrawler {
	
	// The master URL represents the home page of each source
	private String masterURL;
	
	/**
	 * The constructor for being initialised with the master URL
	 * @param URL	the master URL
	 */
	public AbstractCrawler(String URL){
		this.masterURL = URL;
	}
	
	/**
	 * The getter and setter of the master URL
	 * 
	 */
	public String getMasterURL() {
		return masterURL;
	}


	public void setMasterURL(String masterURL) {
		this.masterURL = masterURL;
	}

	//the abstract class will be used to crawl embedded URLs from the home page using the master URL
	public abstract ArrayList<HashMap<String, String>> crawlURLs() throws IOException;
	// the abstract class will be used to retrieve news and other related info from a URL 
	public abstract GMSDocument crawlNews(HashMap<String, String> info) throws IOException;
	// the abstract class to for storing everything into the database
	public abstract void store();
}
