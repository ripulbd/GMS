package mir.gla.ac.uk.gms;

import java.io.BufferedReader;
import java.io.FileReader;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class DRFixer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBUtils dbUtil = new DBUtils();
		try
		  {
		    BufferedReader reader = new BufferedReader(new FileReader("/home/ripul/drLinks"));
		    String line;
		    while ((line = reader.readLine()) != null)
		    {
		      System.out.println(line);
		      BasicDBObject searchQuery = new BasicDBObject();
		      searchQuery.put("url", line);
			  DBCollection table = dbUtil.getTable();
		      
			  DBCursor cursor = table.find(searchQuery);
			  while(cursor.hasNext()){
				  DBObject dbObject = cursor.next();
				  table.remove(dbObject); 
			  }			  
			  //break;
		    }
		    reader.close();
		  }
		  catch (Exception e)
		  {
		    System.err.format("Exception occurred trying to read '%s'.", "/home/ripul/drLinks");
		    e.printStackTrace();
		  }
	}

}
