import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.JavaScriptPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Node;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLButtonElement;


public class News {

	private static String outputFolder = "/home/ripul/images/scotsman/";
	
	private String headline, timeStamp, mainStory, category, source, description, link;
	private ArrayList<HashMap<String, String>> imageNameCaption, videoNameCaption;
	private ArrayList<String> relatedStoryLink, keywords;
	private ArrayList<Comment> comments;
	
	public News(String link){
		this.link = link;
		imageNameCaption = new ArrayList<HashMap<String, String>>();
		videoNameCaption = new ArrayList<HashMap<String, String>>();
		relatedStoryLink = new ArrayList<String>();
		keywords = new ArrayList<String>();
		comments = new ArrayList<Comment>();
	}
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	
	public static String getOutputFolder() {
		return outputFolder;
	}
	public static void setOutputFolder(String outputFolder) {
		News.outputFolder = outputFolder;
	}
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getMainStory() {
		return mainStory;
	}
	public void setMainStory(String mainStory) {
		this.mainStory = mainStory;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ArrayList<Comment> getComments() {
		return comments;
	}
	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}

	public ArrayList<HashMap<String, String>> getImageNameCaption() {
		return imageNameCaption;
	}

	public void setImageNameCaption(
			ArrayList<HashMap<String, String>> imageNameCaption) {
		this.imageNameCaption = imageNameCaption;
	}

	public ArrayList<HashMap<String, String>> getVideoNameCaption() {
		return videoNameCaption;
	}

	public void setVideoNameCaption(
			ArrayList<HashMap<String, String>> videoNameCaption) {
		this.videoNameCaption = videoNameCaption;
	}

	public ArrayList<String> getRelatedStoryLink() {
		return relatedStoryLink;
	}

	public void setRelatedStoryLink(ArrayList<String> relatedStoryLink) {
		this.relatedStoryLink = relatedStoryLink;
	}

	public ArrayList<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
	}
	
	
}
