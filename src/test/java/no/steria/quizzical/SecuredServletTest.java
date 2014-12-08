package no.steria.quizzical;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SecuredServletTest {
	private HttpServletRequest req = mock(HttpServletRequest.class);
	private HttpServletResponse resp = mock(HttpServletResponse.class);
	private boolean getCalled = false;
	private boolean postCalled = false;
	private DateTime currentTime = new DateTime(2013, 7, 25, 16, 5, 0);

	private SecuredServlet servlet = new SecuredServlet() {
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException ,java.io.IOException {
			getCalled = true;
		};

		protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException ,java.io.IOException {
			postCalled = true;
		};
	};
	private HttpSession mockSession = mock(HttpSession.class);


	@Test
	public void shouldReceiveForbiddenWhenNotLoggedIn() throws Exception {
		when(req.getMethod()).thenReturn("GET");

		servlet.service(req, resp);

		verify(resp).sendError(SC_UNAUTHORIZED);
		assertThat(getCalled).isFalse();
	}

	@Test
	public void shouldReceiveForbiddenWhenTimedOut() throws Exception {
		when(req.getMethod()).thenReturn("GET");
		when(mockSession.getAttribute("valid")).thenReturn(currentTime.minusMinutes(1));

		servlet.service(req, resp);

		verify(resp).sendError(SC_UNAUTHORIZED);
		assertThat(getCalled).isFalse();
	}

	@Test
	public void sholdForwardCallWhenLoggedIn() throws Exception {
		when(req.getMethod()).thenReturn("POST");
		when(mockSession.getAttribute("valid")).thenReturn(currentTime.plusMinutes(1));

		servlet.service(req, resp);

		assertThat(postCalled).isTrue();
	}


	@Before
	public void setup() throws Exception {
		when(req.getSession()).thenReturn(mockSession);
		DateTimeUtils.setCurrentMillisFixed(currentTime.getMillis());
		when(req.getMethod()).thenReturn("POST");

	}


	@After
	public void resetClock() {
		DateTimeUtils.setCurrentMillisSystem();
	}

}
