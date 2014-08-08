package mir.gla.ac.uk.gms;

import java.util.ArrayList;

public abstract class GMSDocument {
	
	protected String title, description, timeStamp, category, source;
	protected String URL;
	protected ArrayList<CommentDocument> comments;
	protected ArrayList<String> keywords;
	
	
	public GMSDocument(String URL){
		this.URL = URL;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStamp() {
		return timeStamp;
	}

	public void setDate(String timeStamp) {
		this.timeStamp = timeStamp;
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

	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}
	
	
	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public ArrayList<CommentDocument> getComments() {
		return comments;
	}

	public void setComments(ArrayList<CommentDocument> comments) {
		this.comments = comments;
	}

	public ArrayList<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
	}
}

