package no.steria.quizzical;

import com.mongodb.*;
import no.steria.quizzical.admin.PasswordUtil;
import no.steria.quizzical.admin.User;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Adds two sample users. These are "martin" with password "eple", and "nikolai" with password "sopp",
 * and some sample quizzes.
 */
public class MongoDatabasePopulation {
	
	private DB db;
	private DBCollection usersInDB;
	private DBCollection responsesInDB;
	private final static String CREATE_USER = "CREATE_USER";

	//Singleton pattern ------
	private static MongoDatabasePopulation instance;
	private MongoDatabasePopulation() {
		init();
	}
	public static synchronized MongoDatabasePopulation getInstance() {
		if(instance == null) {
			instance = new MongoDatabasePopulation();
		}
		return instance;
	}
	//------------------------

	private void init() {
		MongoClient client = null;
		try {
			client = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = client.getDB("quizzical");
		usersInDB = db.getCollection("users");
		responsesInDB = db.getCollection("responses");
	}

	public static void main(String[] args) {
		if (args != null && args.length > 0) {
			String action = args[0];

			if(CREATE_USER.equals(action)) {
				String user = args[1];
				String pass = args[2];
				MongoDatabasePopulation.getInstance().createUser(user, pass);
				return;
			}

			throw new IllegalArgumentException("Invalid action '" + action + "'");
		}

		System.out.println("Usage:\n" +
				"To create a user: CREATE_USER <username> <password>\n");
	}

	private void createUser(String username, String pass) {
		User user = getNewUser(username, pass);
		insertUser(user);
	}

	private User getNewUser(String username, String password){
		int userId=1; //TODO: Do not use hardcoded id
		try {
			byte[] salt = PasswordUtil.generateSalt();
			byte[] encryptedPassword = PasswordUtil.getEncryptedPassword(password, salt);
			return new User(userId, username, salt, encryptedPassword, new ArrayList<Integer>());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void insertUser(User user){
		BasicDBObject userToDB = new BasicDBObject();
		userToDB.put("userId", user.getUserId());
		userToDB.put("username", user.getUsername());
		userToDB.put("salt", user.getSalt());
		userToDB.put("encpassword", user.getEncryptedPassword());
		userToDB.put("quizzes", user.getQuizIds());
		usersInDB.insert(userToDB);
	}

	public void dropResponsesInDB(){
		responsesInDB.drop();
	}

	public Quiz testQuiz1(){
		List<Question> questions = new ArrayList<Question>();
		
		List<Alternative> alternatives1 = new ArrayList<Alternative>();
		alternatives1.add(new Alternative(1, "Oslo"));
		alternatives1.add(new Alternative(2, "Bergen"));
		alternatives1.add(new Alternative(3, "Trondheim"));
		alternatives1.add(new Alternative(4, "Kristiansand"));
		questions.add(new Question(1, "What is the capital of Norway?", alternatives1, 1));
		
		List<Alternative> alternatives2 = new ArrayList<Alternative>();
		alternatives2.add(new Alternative(1, "Sognsvann"));
		alternatives2.add(new Alternative(2, "Tyrifjorden"));
		alternatives2.add(new Alternative(3, "Mjosa"));
		alternatives2.add(new Alternative(4, "Burudvann"));
		questions.add(new Question(2, "What is the largest lake in Norway?", alternatives2, 3));		
		
		return new Quiz(1,"Geography Quiz","This is a quiz about Norwegian geography. The questions span from fantastic cities to amazing lakes.", "Thank you for taking the quiz. The winner will be announced on 2. august at 4 PM.", questions,  "English", true);
	}

	public Response testResponse1(){
		HashMap<String, Integer> quizAnswers = new HashMap<String, Integer>();
		quizAnswers.put("q1", 2);
		quizAnswers.put("q2", 1);
		Response response = new Response(1, "TestVisitor", "test@user.com",  "Sopra", "113", quizAnswers);
		return response;
	}
	
	public Response testResponse2(){
		HashMap<String, Integer> quizAnswers = new HashMap<String, Integer>();
		quizAnswers.put("q1", 1);
		quizAnswers.put("q2", 1);
		Response response = new Response(1, "Lars", "lars@tester.com",  "Sopra", "113", quizAnswers);
		return response;
	}
	
}