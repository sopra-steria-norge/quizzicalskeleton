package no.steria.quizzical;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class LoginServletTest {
	
	private HttpServletRequest req = mock(HttpServletRequest.class);
	private HttpServletResponse resp = mock(HttpServletResponse.class);
	private LoginServlet servlet = new LoginServlet();
	private DateTime currentTime = new DateTime(2013, 7, 25, 16, 5, 0);
	
	
//	@Ignore("Needs to be adapted for template use")
//	public void shouldDisplayLoginPage() throws Exception {
//		when(req.getMethod()).thenReturn("GET");
//		StringWriter htmlDoc = new StringWriter();
//		when(resp.getWriter()).thenReturn(new PrintWriter(htmlDoc));
//		servlet.service(req, resp);
//		
//		assertThat(htmlDoc.toString())
//			.contains("<form action='login' method='POST'")
//			.contains("<input type='text' name='user'")
//			.contains("<input type='password' name='password'")
//			.contains("<button type='submit' name='loginButton'")
//			;
//		
//		DocumentHelper.parseText(htmlDoc.toString());
//	}
	
	@Ignore("Needs to be adapted for user database")
	public void shouldLogin() throws Exception {
		when(req.getMethod()).thenReturn("POST");
		when(req.getParameter("user")).thenReturn("admin");
		when(req.getParameter("password")).thenReturn("password");
		HttpSession mockSession = mock(HttpSession.class);
		when(req.getSession()).thenReturn(mockSession);
		
		servlet.service(req, resp);
		
		verify(resp).sendRedirect("#/admin");
		verify(mockSession).setAttribute("username", "admin");
		verify(mockSession).setAttribute("valid", currentTime.plusMinutes(30));
	}
	
	@Test
	public void shouldDenyMissingUsername() throws Exception {
		when(req.getMethod()).thenReturn("POST");
		when(req.getParameter("password")).thenReturn("password");
		HttpSession mockSession = mock(HttpSession.class);
		when(req.getSession()).thenReturn(mockSession);
		
		servlet.service(req, resp);
		
		verify(resp).sendError(401);
		verify(mockSession,never()).setAttribute(anyString(), any(Object.class));
	}
	
	@Ignore("Needs to be adapted for user database")
	public void shouldDenyIllegal() throws Exception {
		when(req.getMethod()).thenReturn("POST");
		when(req.getParameter("user")).thenReturn("admin");
		when(req.getParameter("password")).thenReturn("pasx");
		HttpSession mockSession = mock(HttpSession.class);
		when(req.getSession()).thenReturn(mockSession);
		
		servlet.service(req, resp);
		
		verify(resp).sendError(401);
		verify(mockSession,never()).setAttribute(anyString(), any(Object.class));
	}

	
	@Before
	public void setFixedTime() {
		DateTimeUtils.setCurrentMillisFixed(currentTime.getMillis());
	}
	
	
	@After
	public void resetClock() {
		DateTimeUtils.setCurrentMillisSystem();
	}
}
