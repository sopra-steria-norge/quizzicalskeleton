package no.steria.quizzical.admin;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import no.steria.quizzical.MongoConnection;

public class MongoUserDao {

	public static final String USERNAME = "username";
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
		return this.getUser(USERNAME, username);
	}

	private User getUser(String key, Object value){
		DBObject document = collection.findOne(new BasicDBObject(key, value));

		if(document == null) {
			return null;
		}

		Integer userId = (Integer) document.get("userId");
		String username = (String) document.get(USERNAME);
		@SuppressWarnings("unchecked")
		List<Integer> quizzes = (ArrayList<Integer>) document.get("quizzes");
		byte[] salt = (byte[]) document.get("salt");
		byte[] encryptetPassword= (byte[]) document.get("encpassword");

		return new User(userId, username, salt, encryptetPassword,quizzes);
	}

	public void addQuizIdToUser(int quizId, int userId) {
		DBObject document = collection.findOne(new BasicDBObject("userId",userId));
		@SuppressWarnings("unchecked")
		List<Integer> quizzes = (ArrayList<Integer>) document.get("quizzes");
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
			List<Integer> quizzes = (ArrayList<Integer>) document.get("quizzes");
			if(quizzes.contains(quizId)){
				collection.remove(document);
				quizzes.remove((Object)quizId);
				document.put("quizzes", quizzes);
				collection.insert(document);
			}
		}
	}

	//TODO: Move to user service
	boolean validatePassword(String username, String password) {
		User user = getUser(username);
		Boolean validated;

		try {
			validated = user != null &&
					user.getSalt() != null &&
					new PasswordUtil().authenticate(password, user.getEncryptedPassword(), user.getSalt());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			validated = false;
		}
		return validated;

	}

	//TODO: Move to user service
	public void setPassword(String username, String newPass) {

		try {
			DBObject userObj = collection.findOne(new BasicDBObject(USERNAME, username));

			byte[] salt = PasswordUtil.generateSalt();
			byte[] encryptedPassword = PasswordUtil.getEncryptedPassword(newPass, salt);

			userObj.put("salt", salt);
			userObj.put("encpassword", encryptedPassword);

			collection.update(new BasicDBObject(USERNAME, username), userObj);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
