package mir.gla.ac.uk.gms;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class UpdateDate {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBUtils dbUtils = new DBUtils();
		
		/**
		 * Fixing the date of BBC from 15 September to 15/09/2014
		 */
		/*BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("source", "http://www.bbc.co.uk");
		
		DBCollection table = dbUtils.getTable();
	 
		DBCursor cursor = table.find(searchQuery);
		System.out.println("Count:" + cursor.count());
		while(cursor.hasNext()){
			
			DBObject dbObject = cursor.next();
			String URL = dbObject.get("url").toString();
			String oldTimeStamp = dbObject.get("timeStamp").toString();
			
			String[] array = oldTimeStamp.split(" ");
			if(array.length > 2){
				String newTimeStamp = array[0] + "/" + returnNumber(array[1]) + "/" + array[2];
				
				BasicDBObject newDocument = new BasicDBObject();			
				newDocument.put("timeStamp", newTimeStamp);
				
				BasicDBObject updateObj = new BasicDBObject();
			    updateObj.put("$set", newDocument);
			 
				BasicDBObject updateQuery = new BasicDBObject("url", URL);
			 
				table.update(updateQuery, updateObj,false,true);
				//System.out.println("Title:" + URL +", TimeStamp:" + oldTimeStamp +", New TimeStamp:" + newTimeStamp);
			}
		}*/
		
		/**
		 * Fixing the date of the DR from 2014-09-15 to 15/09/2014
		 */
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("source", "http://www.dailyrecord.co.uk");
		
		DBCollection table = dbUtils.getTable();
	 
		DBCursor cursor = table.find(searchQuery);
		System.out.println("Count:" + cursor.count());
		while(cursor.hasNext()){
			
			DBObject dbObject = cursor.next();
			String URL = dbObject.get("url").toString();
			String oldTimeStamp = dbObject.get("timeStamp").toString();
			
			String[] array = oldTimeStamp.split("-");
			if(array.length > 2){
				String newTimeStamp = array[2] + "/" + array[1] + "/" + array[0];
				
				BasicDBObject newDocument = new BasicDBObject();			
				newDocument.put("timeStamp", newTimeStamp);
				
				BasicDBObject updateObj = new BasicDBObject();
			    updateObj.put("$set", newDocument);
			 
				BasicDBObject updateQuery = new BasicDBObject("url", URL);
			 
				table.update(updateQuery, updateObj,false,true);
				System.out.println("Title:" + URL +", TimeStamp:" + oldTimeStamp +", New TimeStamp:" + newTimeStamp);
			}
			//break;
		}
	}
	
	public static String returnNumber(String month){
		String number = "";
		switch(month){
			case "January":
				number = "01";
				break;
			case "February":
				number = "02";
				break;
			case "March":
				number = "03";
				break;
			case "April":
				number = "04";
				break;
			case "May":
				number = "05";
				break;
			case "June":
				number = "06";
				break;
			case "July":
				number = "07";
				break;
			case "August":
				number = "08";
				break;
			case "September":
				number = "09";
				break;
			case "October":
				number = "10";
				break;
			case "November":
				number = "11";
				break;
			case "December":
				number = "12";
				break;			
		}
		return number;
	}

}
