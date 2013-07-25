package no.steria.quizzical;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		writer
		.append("<html>") //
		.append("<body>")
		.append("<form action='login' method='POST'>")
		.append("User <input type='text' name='user'/><br/>")
		.append("Password <input type='password' name='password'/><br/>")
		.append("<input type='submit' name='loginButton' value='Login'/>")
		.append("</form></body></html>");
		;
	}
}
