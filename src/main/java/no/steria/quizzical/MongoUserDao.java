package no.steria.quizzical;

import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoUserDao{
			
	private DB db;
	private DBCollection collection;
	
	public MongoUserDao() {
		try {
			MongoClient client = new MongoClient();
			db = client.getDB("quizzical");
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		collection = db.getCollection("users");
	}
		
	public User getUser(int userId){
		User user = null;
		DBCursor cursor = collection.find(new BasicDBObject("userId",userId));
		while(cursor.hasNext()){
			DBObject next = cursor.next();
			String username = (String) next.get("username");
			String password = (String) next.get("password");			
			@SuppressWarnings("unchecked")
			ArrayList<Integer> quizzes = (ArrayList<Integer>) next.get("quizzes");
			user = new User(userId, username, password, quizzes);
		}
		return user;
	}

	
	public void addQuizToUser(int userId, int quizId) {
		// Implement method
	}

}
