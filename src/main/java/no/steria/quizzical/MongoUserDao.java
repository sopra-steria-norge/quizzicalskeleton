package no.steria.quizzical;

import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoUserDao{
			
	private DB db;
	private DBCollection collection;
	
	public MongoUserDao() {
		db = MongoConnection.getConnection();
		collection = db.getCollection("users");
	}

	public User getUser(int userId){
		return this.getUser("userId",userId);
	}
	public User getUser(String username){
		return this.getUser("username", username);
	}
	private User getUser(String key, Object value){
		User user = null;
		DBCursor cursor = collection.find(new BasicDBObject(key,value));
		while(cursor.hasNext()){
			DBObject document = cursor.next();
			Integer userId = (Integer) document.get("userId");
			String username = (String) document.get("username");
			@SuppressWarnings("unchecked")
			ArrayList<Integer> quizzes = (ArrayList<Integer>) document.get("quizzes");
			byte[] salt = (byte[]) document.get("salt");
			byte[] encryptetPassword= (byte[]) document.get("encpassword");
			user = new User(userId, username, salt, encryptetPassword,quizzes);
		}
		return user;
	}
	
	
	public void addQuizIdToUser(int quizId, int userId) {
		DBObject document = collection.findOne(new BasicDBObject("userId",userId));
		@SuppressWarnings("unchecked")
		ArrayList<Integer> quizzes = (ArrayList<Integer>) document.get("quizzes");
		if(!quizzes.contains(quizId)){
			collection.remove(document);
			quizzes.add(quizId);
			document.put("quizzes", quizzes);
			collection.insert(document);
		}
	}

	public void removeQuizIdFromUsers(int quizId){
		DBCursor cursor = collection.find();
		while(cursor.hasNext()){
			DBObject document = cursor.next();	
			@SuppressWarnings("unchecked")
			ArrayList<Integer> quizzes = (ArrayList<Integer>) document.get("quizzes");
			if(quizzes.contains(quizId)){
				collection.remove(document);
				quizzes.remove((Object)quizId);
				document.put("quizzes", quizzes);
				collection.insert(document);
			}
		}
	}
	
}
