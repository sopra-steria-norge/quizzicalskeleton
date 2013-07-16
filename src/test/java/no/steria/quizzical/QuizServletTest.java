package no.steria.quizzical;


import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.mockito.Mockito;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

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
        
        BasicDBList alternatives = new BasicDBList();
        alternatives.add(new BasicDBObject("alt1","Oslo"));
		when(questionDao.getQuestions()).thenReturn(Arrays.asList(new Question(1,"The question",alternatives)));
		servlet.setQuestionDao(questionDao);
		
		servlet.service(req, resp);
		
		ObjectMapper mapper = new ObjectMapper();
		List<Question> receivedQuestions = mapper.readValue(htmlDoc.toString(), new TypeReference<List<Question>>() {});
		
		assertThat(receivedQuestions).hasSize(1);
		assertThat(receivedQuestions.get(0).getId()).isEqualTo(1);
		assertThat(receivedQuestions.get(0).getText()).isEqualTo("The question");
	
		Mockito.verify(resp).setContentType("text/json");
	}

}
