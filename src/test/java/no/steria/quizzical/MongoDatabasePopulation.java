package no.steria.quizzical;

import com.mongodb.*;

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
	private DBCollection quizzesInDB;
	private DBCollection usersInDB;
	private DBCollection responsesInDB;
	private PasswordUtil passwordUtil;
	private final static String CREATE_USER = "CREATE_USER";
	private final static String SET_TEST_QUIZZES = "SET_TEST_QUIZZES";

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
		quizzesInDB = db.getCollection("quizzes");
		usersInDB = db.getCollection("users");
		responsesInDB = db.getCollection("responses");
		
		passwordUtil = new PasswordUtil();
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
			else if(SET_TEST_QUIZZES.equals(action)) {
				MongoDatabasePopulation.getInstance().insertTestQuizzesIntoDB();
				return;
			}

			throw new IllegalArgumentException("Invalid action '" + action + "'");
		}

		System.out.println("Usage:\n" +
				"To create a user: CREATE_USER <username> <password>\n" +
				"To clear quizzes and set test quiz data: SET_TEST_QUIZZES");
	}

	private void createUser(String username, String pass) {
		User user = getUser(username, pass);
		insertUser(user);
	}

	private User getUser(String username, String password){
		int userId=1;
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

	/**
	 * This will drop all quizzes and insert test quizzes.
	 */
	void insertTestQuizzesIntoDB(){
		List<Quiz> quizzes = createQuizData();

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
	
	private List<Quiz> createQuizData(){
		List<Quiz> quizzes = new ArrayList<Quiz>();
		quizzes.add(testQuiz1());
		quizzes.add(testQuiz2());
		quizzes.add(testQuiz3());
		return quizzes;
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
		
		return new Quiz(1,"Geography Quiz","This is a quiz about Norwegian geography. The questions span from fantastic cities to amazing lakes.", "Thank you for taking the quiz. The winner will be announced on 2. august at 4 PM.", questions, true);	
	}
	
	private Quiz testQuiz2(){
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
	
	private Quiz testQuiz3(){
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
	
	public Quiz getQuizHelper(int quizId) {
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

	public Response testResponse1(){
		HashMap<String, Integer> quizAnswers = new HashMap<String, Integer>();
		quizAnswers.put("q1", 2);
		quizAnswers.put("q2", 1);
		Response response = new Response(1, "TestVisitor", "test@user.com", quizAnswers);
		return response;
	}
	
	public Response testResponse2(){
		HashMap<String, Integer> quizAnswers = new HashMap<String, Integer>();
		quizAnswers.put("q1", 1);
		quizAnswers.put("q2", 1);
		Response response = new Response(1, "Lars", "lars@tester.com", quizAnswers);
		return response;
	}
	
}