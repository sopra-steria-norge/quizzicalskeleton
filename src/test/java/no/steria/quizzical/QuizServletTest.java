package no.steria.quizzical;


import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

public class QuizServletTest {
	
	private QuizzicalServlet servlet = new QuizzicalServlet();
	private HttpServletRequest req = mock(HttpServletRequest.class);
	private HttpServletResponse resp = mock(HttpServletResponse.class);

	@Test
    public void shouldAddNumbers() throws Exception {
		when(req.getMethod()).thenReturn("POST");
        when(req.getReader()).thenReturn(new BufferedReader(new StringReader("{\"one\":3,\"two\":4}")));
        
        StringWriter htmlDoc = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(htmlDoc));
    
        servlet.service(req, resp);
        
        assertThat(htmlDoc.toString()).isEqualTo("{\"sum\":7}");
    }
	
	@Test
	public void shouldReturnQuestionsWithAlternatives() throws Exception {
		when(req.getMethod()).thenReturn("GET");
		when(req.getPathInfo()).thenReturn("/getQuizQuestions");
		
		StringWriter htmlDoc = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(htmlDoc));
        
        QuestionDao questionDao = mock(QuestionDao.class);
        when(questionDao.getQuestions()).thenReturn(Arrays.asList(new Question(1,"The question")));
		servlet.setQuestionDao(questionDao);
		
		servlet.service(req, resp);
		
		StringBuilder expectedResponse = new StringBuilder();
		expectedResponse.append("{\"questions\":[");
		expectedResponse.append("{\"questionId\":1, \"text\":\"The question\"}");
		expectedResponse.append("]}");
		
		assertThat(htmlDoc.toString()).isEqualTo(expectedResponse.toString());

		
		
	}

}
