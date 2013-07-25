package no.steria.quizzical;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dom4j.DocumentHelper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginServletTest {
	
	private HttpServletRequest req = mock(HttpServletRequest.class);
	private HttpServletResponse resp = mock(HttpServletResponse.class);
	private LoginServlet servlet = new LoginServlet();
	
	
	@Test
	public void shouldDisplayLoginPage() throws Exception {
		when(req.getMethod()).thenReturn("GET");
		StringWriter htmlDoc = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(htmlDoc));
		servlet.service(req, resp);
		
		assertThat(htmlDoc.toString()) //
			.contains("<html>") //
			.contains("<body>")
			.contains("<form action='login' method='POST'")
			.contains("User <input type='text' name='user'")
			.contains("Password <input type='password' name='password'")
			.contains("<input type='submit' name='loginButton' value='Login'")
			;
		
		DocumentHelper.parseText(htmlDoc.toString());
		
	}
	
	@Test
	public void shouldLogin() throws Exception {
		when(req.getMethod()).thenReturn("POST");
		when(req.getParameter("user")).thenReturn("admin");
		when(req.getParameter("password")).thenReturn("password");
		HttpSession mockSession = mock(HttpSession.class);
		when(req.getSession()).thenReturn(mockSession);
		
		servlet.service(req, resp);
		
		verify(resp).sendRedirect("#/admin");
		verify(mockSession).setAttribute("username", "admin");
		verify(mockSession).setAttribute("valid", "20130725163500");
	}
	
	@Test
	public void shouldDenyMissingUsername() throws Exception {
		when(req.getMethod()).thenReturn("POST");
		when(req.getParameter("password")).thenReturn("password");
		HttpSession mockSession = mock(HttpSession.class);
		when(req.getSession()).thenReturn(mockSession);
		
		servlet.service(req, resp);
		
		verify(resp).sendRedirect("login");
		verify(mockSession,never()).setAttribute(anyString(), any(Object.class));
	}
	
	@Test
	public void shouldDenyIllegal() throws Exception {
		when(req.getMethod()).thenReturn("POST");
		when(req.getParameter("user")).thenReturn("admin");
		when(req.getParameter("password")).thenReturn("pasx");
		HttpSession mockSession = mock(HttpSession.class);
		when(req.getSession()).thenReturn(mockSession);
		
		servlet.service(req, resp);
		
		verify(resp).sendRedirect("login");
		verify(mockSession,never()).setAttribute(anyString(), any(Object.class));
	}

	
	@Before
	public void setFixedTime() {
		DateTimeUtils.setCurrentMillisFixed(new DateTime(2013, 7, 25, 16, 5, 0).getMillis());
	}
	
	
	@After
	public void resetClock() {
		DateTimeUtils.setCurrentMillisSystem();
	}
}
