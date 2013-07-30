package no.steria.quizzical;


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

import org.junit.Test;
import org.mockito.Mockito;


public class AdminServletTest {
	private HttpServletRequest req = mock(HttpServletRequest.class);
	private HttpServletResponse resp = mock(HttpServletResponse.class);
	private AdminServlet servlet = new AdminServlet();


	@Test
	public void shouldRetrieveQuizzesForUser() throws Exception {
		when(req.getParameter("mode")).thenReturn("2");
		when(req.getParameter("userId")).thenReturn("1");
		MongoUserDao mongoUserDao = mock(MongoUserDao.class);
		User dummyUser = new User(1, "martin", null, null,Arrays.asList(new Integer(33)));
		when(mongoUserDao.getUser(1)).thenReturn(dummyUser );
		
		MongoQuizDao mongoQuizDao = mock(MongoQuizDao.class);
		when(mongoQuizDao.getQuiz(33)).thenReturn(new Quiz(33,"DummyQuiz","description","sub", new ArrayList<Question>(), true));
		
		MongoResponseDao mongoResponseDao = mock(MongoResponseDao.class);
		when(mongoResponseDao.countResponsesForQuiz(33)).thenReturn(43);
		
		servlet.setMongoUserDao(mongoUserDao);
		servlet.setMongoQuizDao(mongoQuizDao);
		servlet.setMongoResponseDao(mongoResponseDao);
		when(resp.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
		servlet.doGet(req, resp);
	}
	
	@Test
	public void shouldRecieveQuizFromAdmin() throws Exception {
		MongoUserDao mongoUserDao = mock(MongoUserDao.class);
		User dummyUser = new User(1, "martin", null, null,Arrays.asList(new Integer(33)));
		when(mongoUserDao.getUser(1)).thenReturn(dummyUser);
		
		MongoQuizDao mongoQuizDao = mock(MongoQuizDao.class);
		when(mongoQuizDao.getQuiz(33)).thenReturn(new Quiz(33,"DummyQuiz","description","sub", new ArrayList<Question>(), true));
		
		MongoResponseDao mongoResponseDao = mock(MongoResponseDao.class);
		when(mongoResponseDao.countResponsesForQuiz(33)).thenReturn(43);
		
		String s = "{\"quizId\": 9, \"quizName\":\"SteriaQuiz\",\"quizDesc\":\"Quiz om Steria\",\"submitMsg\":\"Takk\",\"questions\":[{\"id\":1,\"text\":\"Spm1\",\"alternatives\":[{\"aid\":1,\"atext\":\"svar1\"},{\"aid\":2,\"atext\":\"svar2\"}],\"answer\":\"2\"}], \"active\": true, \"userId\": 1}";
		BufferedReader br = new BufferedReader(new StringReader(s));
		when(req.getReader()).thenReturn(br);
		
		Alternative alt1 = new Alternative(1, "svar1");
		Alternative alt2 = new Alternative(2, "svar2");
		
		List<Alternative> alternatives = new ArrayList<Alternative>();
		alternatives.add(alt1);
		alternatives.add(alt2);
		
		Question q = new Question(1, "Spm1", alternatives, 2);
		ArrayList<Question> qs = new ArrayList<Question>();
		qs.add(q);
		
		Quiz quiz = new Quiz(9, "SteriaQuiz", "Quiz om Steria", "Takk", qs, true);
		
		servlet.setMongoUserDao(mongoUserDao);
		servlet.setMongoQuizDao(mongoQuizDao);
		servlet.setMongoResponseDao(mongoResponseDao);
		
		servlet.doPost(req, resp);
		
		Mockito.verify(mongoQuizDao).insertQuizIntoDB(quiz, 1);
	}
	
}
