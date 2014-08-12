package mir.gla.ac.uk.gms;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;


public class DBUtils {
	private String dbName;
	private String dbServer;
	private String userName;
	private String password;
	private String collection;
	private DB db;
	private MongoClient mongo;
	private DBCollection table;
	
	public DBUtils(){
		dbName = "test";
		dbServer = "localhost";
		collection = "document";
		MongoClient mongo;
		try {
			mongo = new MongoClient(this.dbServer, 27017);
			
			/**** Get database ****/
			// if database doesn't exists, MongoDB will create it for you
			DB db = mongo.getDB(this.dbName);
			db.dropDatabase();
			/**** Get collection ****/
			// if collection doesn't exists, MongoDB will create it for you
			table = db.getCollection(this.collection);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DBUtils(String dbName, String dbServer, String collection){
		this.dbName = dbName;
		this.dbServer = dbServer;
		this.collection = collection;
		MongoClient mongo;
		try {
			mongo = new MongoClient(this.dbServer, 27017);
			
			/**** Get database ****/
			// if database doesn't exists, MongoDB will create it for you
			DB db = mongo.getDB(this.dbName);
		 
			/**** Get collection ****/
			// if collection doesn't exists, MongoDB will create it for you
			table = db.getCollection(this.collection);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DBUtils(String dbName, String dbServer, String collection, String username, String password){
		this.dbName = dbName;
		this.dbServer = dbServer;
		this.collection = collection;
		this.userName = username;
		this.password = password;
		MongoClient mongo;
		try {
			mongo = new MongoClient(this.dbServer, 27017);
			
			/**** Get database ****/
			// if database doesn't exists, MongoDB will create it for you
			DB db = mongo.getDB(dbName);
			
			boolean auth = db.authenticate(this.userName, this.password.toCharArray());
		 
			/**** Get collection ****/
			// if collection doesn't exists, MongoDB will create it for you
			table = db.getCollection(this.collection);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbServer() {
		return dbServer;
	}
	public void setDbServer(String dbServer) {
		this.dbServer = dbServer;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void addDocument(GMSNewsDocument newsDoc){
		String title = newsDoc.getTitle();
		String description = newsDoc.getDescription();
		String timeStamp = newsDoc.getTimeStamp();
		String category = newsDoc.getCategory();
		String URL = newsDoc.getURL();
		String source = newsDoc.getSource();
		String mainStory = newsDoc.getMainStory();
		ArrayList<String> keywords = newsDoc.getKeywords();
		ArrayList<CommentDocument> comments = newsDoc.getComments();
		ArrayList<HashMap<String, String>> imageNameCaption = newsDoc.getImageNameCaption();
		ArrayList<HashMap<String, String>> videoNameCaption = newsDoc.getVideoNameCaption();	
		ArrayList<HashMap<String, String>> relatedStories = newsDoc.getRelatedStories();
		
		BasicDBObject newsDocument = new BasicDBObject();
		newsDocument.put("title", title);
		newsDocument.put("description", description);
		newsDocument.put("timeStamp", timeStamp);
		newsDocument.put("category", category);
		newsDocument.put("url", URL);
		newsDocument.put("source", source);
		newsDocument.put("mainStory", mainStory);
		if(keywords != null){
			//BasicDBObject keywordDoc = new BasicDBObject("$pushAll", new BasicDBObject("keywords", keywords));
			//newsDocument.put("$pushAll", new BasicDBObject("keywords", keywords));
			newsDocument.put("keywords", keywords);
		}
		
		if(imageNameCaption != null){
			//newsDocument.put("$pushAll", new BasicDBObject("keywords", keywords));
			newsDocument.put("images", imageNameCaption);
		}
		
		if(videoNameCaption != null){
			//newsDocument.put("$pushAll", new BasicDBObject("keywords", keywords));
			newsDocument.put("videos", videoNameCaption);
		}
		
		if(relatedStories != null){
			//newsDocument.put("$pushAll", new BasicDBObject("keywords", keywords));
			newsDocument.put("relatedStories", relatedStories);
		}
		
		if(comments != null){
			newsDocument.put("comments", comments);
			//newsDocument.put("comments", JSON.parse(comments.toString()));
			/*BasicDBObject commentOBject = new BasicDBObject();
			for(CommentDocument commentDoc : comments){
				
			}*/
		}
		
		table.insert(newsDocument);
		/*protected String title, description, timeStamp, category, source;
		protected String URL;
		protected ArrayList<CommentDocument> comments;
		protected ArrayList<String> keywords;*/
		/*for (HashMap<String, String> tmpMap : list) {
			BasicDBObject document = new BasicDBObject();
			for (String key : tmpMap.keySet()) {
				document.put(key, tmpMap.get(key));
			}
			table.insert(document);
		}*/
	}
	
	public boolean find(String key, String value){
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put(key, value);
	 
		DBCursor cursor = table.find(searchQuery);
		if(cursor.size() > 0)return true;
		return false;
	}
	
}
