package no.steria.quizzical;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class MongoDemo {
	
	public static void main(String[] args) throws Exception {
		MongoClient client = new MongoClient();
		DB db = client.getDB("quizzical");

		DBCollection quizzes = db.getCollection("quizzes");
		quizzes.drop();
		
		// Quiz number one		
		BasicDBList quiz1 = new BasicDBList();
		
		BasicDBList alternatives11 = new BasicDBList();
		alternatives11.add(new BasicDBObject().append("aid", 1).append("atext", "Oslo"));
		alternatives11.add(new BasicDBObject().append("aid", 2).append("atext", "Bergen"));
		alternatives11.add(new BasicDBObject().append("aid", 3).append("atext", "Trondheim"));
		alternatives11.add(new BasicDBObject().append("aid", 4).append("atext", "Kristiansand"));
		quiz1.add(createQuestionDBObject(1, "What is the capital of Norway?", alternatives11, 1));

		BasicDBList alternatives12 = new BasicDBList();
		alternatives12.add(new BasicDBObject().append("aid", 1).append("atext", "Sognsvann"));
		alternatives12.add(new BasicDBObject().append("aid", 2).append("atext", "Tyrifjorden"));
		alternatives12.add(new BasicDBObject().append("aid", 3).append("atext", "Mjosa"));
		alternatives12.add(new BasicDBObject().append("aid", 4).append("atext", "Burudvann"));
		quiz1.add(createQuestionDBObject(2, "What is the largest lake in Norway?", alternatives12, 3));

		quizzes.insert(createQuiz(1, "Geography Quiz", "This is a quiz about Norwegian geography", "Thank you for taking the quiz", quiz1));

		// Quiz number two
		BasicDBList quiz2 = new BasicDBList();
		
		BasicDBList alternatives21 = new BasicDBList();
		alternatives21.add(new BasicDBObject().append("aid", 1).append("atext", "Oslo"));
		alternatives21.add(new BasicDBObject().append("aid", 2).append("atext", "Bergen"));
		alternatives21.add(new BasicDBObject().append("aid", 3).append("atext", "Trondheim"));
		alternatives21.add(new BasicDBObject().append("aid", 4).append("atext", "Kristiansand"));
		quiz2.add(createQuestionDBObject(1, "What is the capital of Norway?", alternatives21, 1));

		BasicDBList alternatives22 = new BasicDBList();
		alternatives22.add(new BasicDBObject().append("aid", 1).append("atext", "Sognsvann"));
		alternatives22.add(new BasicDBObject().append("aid", 2).append("atext", "Tyrifjorden"));
		alternatives22.add(new BasicDBObject().append("aid", 3).append("atext", "Mjosa"));
		alternatives22.add(new BasicDBObject().append("aid", 4).append("atext", "Burudvann"));
		quiz2.add(createQuestionDBObject(2, "What is the largest lake in Norway?", alternatives22, 3));

		quizzes.insert(createQuiz(1, "Science Quiz", "This is a quiz about Norwegian geography", "Thank you for taking the quiz", quiz2));
		}
	
	private static BasicDBObject createQuestionDBObject(int idValue, String textValue, BasicDBList alternativeValues, int answerValue){
		BasicDBObject document = new BasicDBObject();
		document.put("id", idValue);
		document.put("text", textValue);
		document.put("alternatives", alternativeValues);
		document.put("answer", answerValue);
		return document;
	}
	
	private static BasicDBObject createQuiz(int quizId, String quizName, String quizDescription, String quizSubmittedMsg, BasicDBList questions){
		BasicDBObject quiz = new BasicDBObject();
		quiz.put("quizid", quizId);
		quiz.put("name", quizName);
		quiz.put("desc", quizDescription);
		quiz.put("submitMsg", quizSubmittedMsg);
		quiz.put("questions", questions);
		return quiz;
	}
	
}
