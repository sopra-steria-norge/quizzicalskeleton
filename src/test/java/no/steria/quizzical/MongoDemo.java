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
	private static DBCollection quizzesToDB;
	private static DBCollection usersInDB;
	
	public static void main(String[] args) {
		insertQuizzesIntoDB(createQuizData());
		insertTestUsersIntoDB();
	}
	
	public static void insertTestQuizzes(){
		insertQuizzesIntoDB(createQuizData());
	}
		
	private static void init(){
		MongoClient client = null;
		try {
			client = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = client.getDB("quizzical");		
	}

	private static List<Quiz> createQuizData(){
		List<Quiz> quizzes = new ArrayList<Quiz>();
		quizzes.add(testQuiz1());
		quizzes.add(testQuiz2());
		return quizzes;
	}
	
	private static void insertQuizzesIntoDB(List<Quiz> quizzes){
		init();
		quizzesToDB = db.getCollection("quizzes");
		quizzesToDB.drop();		
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
			quizzesToDB.insert(quizToDB);
		}
	}
	
	
	public static Quiz getQuizHelper(int quizId) {
		DBObject quizObject = quizzesToDB.findOne(new BasicDBObject("quizId", quizId));
		
		String quizName = (String) quizObject.get("name");
		String quizDesc = (String) quizObject.get("desc");
		String submitMsg = (String) quizObject.get("submitMsg");
		@SuppressWarnings("unchecked")
		List<Question> questions = (List<Question>) quizObject.get("questions");
		
		Quiz quiz = new Quiz(quizId, quizName, quizDesc, submitMsg, questions);
		return quiz;
	}
	
	
	private static void insertTestUsersIntoDB(){
		init();
		usersInDB = db.getCollection("users");
		usersInDB.drop();
		
		int userId1=1;
		String username1="martin", password1="eple";
		ArrayList<Integer> quizzes1 = new ArrayList<Integer>();
		quizzes1.add(1);
		quizzes1.add(2);		

		BasicDBObject user1 = new BasicDBObject();
		user1.put("userId", userId1);
		user1.put("username", username1);
		user1.put("password", password1);
		user1.put("quizzes", quizzes1);
		usersInDB.insert(user1);

		int userId2=2;
		String username2="nikolai", password2="sopp";
		ArrayList<Integer> quizzes2 = new ArrayList<Integer>();
		quizzes2.add(3);
		quizzes2.add(4);		

		BasicDBObject user2 = new BasicDBObject();
		user2.put("userId", userId2);
		user2.put("username", username2);
		user2.put("password", password2);
		user2.put("quizzes", quizzes2);
		usersInDB.insert(user2);
		
		int userId3=3;
		String username3="andy", password3="sitron";
		ArrayList<Integer> quizzes3 = new ArrayList<Integer>();
		quizzes3.add(1);
		quizzes3.add(5);		

		BasicDBObject user3 = new BasicDBObject();
		user3.put("userId", userId3);
		user3.put("username", username3);
		user3.put("password", password3);
		user3.put("quizzes", quizzes3);
		usersInDB.insert(user3);
		
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
		
		return new Quiz(1,"Geography Quiz","This is a quiz about Norwegian geography. The questions span from fantastic cities to amazing lakes.", "Thank you for taking the quiz. The winner will be announced on 2. august at 4 PM.", questions);	
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

		return new Quiz(2,"Science Quiz For Kids","This quiz contains question relevant for children with interest in science. Use this quiz vicely as kids can be extremely interested in science, and possibly learn too much.","Thank you for taking the quiz. The winner will be announced on 2. august at 4 PM year 2017", questions);	
	}
}

