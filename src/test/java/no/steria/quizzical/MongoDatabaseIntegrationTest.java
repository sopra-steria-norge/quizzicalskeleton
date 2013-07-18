package no.steria.quizzical;

import java.net.UnknownHostException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import static org.fest.assertions.Assertions.assertThat;

public class MongoDatabaseIntegrationTest {

	DBCollection quizzes;
	
	@Before
	public void setUp(){
		MongoClient client = null;
		try {
			client = new MongoClient();
		} catch (UnknownHostException e) {
			System.out.println("Unknown host");
			e.printStackTrace();
		}
		DB db = client.getDB("quizzical");
		quizzes = db.getCollection("quizzes");
		quizzes.drop();
	}
	
	@Test
	public void insertToAndRetrieveFromDatabase(){
		
		insertIntoDatabaseHelper();
		retrieveFromDatabaseHelper();
	}
	
	private void insertIntoDatabaseHelper(){
		// Quiz number one		
		BasicDBList quiz1 = new BasicDBList();
		
		BasicDBList alternatives11 = new BasicDBList();
		alternatives11.add(new BasicDBObject().append("aid", 1).append("atext", "Oslo"));
		alternatives11.add(new BasicDBObject().append("aid", 2).append("atext", "Bergen"));
		quiz1.add(createQuestionDBObjectHelper(1, "What is the capital of Norway?", alternatives11, 1));

		BasicDBList alternatives12 = new BasicDBList();
		alternatives12.add(new BasicDBObject().append("aid", 1).append("atext", "Sognsvann"));
		alternatives12.add(new BasicDBObject().append("aid", 2).append("atext", "Tyrifjorden"));
		quiz1.add(createQuestionDBObjectHelper(2, "What is the largest lake in Norway?", alternatives12, 3));

		quizzes.insert(createQuizHelper(1, "Geography Quiz", "This is a quiz about Norwegian geography", "Thank you for taking the quiz", quiz1));
	}
	
	private BasicDBObject createQuestionDBObjectHelper(int idValue, String textValue, BasicDBList alternativeValues, int answerValue){
		BasicDBObject document = new BasicDBObject();
		document.put("id", idValue);
		document.put("text", textValue);
		document.put("alternatives", alternativeValues);
		document.put("answer", answerValue);
		return document;
	}
	
	private BasicDBObject createQuizHelper(int quizId, String quizName, String quizDescription, String quizSubmittedMsg, BasicDBList questions){
		BasicDBObject quiz = new BasicDBObject();
		quiz.put("quizid", quizId);
		quiz.put("name", quizName);
		quiz.put("desc", quizDescription);
		quiz.put("submitMsg", quizSubmittedMsg);
		quiz.put("questions", questions);
		return quiz;
	}
	
	public void retrieveFromDatabaseHelper(){
		getQuizHelper(1);
		
	}

	private Quiz getQuizHelper(int quizId) {
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("quizid", quizId);
		
		DBObject quizObject = quizzes.findOne(whereQuery);
		
		String quizName = (String) quizObject.get("name");
		String quizDesc = (String) quizObject.get("desc");
		String submitMsg = (String) quizObject.get("submitMsg");
		BasicDBList questions = (BasicDBList) quizObject.get("questions");
		
		Quiz quiz = new Quiz(quizId, quizName, quizDesc, submitMsg, questions);
		return quiz;
	}
	
	
}
