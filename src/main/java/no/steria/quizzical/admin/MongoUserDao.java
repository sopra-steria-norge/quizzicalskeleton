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

	public void createUser(String username, String pass) {
		if(getUser(username) != null) {
			throw new CreateUserException("User " + username + " already exists");
		}

		User user = getNewUser(username, pass);
		insertUser(user);
	}

	private User getNewUser(String username, String password){
		int userId = getNewUserId();
		try {
			byte[] salt = PasswordUtil.generateSalt();
			byte[] encryptedPassword = PasswordUtil.getEncryptedPassword(password, salt);
			return new User(userId, username, salt, encryptedPassword, new ArrayList<Integer>());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	//TODO: This would re-use an old id of a deleted user. (Users are not deleted now though.)
	private int getNewUserId() {
		int newUserId = 1;
		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			if (dbo.containsField("userId")) {
				int userIdDB = (Integer)dbo.get("userId");
				if (userIdDB >= newUserId) {
					newUserId = userIdDB + 1;
				}
			}
		}
		return newUserId;
	}

	private void insertUser(User user){
		BasicDBObject userToDB = new BasicDBObject();
		userToDB.put("userId", user.getUserId());
		userToDB.put("username", user.getUsername());
		userToDB.put("salt", user.getSalt());
		userToDB.put("encpassword", user.getEncryptedPassword());
		userToDB.put("quizzes", user.getQuizIds());
		collection.insert(userToDB);
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
