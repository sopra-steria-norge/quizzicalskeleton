package no.steria.quizzical;


import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

public class QuizServletTest {
	@Test
	public void servletShouldSayHello() throws Exception {
		QuizServlet servlet = new QuizServlet();
		
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(req.getMethod()).thenReturn("GET");
		StringWriter htmlDoc = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(htmlDoc));
		servlet.service(req, resp);
		
		verify(resp).setContentType("text/html");
		assertThat(htmlDoc.toString()).contains("<h1>Hello from servlet</h1>");
	}

}
