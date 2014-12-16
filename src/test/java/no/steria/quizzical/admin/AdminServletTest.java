package no.steria.quizzical.admin;

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
import javax.servlet.http.HttpSession;

import no.steria.quizzical.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AdminServletTest {
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private AdminServlet servlet;
	private MongoUserDao mongoUserDao;

	@Before
	public void setUp(){
		req = mock(HttpServletRequest.class);
		resp = mock(HttpServletResponse.class);
		servlet = new AdminServlet();
		mongoUserDao = mock(MongoUserDao.class);
	}
	
	@Test
	public void shouldRetrieveQuizzesForUser() throws Exception {
		when(req.getParameter("mode")).thenReturn("2");
		when(req.getParameter("userId")).thenReturn("1");
		User dummyUser = new User(1, "testUser", null, null,Arrays.asList(new Integer(33)));
		when(mongoUserDao.getUser(1)).thenReturn(dummyUser);
		
		MongoQuizDao mongoQuizDao = mock(MongoQuizDao.class);
		when(mongoQuizDao.getQuiz(33)).thenReturn(new Quiz(33,"DummyQuiz","description","sub", new ArrayList<Question>(), "English", true, null));
		
		MongoResponseDao mongoResponseDao = mock(MongoResponseDao.class);
		when(mongoResponseDao.countResponsesForQuiz(33)).thenReturn(43);
		
		servlet.setMongoUserDao(mongoUserDao);
		servlet.setMongoQuizDao(mongoQuizDao);
		servlet.setMongoResponseDao(mongoResponseDao);
		when(resp.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
		servlet.doGet(req, resp);
	}

	@Test
	public void shouldRetrieveQuizzesOfCurrentUserBySession() throws Exception {
		when(req.getParameter("mode")).thenReturn("12");
		
		HttpSession mockSession = mock(HttpSession.class);
		when(req.getSession()).thenReturn(mockSession);
		when(mockSession.getAttribute("username")).thenReturn("testUser");
		
		User dummyUser = new User(1, "testUser", null, null,Arrays.asList(new Integer(33)));
		when(mongoUserDao.getUser("testUser")).thenReturn(dummyUser);
		
		MongoQuizDao mongoQuizDao = mock(MongoQuizDao.class);
		when(mongoQuizDao.getQuiz(33)).thenReturn(new Quiz(33,"DummyQuiz","description","sub", new ArrayList<Question>(), "English", true, null));
		
		MongoResponseDao mongoResponseDao = mock(MongoResponseDao.class);
		when(mongoResponseDao.countResponsesForQuiz(33)).thenReturn(43);
		
		servlet.setMongoUserDao(mongoUserDao);
		servlet.setMongoQuizDao(mongoQuizDao);
		servlet.setMongoResponseDao(mongoResponseDao);
		when(resp.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
		servlet.doGet(req, resp);
	}
	
	@Test
	public void shouldRecieveQuizFromAdminAddPage() throws Exception {
		User dummyUser = new User(1, "testUser", null, null, Arrays.asList(new Integer(33)));
		MongoQuizDao mongoQuizDao = mock(MongoQuizDao.class);
		
		String s = "{\"quizId\": -1, \"quizName\":\"SteriaQuiz\",\"quizDesc\":\"Quiz om Steria\",\"submitMsg\":\"Takk\",\"questions\":[{\"id\":1,\"text\":\"Spm1\",\"alternatives\":[{\"aid\":1,\"atext\":\"svar1\"},{\"aid\":2,\"atext\":\"svar2\"}],\"answer\":\"2\"}], \"active\": true, \"userId\": 1}";
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
		
		Quiz quiz = new Quiz(-1, "SteriaQuiz", "Quiz om Steria", "Takk", qs, "English", true, null);
		
		HttpSession mockSession = mock(HttpSession.class);
		when(req.getSession()).thenReturn(mockSession);
		when(mockSession.getAttribute("username")).thenReturn("testUser");
		when(mongoUserDao.getUser("testUser")).thenReturn(dummyUser);		
		
		servlet.setMongoUserDao(mongoUserDao);
		servlet.setMongoQuizDao(mongoQuizDao);
		servlet.doPost(req, resp);
		
		Mockito.verify(mongoQuizDao).insertQuizIntoDB(quiz, 1);
	}
	
	@Test
	public void shouldRetrieveNumberOfRespondentsOnASpecificQuiz() throws Exception {
		when(req.getParameter("mode")).thenReturn("3");
		when(req.getParameter("quizId")).thenReturn("1");
		User dummyUser = new User(1, "testUser", null, null, Arrays.asList(new Integer(33)));
		when(mongoUserDao.getUser(1)).thenReturn(dummyUser);
		
		MongoQuizDao mongoQuizDao = mock(MongoQuizDao.class);
		when(mongoQuizDao.getQuiz(33)).thenReturn(new Quiz(33,"DummyQuiz","description","sub", new ArrayList<Question>(), "English", true, null));
		
		MongoResponseDao mongoResponseDao = mock(MongoResponseDao.class);
		when(mongoResponseDao.countResponsesForQuiz(33)).thenReturn(43);
		
		servlet.setMongoUserDao(mongoUserDao);
		servlet.setMongoQuizDao(mongoQuizDao);
		servlet.setMongoResponseDao(mongoResponseDao);
		when(resp.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
		servlet.doGet(req, resp);
	}
	
}
