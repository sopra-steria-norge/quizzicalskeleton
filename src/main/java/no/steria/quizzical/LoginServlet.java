package no.steria.quizzical;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;

public class LoginServlet extends HttpServlet {
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
//		PrintWriter writer = resp.getWriter();
//		writer
//		.append("<html>") //
//		.append("<body>")
//		.append("<div class='container'>")
//		.append("<h2 class='form-signin-heading'>Login using any user and password: 'password'</h2>")
//		.append("<form action='login' method='POST'>")
//		.append("<input type='text' name='user' class='input-block-level' placeholder='Username'/>")
//		.append("<input type='password' name='password' class='input-block-level' placeholder='Password'/>")
//		.append("<button type='submit' name='loginButton' class='bt btn-large btn-primary'>Login</button>")
//		.append("</form></div></body></html>");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DateTime validUntil = new DateTime().plusMinutes(30);
		HttpSession session = req.getSession();
		String username = req.getParameter("user");
		if (username == null || username.trim().isEmpty() || !validatePassword(req)) {
			resp.sendRedirect("login");
		}else{
			session.setAttribute("username", username);
			session.setAttribute("valid", validUntil);
			resp.sendRedirect("#/admin");			
		}
	}

	private boolean validatePassword(HttpServletRequest req) {
		return "password".equals(req.getParameter("password"));
	}
}
