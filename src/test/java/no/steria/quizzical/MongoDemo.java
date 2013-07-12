package no.steria.quizzical;

import com.mongodb.BasicDBList;
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
		
		BasicDBList alternatives = new BasicDBList();
		alternatives.add(new BasicDBObject("alt1", "Oslo"));
		alternatives.add(new BasicDBObject("alt2", "Bergen"));
		alternatives.add(new BasicDBObject("alt3", "Trondheim"));
		alternatives.add(new BasicDBObject("alt4", "Kristiansand"));
		table.insert(makeDocumentForDB(1, "What is the capital of Norway?", alternatives, 1));
		
//		ArrayList<BasicDBObject> alternatives2 = new ArrayList<BasicDBObject>();
//		alternatives2.add("Sognsvann");
//		alternatives2.add("Tyrifjorden");
//		alternatives2.add("Glomma");
//		alternatives2.add("Burudvann");
//		table.insert(makeDocumentForDB(2, "What is the largest lake in Norway?", alternatives2, 2));

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
