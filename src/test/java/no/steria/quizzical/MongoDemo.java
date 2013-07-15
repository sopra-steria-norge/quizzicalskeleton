package no.steria.quizzical;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class MongoDemo {
	
	public static void main(String[] args) throws Exception {
		MongoClient client = new MongoClient();
		DB db = client.getDB("quizzical");

		DBCollection table = db.getCollection("questions");
		table.drop();
		
		BasicDBList alternatives = new BasicDBList();
		alternatives.add(new BasicDBObject().append("aid", 1).append("atext", "Oslo"));
		alternatives.add(new BasicDBObject().append("aid", 2).append("atext", "Bergen"));
		alternatives.add(new BasicDBObject().append("aid", 3).append("atext", "Trondheim"));
		alternatives.add(new BasicDBObject().append("aid", 4).append("atext", "Kristiansand"));
		table.insert(makeDocumentForDB(1, "What is the capital of Norway?", alternatives, 1));

		BasicDBList alternatives2 = new BasicDBList();
		alternatives2.add(new BasicDBObject().append("aid", 1).append("atext", "Sognsvann"));
		alternatives2.add(new BasicDBObject().append("aid", 2).append("atext", "Tyrifjorden"));
		alternatives2.add(new BasicDBObject().append("aid", 3).append("atext", "Mjosa"));
		alternatives2.add(new BasicDBObject().append("aid", 4).append("atext", "Burudvann"));
		table.insert(makeDocumentForDB(2, "What is the largest lake in Norway?", alternatives2, 3));
	
	}
	
	private static BasicDBObject makeDocumentForDB(int idValue, String textValue, BasicDBList alternativeValues, int answerValue){
		BasicDBObject document = new BasicDBObject();
		document.put("id", idValue);
		document.put("text", textValue);
		document.put("alternatives", alternativeValues);
		document.put("answer", answerValue);
		return document;
	}
		
}
