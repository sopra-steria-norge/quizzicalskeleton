package no.steria.quizzical;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoResponseDao{
		
	private DB db;
	
	public MongoResponseDao() {
		try {
			MongoClient client = new MongoClient();
			db = client.getDB("quizzical");
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	public void setResponse(Response response) {
		DBCollection collection = db.getCollection("responses");
		BasicDBObject document = new BasicDBObject();
		document.put("quizId", response.getQuizId());
		document.put("name", response.getName());
		document.put("email", response.getEmail());
		document.put("score", response.calculateAndRetrieveScore());
		collection.insert(document);
	}

}
