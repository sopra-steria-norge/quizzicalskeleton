package no.steria.quizzical;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class AdminServletTest {
	private HttpServletRequest req = mock(HttpServletRequest.class);
	private HttpServletResponse resp = mock(HttpServletResponse.class);
	private AdminServlet servlet = new AdminServlet();


	@Test
	public void sholdRetrieveQuizzesForUser() throws Exception {
		when(req.getParameter("mode")).thenReturn("2");
		when(req.getParameter("userId")).thenReturn("1");
		MongoUserDao mongoUserDao = mock(MongoUserDao.class);
		User dummyUser = new User(1, "martin", "password", Arrays.asList(new Integer(33)));
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
	
}
