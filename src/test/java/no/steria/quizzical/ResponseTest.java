package no.steria.quizzical;

import static org.fest.assertions.Assertions.assertThat;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ResponseTest {
	
	private HashMap<String, Integer> quizUserAnswers;
	private Quiz quiz;
	private Response response = new Response(1, "Ola", "o@example.com", quizUserAnswers);
	
	public ResponseTest() {
		quiz = MongoDemo.testQuiz1();
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
}
