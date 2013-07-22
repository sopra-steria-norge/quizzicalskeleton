package no.steria.quizzical;

import java.util.HashMap;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class ResponseTest {
	
	private HashMap<String, Integer> quizUserAnswers;
	private Quiz quiz;
	private Response response = new Response(1, "Ola", "o@example.com", quizUserAnswers);
	
	public ResponseTest() {
		quiz = new Quiz(1, "GeoQuiz", "Geo", "GeoQuiz", createQuiz());
		quizUserAnswers = new HashMap<String, Integer>();
	}
	
	@Test
	public void noCorrectAnswers(){
		quizUserAnswers.put("q1", 2);
		quizUserAnswers.put("q2", 4);
		response = new Response(1, "Ola", "o@example.com", quizUserAnswers);
		response.calculateScore(quiz);
		assertThat(response.getScore()).isEqualTo(0);
	}
	
	@Test
	public void oneCorrectAnswer(){
		quizUserAnswers.put("q1", 2);
		quizUserAnswers.put("q2", 3);
		response = new Response(1, "Ola", "o@example.com", quizUserAnswers);
		response.calculateScore(quiz);
		assertThat(response.getScore()).isEqualTo(1);
	}
	
	@Test
	public void twoCorrectAnswers(){
		quizUserAnswers.put("q1", 1);
		quizUserAnswers.put("q2", 3);
		response = new Response(1, "Ola", "o@example.com", quizUserAnswers);
		response.calculateScore(quiz);
		assertThat(response.getScore()).isEqualTo(2);
	}
	
	private BasicDBList createQuiz(){
		BasicDBList quiz1 = new BasicDBList();
		
		BasicDBList alternatives11 = new BasicDBList();
		alternatives11.add(new BasicDBObject().append("aid", 1).append("atext", "Oslo"));
		alternatives11.add(new BasicDBObject().append("aid", 2).append("atext", "Bergen"));
		quiz1.add(createQuestionDBObject(1, "What is the capital of Norway?", alternatives11, 1));

		BasicDBList alternatives12 = new BasicDBList();
		alternatives12.add(new BasicDBObject().append("aid", 1).append("atext", "Sognsvann"));
		alternatives12.add(new BasicDBObject().append("aid", 2).append("atext", "Tyrifjorden"));
		quiz1.add(createQuestionDBObject(2, "What is the largest lake in Norway?", alternatives12, 3));

		return quiz1;
	}
	private static BasicDBObject createQuestionDBObject(int idValue, String textValue, BasicDBList alternativeValues, int answerValue){
		BasicDBObject document = new BasicDBObject();
		document.put("id", idValue);
		document.put("text", textValue);
		document.put("alternatives", alternativeValues);
		document.put("answer", answerValue);
		return document;
	}
}
