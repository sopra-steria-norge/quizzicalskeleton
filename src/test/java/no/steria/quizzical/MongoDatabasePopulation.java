package no.steria.quizzical;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import no.steria.quizzical.admin.MongoUserDao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MongoDatabasePopulation {
	
	private DB db;
	private DBCollection usersInDB;
	private DBCollection responsesInDB;
	private final static String CREATE_USER = "CREATE_USER";
	private static MongoUserDao mongoUserDao = new MongoUserDao();

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
				mongoUserDao.createUser(user, pass);
				return;
			}

			throw new IllegalArgumentException("Invalid action '" + action + "'");
		}

		System.out.println("Usage:\n" +
				"To create a user: CREATE_USER <username> <password>\n");
	}

	public void removeResponsesInDB(int quizId) {
		responsesInDB.remove(new BasicDBObject("quizId", quizId));
	}

	public Quiz testQuiz1(){
		List<Question> questions = new ArrayList<Question>();
		
		List<Alternative> alternatives1 = new ArrayList<Alternative>();
		alternatives1.add(new Alternative(1, "Oslo"));
		alternatives1.add(new Alternative(2, "Bergen"));
		alternatives1.add(new Alternative(3, "Trondheim"));
		alternatives1.add(new Alternative(4, "Kristiansand"));
		questions.add(new Question(1, "What is the capital of Norway?", alternatives1, 1, false));
		
		List<Alternative> alternatives2 = new ArrayList<Alternative>();
		alternatives2.add(new Alternative(1, "Sognsvann"));
		alternatives2.add(new Alternative(2, "Tyrifjorden"));
		alternatives2.add(new Alternative(3, "Mjosa"));
		alternatives2.add(new Alternative(4, "Burudvann"));
		questions.add(new Question(2, "What is the largest lake in Norway?", alternatives2, 3, false));
		
		return new Quiz(1,"Geography Quiz","This is a quiz about Norwegian geography. The questions span from fantastic cities to amazing lakes.", "Thank you for taking the quiz. The winner will be announced on 2. august at 4 PM.", questions,  0, false, true, null);
	}

}