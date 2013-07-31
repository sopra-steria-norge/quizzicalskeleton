package no.steria.quizzical;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoDemo {
	
	private static DB db;
	private static DBCollection quizzesInDB;
	private static DBCollection usersInDB;
	private static PasswordUtil passwordUtil;
	
	private static void init(){
		MongoClient client = null;
		try {
			client = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = client.getDB("quizzical");
		
		passwordUtil = new PasswordUtil();
	}

	public static void main(String[] args) {
		init();
		if (args != null && args.length > 0 && "delete".equals(args[0])) {
			System.out.println("TODO not impl yet");
		} else {
			insertTestQuizzesIntoDB(createQuizData());
			insertTestUsersIntoDB(createUserData());
		}
	}
	
	public static void insertTestQuizzes(){
		init();
		insertTestQuizzesIntoDB(createQuizData());
	}	
	
	private static void insertTestQuizzesIntoDB(List<Quiz> quizzes){
		quizzesInDB = db.getCollection("quizzes");
		quizzesInDB.drop();		
		for(Quiz quizData : quizzes){
			BasicDBObject quizToDB = new BasicDBObject();
			quizToDB.put("quizId", quizData.getQuizId());
			quizToDB.put("name", quizData.getQuizName());
			quizToDB.put("desc", quizData.getQuizDesc());
			quizToDB.put("submitMsg", quizData.getSubmitMsg());
			BasicDBList questionsToDB = new BasicDBList();
			for(Question questionData : quizData.getQuestions()){
				BasicDBObject questionToDB = new BasicDBObject();
				questionToDB.put("id", questionData.getId());
				questionToDB.put("text", questionData.getText());
				questionToDB.put("answer", questionData.getAnswer());
				BasicDBList alternativesToDB = new BasicDBList();
				for(Alternative alternativeData : questionData.getAlternatives()){
					alternativesToDB.add(new BasicDBObject().append("aid", alternativeData.getAid()).append("atext", alternativeData.getAtext()));
				}
				questionToDB.put("alternatives", alternativesToDB);
				questionsToDB.add(questionToDB);
			}
			quizToDB.put("questions", questionsToDB);
			quizToDB.put("active", quizData.getActive());
			quizzesInDB.insert(quizToDB);
		}
	}
	
	private static List<Quiz> createQuizData(){
		List<Quiz> quizzes = new ArrayList<Quiz>();
		quizzes.add(testQuiz1());
		quizzes.add(testQuiz2());
		quizzes.add(testQuiz3());
		return quizzes;
	}
	
	public static Quiz testQuiz1(){
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
		
		return new Quiz(1,"Geography Quiz","This is a quiz about Norwegian geography. The questions span from fantastic cities to amazing lakes.", "Thank you for taking the quiz. The winner will be announced on 2. august at 4 PM.", questions, true);	
	}
	
	private static Quiz testQuiz2(){
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

		return new Quiz(2,"Science Quiz For Kids","This quiz contains question relevant for children with interest in science. Use this quiz vicely as kids can be extremely interested in science, and possibly learn too much.","Thank you for taking the quiz. The winner will be announced on 2. august at 4 PM year 2017", questions, true);	
	}
	
	private static Quiz testQuiz3(){
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
		
		return new Quiz(3,"Nikkos Superquiz","This is a quiz about Norwegian geography. The questions span from fantastic cities to amazing lakes.", "Thank you for taking the quiz. The winner will be announced on 2. august at 4 PM.", questions, true);	
	}
	
	public static Quiz getQuizHelper(int quizId) {
		DBObject quizObject = quizzesInDB.findOne(new BasicDBObject("quizId", quizId));
		
		String quizName = (String) quizObject.get("name");
		String quizDesc = (String) quizObject.get("desc");
		String submitMsg = (String) quizObject.get("submitMsg");
		@SuppressWarnings("unchecked")
		List<Question> questions = (List<Question>) quizObject.get("questions");
		boolean active = (boolean) quizObject.get("active");
		
		Quiz quiz = new Quiz(quizId, quizName, quizDesc, submitMsg, questions, active);
		return quiz;
	}
	
	public static void dropQuizzesInDB(){
		quizzesInDB = db.getCollection("quizzes");
		quizzesInDB.drop();
	}

	private static void insertTestUsersIntoDB(List<User> users){
		usersInDB = db.getCollection("users");
		usersInDB.drop();

		for(User user : users){
			BasicDBObject userToDB = new BasicDBObject();
			userToDB.put("userId", user.getUserId());
			userToDB.put("username", user.getUsername());
			userToDB.put("salt", user.getSalt());
			userToDB.put("encpassword", user.getEncryptedPassword());			
			userToDB.put("quizzes", user.getQuizIds());
			usersInDB.insert(userToDB);			
		}
	}
		
	private static List<User> createUserData(){
		List<User> users = new ArrayList<User>();
		users.add(testUser1());
		users.add(testUser2());
		users.add(testUser3());		
		return users;
	}
	
	public static User testUser1(){
		int userId=1;
		String username="martin", password="eple";
		ArrayList<Integer> quizzes = new ArrayList<Integer>();
		quizzes.add(1);
		quizzes.add(2);
		byte[] salt = null;
		byte[] encryptedPassword = null;
		try {
			salt = passwordUtil.generateSalt();
			encryptedPassword = passwordUtil.getEncryptedPassword(password, salt);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new User(userId, username, salt, encryptedPassword, quizzes);
	}
	
	private static User testUser2(){
		int userId=2;
		String username="nikolai", password="sopp";
		ArrayList<Integer> quizzes = new ArrayList<Integer>();
		quizzes.add(3);
		quizzes.add(1);
		byte[] salt = null;
		byte[] encryptedPassword = null;
		try {
			salt = passwordUtil.generateSalt();
			encryptedPassword = passwordUtil.getEncryptedPassword(password, salt);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new User(userId, username, salt, encryptedPassword, quizzes);
	}
	
	private static User testUser3(){
		int userId=3;
		String username="andy", password="sitron";
		ArrayList<Integer> quizzes = new ArrayList<Integer>();
		quizzes.add(1);
		quizzes.add(5);
		byte[] salt = null;
		byte[] encryptedPassword = null;
		try {
			salt = passwordUtil.generateSalt();
			encryptedPassword = passwordUtil.getEncryptedPassword(password, salt);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new User(userId, username, salt, encryptedPassword, quizzes);
	}
	
	
}