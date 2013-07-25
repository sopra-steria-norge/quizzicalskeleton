package no.steria.quizzical;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LoginServlet extends HttpServlet {
	
	private DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("YYYYMMddHHmmss"); 
	
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
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DateTime validUntil = new DateTime().plusMinutes(30);
		HttpSession session = req.getSession();
		session.setAttribute("username", req.getParameter("user"));
		session.setAttribute("valid", dateTimeFormat.print(validUntil));
		resp.sendRedirect("#/admin");
	}
}
