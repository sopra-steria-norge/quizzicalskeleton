package no.steria.quizzical.admin;

import org.joda.time.DateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class LoginServlet extends HttpServlet {

	private MongoUserDao mongoUserDao;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DateTime validUntil = new DateTime().plusMinutes(30);
		HttpSession session = req.getSession();
		String username = req.getParameter("user");
		String password = req.getParameter("password");

		if (!mongoUserDao.validatePassword(username, password)) {
			resp.sendRedirect("#/?loginFailed=true");
		}else{
			session.setAttribute("username", username);
			session.setAttribute("valid", validUntil);
			resp.sendRedirect("#/admin");			
		}
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		mongoUserDao = new MongoUserDao();
	}
	
	public void setMongoUserDao(MongoUserDao mongoUserDao) {
		this.mongoUserDao = mongoUserDao;
	}
}