//	private static BasicDBObject createQuestionHelper(int idValue, String textValue, List<Alternative> alternatives, int answerValue){
//		BasicDBObject document = new BasicDBObject();
//		document.put("id", idValue);
//		document.put("text", textValue);
//		document.put("alternatives", alternatives);
//		document.put("answer", answerValue);
//		return document;
//	}
//	
//	private static BasicDBObject createQuizHelper(int quizId, String quizName, String quizDescription, String quizSubmittedMsg, List<Question> questions){
//		BasicDBObject quiz = new BasicDBObject();
//		quiz.put("quizId", quizId);
//		quiz.put("name", quizName);
//		quiz.put("desc", quizDescription);
//		quiz.put("submitMsg", quizSubmittedMsg);
//		quiz.put("questions", questions);
//		quiz.put("active", true);
//		return quiz;
//	}
//	public static List<Question> createSomeTestQuestions(){
//		List<Question> quiz1 = new ArrayList<Question>();
//		
//		List<Alternative> alternatives11 = new ArrayList<Alternative>();
//		alternatives11.add(new Alternative(1, "Oslo"));
//		alternatives11.add(new Alternative(2, "Bergen"));
//		quiz1.add(createQuestionHelper(1, "What is the capital of Norway?", alternatives11, 1));
//
//		List<Alternative> alternatives12 = new ArrayList<Alternative>();
//		alternatives12.add(new Alternative(1, "Sognsvann"));
//		alternatives12.add(new Alternative(2, "Tyrifjorden"));
//		quiz1.add(createQuestionHelper(2, "What is the largest lake in Norway?", alternatives12, 3));
//
//		return quiz1;
//	}
//	public static void insertTestQuizzesIntoDB(Quiz[] quiz) {
//		init();
//		quizzesToDB = db.getCollection("quizzes");
//		quizzesToDB.drop();
//		
//		// Quiz number one		
//		if(quiz[0].getQuestions() == null){
//			List<Question> quiz1 = new ArrayList<Question>();
//			
//			List<Alternative> alternatives11 = new ArrayList<Alternative>();
//			alternatives11.add(new Alternative(1, "Oslo"));
//			alternatives11.add(new Alternative(2, "Bergen"));
//			alternatives11.add(new Alternative(3, "Trondheim"));
//			alternatives11.add(new Alternative(4, "Kristiansand"));
//			quiz1.add(createQuestionHelper(1, "What is the capital of Norway?", alternatives11, 1));
//
//			List<Alternative> alternatives12 = new ArrayList<Alternative>();
//			alternatives12.add(new Alternative(1, "Sognsvann"));
//			alternatives12.add(new Alternative(2, "Tyrifjorden"));
//			alternatives12.add(new Alternative(3, "Mjosa"));
//			alternatives12.add(new Alternative(4, "Burudvann"));
//			quiz1.add(createQuestionHelper(2, "What is the largest lake in Norway?", alternatives12, 3));
//
//			quizzesToDB.insert(createQuizHelper(quiz[0].getQuizId(), quiz[0].getQuizName(), quiz[0].getQuizDesc(), quiz[0].getSubmitMsg(), quiz1));			
//
//		}else{
//			quizzesToDB.insert(createQuizHelper(quiz[0].getQuizId(), quiz[0].getQuizName(), quiz[0].getQuizDesc(), quiz[0].getSubmitMsg(), quiz[0].getQuestions()));			
//		}
//
//
//		// Quiz number two
//		if(quiz[0].getQuestions() == null){
//			List<Question> quiz2 = new ArrayList<Question>();
//			
//			List<Alternative> alternatives21 = new ArrayList<Alternative>();
//			alternatives21.add(new Alternative(1, "Oslo"));
//			alternatives21.add(new Alternative(2, "Bergen"));
//			alternatives21.add(new Alternative(3, "Trondheim"));
//			alternatives21.add(new Alternative(4, "Kristiansand"));
//			quiz2.add(createQuestionHelper(1, "What is the capital of Norway?", alternatives21, 1));
//
//			List<Alternative> alternatives22 = new ArrayList<Alternative>();
//			alternatives22.add(new Alternative(1, "Sognsvann"));
//			alternatives22.add(new Alternative(2, "Tyrifjorden"));
//			alternatives22.add(new Alternative(3, "Mjosa"));
//			alternatives22.add(new Alternative(4, "Burudvann"));
//			quiz2.add(createQuestionHelper(2, "What is the largest lake in Norway?", alternatives22, 3));
//	
//			quizzesToDB.insert(createQuizHelper(quiz[1].getQuizId(), quiz[1].getQuizName(), quiz[1].getQuizDesc(), quiz[1].getSubmitMsg(), quiz2));
//		}else{
//			quizzesToDB.insert(createQuizHelper(quiz[1].getQuizId(), quiz[1].getQuizName(), quiz[1].getQuizDesc(), quiz[1].getSubmitMsg(), quiz[1].getQuestions()));			
//		}
//	}