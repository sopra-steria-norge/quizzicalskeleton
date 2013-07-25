package no.steria.quizzical;

import java.net.UnknownHostException;
import java.util.Random;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoResponseDao{
		
	private DB db;
	private DBCollection collection;
	
	public MongoResponseDao() {
		try {
			MongoClient client = new MongoClient();
			db = client.getDB("quizzical");
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		collection = db.getCollection("responses");
	}

	public void setResponse(Response response) {		
		BasicDBObject document = new BasicDBObject();
		document.put("quizId", response.getQuizId());
		document.put("name", response.getName());
		document.put("email", response.getEmail());
		document.put("score", response.getScore());
		
		collection.insert(document);
	}
	
	public void setResponse(DBObject document){
		collection.insert(document);
	}

	public int countResponsesForQuiz(int quizId){		
		DBCursor cursor = collection.find(new BasicDBObject("quizId",quizId));
		return cursor.count();
	}
	
	public String[] drawRandomWinner(int quizId){
		DBCursor cursor = collection.find(new BasicDBObject("quizId",quizId));
		Random rand = new Random();
		int length = cursor.count();
		String[] winner = new String[2];
		if(length>0){
			cursor.skip(rand.nextInt(length));
			DBObject next = cursor.next();
			winner[0] = (String) next.get("name");
			winner[1] = (String) next.get("email");
		}else{
			winner[0] = "No responses yet!";
			winner[1] = "";
		}
		return winner;
	}
	
}
