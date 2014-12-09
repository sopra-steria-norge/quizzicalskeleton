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
        
        MongoQuizDao mongoQuizDao = mock(MongoQuizDao.class);
        Quiz quiz = MongoDatabasePopulation.getInstance().testQuiz1();
        when(mongoQuizDao.getQuiz(1)).thenReturn(quiz);

        servlet.setQuizDao(mongoQuizDao);
        servlet.service(req, resp);
		
		ObjectMapper mapper = new ObjectMapper();
		Quiz receivedQuiz = mapper.readValue(htmlDoc.toString(), new TypeReference<Quiz>() {});

		assertThat(receivedQuiz.getQuizId()).isEqualTo(1);
	
		Mockito.verify(resp).setContentType("text/json");
	}	

}
