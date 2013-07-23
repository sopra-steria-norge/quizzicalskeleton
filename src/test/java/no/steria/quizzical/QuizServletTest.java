package no.steria.quizzical;


import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.mockito.Mockito;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class QuizServletTest {
	
	private QuizServlet servlet = new QuizServlet();
	private HttpServletRequest req = mock(HttpServletRequest.class);
	private HttpServletResponse resp = mock(HttpServletResponse.class);
	
	@Test
	public void shouldAskForQuiz() throws Exception {
		when(req.getMethod()).thenReturn("GET");
		when(req.getPathInfo()).thenReturn("/getQuizQuestions");
		when(req.getParameter("quizId")).thenReturn("1");
		when(req.getParameter("mode")).thenReturn("1");
		
		StringWriter htmlDoc = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(htmlDoc));
        
        QuizDao quizDao = mock(QuizDao.class);
        Quiz quiz = new Quiz(1, "Geography Quiz", "This is a quiz about Norwegian geography", "Thank you for taking the quiz", createQuiz());
        when(quizDao.getQuiz(1)).thenReturn(quiz);

        servlet.setQuizDao(quizDao);
        servlet.service(req, resp);
		
		ObjectMapper mapper = new ObjectMapper();
		Quiz receivedQuiz = mapper.readValue(htmlDoc.toString(), new TypeReference<Quiz>() {});

		assertThat(receivedQuiz.getQuizId()).isEqualTo(1);
	
		Mockito.verify(resp).setContentType("text/json");
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
