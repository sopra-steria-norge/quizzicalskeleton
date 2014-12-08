package no.steria.quizzical;

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
		if (!validatePassword(req)) {
			resp.sendRedirect("#/?loginFailed=true");
		}else{
			session.setAttribute("username", username);
			session.setAttribute("valid", validUntil);
			resp.sendRedirect("#/admin");			
		}
	}

	private boolean validatePassword(HttpServletRequest req) {
		User user = null;
		Boolean validated = false;
		user = mongoUserDao.getUser(req.getParameter("user"));
//		validated = user != null && user.getPassword().equals(req.getParameter("password"));
		try {
			validated = user != null && user.getSalt() != null && new PasswordUtil().authenticate(
					req.getParameter("password"), user.getEncryptedPassword(), user.getSalt());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			validated = false;
		}
		return validated;

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
