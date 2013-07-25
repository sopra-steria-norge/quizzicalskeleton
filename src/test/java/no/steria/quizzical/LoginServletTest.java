package no.steria.quizzical;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentHelper;
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
}
