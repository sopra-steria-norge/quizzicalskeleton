package no.steria.quizzical;

import java.util.Date;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoDemo {

	public static void main(String[] args) throws Exception {
		MongoClient client = new MongoClient();
		DB db = client.getDB("javatest");
		
		Set<String> tables = db.getCollectionNames();
		 
		for(String coll : tables){
			System.out.println(coll);
		}
		
		DBCollection table = db.getCollection("user");
		BasicDBObject document = new BasicDBObject();
		document.put("name", "username");
		document.put("age", 30);
		document.put("createdDate", new Date());
		table.insert(document);
		

	}

}
