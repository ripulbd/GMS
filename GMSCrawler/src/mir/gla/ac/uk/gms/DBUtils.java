package mir.gla.ac.uk.gms;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bson.io.BasicOutputBuffer;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DefaultDBEncoder;
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
	private long totalSize, totalNumber;
	
	public DBUtils(){
		dbName = "gmsTry";
		dbServer = "localhost";
		collection = "gmsNews";
		MongoClient mongo;
		try {
			mongo = new MongoClient(this.dbServer, 27017);
			
			/**** Get database ****/
			// if database doesn't exists, MongoDB will create it for you
			db = mongo.getDB(this.dbName);
			//db.dropDatabase();
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
	
	public long totalSize(){
		CommandResult re = db.getCollection(this.collection).getStats();
		/*for(String k: re.keySet()){
			System.out.println(k+"="+re.get(k) );
		    totalSize++;
		}*/
		//totalSize = re.getInt("size") / 1024;
		totalSize = re.getInt("size");
		return totalSize;
	}
	
	public long totalNumber(){
		CommandResult re = db.getCollection(this.collection).getStats();
		/*for(String k: re.keySet()){
			System.out.println(k+"="+re.get(k) );
		}*/
		totalNumber = re.getInt("count");
		return totalNumber;
	}
	
	public DBCollection getTable() {
		return table;
	}

	public long totalNumber(String source){
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("source", source);
	 
		DBCursor cursor = table.find(searchQuery);
		return cursor.count();
	}
	
	public long totalSize(String source){
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("source", source);
	 
		DBCursor cursor = table.find(searchQuery);
		int size = 0;
		while(cursor.hasNext()){
			DBObject dbObject = cursor.next();
			size += DefaultDBEncoder.FACTORY.create().writeObject(new BasicOutputBuffer(), dbObject);
		}
		
		//size = size / 1024;
		
		return size;
	}
	
	public long totalSizeEachElement(String URL){
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("url", URL);
	 
		DBCursor cursor = table.find(searchQuery);
		int size = 0;
		while(cursor.hasNext()){
			DBObject dbObject = cursor.next();
			size += DefaultDBEncoder.FACTORY.create().writeObject(new BasicOutputBuffer(), dbObject);
		}
		return size;
	}
	
	public long totalNumberEachDate(String date){
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("timeStamp", java.util.regex.Pattern.compile(date));
	 
		DBCursor cursor = table.find(searchQuery);
		
		return cursor.count();
	}
	
	public long totalNumberEachDate(String date, String source){
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("timeStamp", java.util.regex.Pattern.compile(date));
		searchQuery.put("source", source);
	 
		DBCursor cursor = table.find(searchQuery);
		
		return cursor.count();
	}
	
	public ArrayList<String> imageName(String URL){
		ArrayList<String> tempList = new ArrayList<String>();
		
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("url", URL);
	 
		DBCursor cursor = table.find(searchQuery);
		while(cursor.hasNext()){
			DBObject dbObject = cursor.next();
			BasicDBList list = (BasicDBList) dbObject.get("images");
			

			// optional: break it into a native java array
			BasicDBObject[] lightArr = list.toArray(new BasicDBObject[0]);
			for (BasicDBObject dbObj : lightArr) {
				// shows each item from the lights array
				String name = dbObj.getString("name");
				tempList.add(name);
			}
			
		}
		return tempList;
	}
	
	public ArrayList<GMSNewsDocument> returnNewsForSource(String source){
		ArrayList<GMSNewsDocument> newsDocList = new ArrayList<GMSNewsDocument>();
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("source", source);
	 
		DBCursor cursor = table.find(searchQuery);
		while(cursor.hasNext()){
			DBObject dbObject = cursor.next();
			
			String title = dbObject.get("title").toString();
			String URL = dbObject.get("url").toString();
			String timeStamp = dbObject.get("timeStamp").toString();
			
			GMSNewsDocument newsDoc = new GMSNewsDocument(URL);
			newsDoc.setTitle(title);
			newsDoc.setTimeStamp(timeStamp);
			newsDocList.add(newsDoc);
		}
		return newsDocList;
	}
	
	public HashMap<String, String> returnURL(String title){
		HashMap<String, String> tmpMap = new HashMap<String, String>();
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("title", title);
	 
		DBCursor cursor = table.find(searchQuery);
		DBObject dbObject = cursor.one();
		String URL = dbObject.get("url").toString();
		String timeStamp =dbObject.get("timeStamp").toString();
		tmpMap.put("url", URL);
		tmpMap.put("timeStamp", timeStamp);
		
		return tmpMap;
	}
}
