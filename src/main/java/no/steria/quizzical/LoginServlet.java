package no.steria.quizzical;

import java.io.IOException;
import java.io.PrintWriter;

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
		PrintWriter writer = resp.getWriter();
		writer
		.append("<html>") //
		.append("<body>")
		.append("<p>Login using any user and password: 'password'</p>")
		.append("<form action='login' method='POST'>")
		.append("User <input type='text' name='user'/><br/>")
		.append("Password <input type='password' name='password'/><br/>")
		.append("<input type='submit' name='loginButton' value='Login'/>")
		.append("</form></body></html>");
		;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DateTime validUntil = new DateTime().plusMinutes(30);
		HttpSession session = req.getSession();
		String username = req.getParameter("user");
		if (username == null || username.trim().isEmpty() || !validatePassword(req)) {
			resp.sendRedirect("login");
			return;
		}
		
		session.setAttribute("username", username);
		session.setAttribute("valid", validUntil);
		resp.sendRedirect("#/admin");
	}

	private boolean validatePassword(HttpServletRequest req) {
		return "password".equals(req.getParameter("password"));
	}
}
