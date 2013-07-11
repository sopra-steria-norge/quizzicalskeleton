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
		DB db = client.getDB("quizzical");
		
		DBCollection table = db.getCollection("questions");
		table.drop();
		BasicDBObject document = new BasicDBObject();
		document.put("id", 1);
		document.put("text", "What is the capital of Norway?");
		table.insert(document);
		
		BasicDBObject document2 = new BasicDBObject();
		document2.put("id", 2);
		document2.put("text", "What is the largest lake in Norway?");
		table.insert(document2);

	}

}
