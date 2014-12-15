package no.steria.quizzical.admin;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import static no.steria.quizzical.admin.Constants.MIN_PASS_LENGTH;

public class AdminCreateUserServlet extends SecuredServlet {

	private MongoUserDao mongoUserDao;

	@Override
	public void init() throws ServletException {
		super.init();
		mongoUserDao = new MongoUserDao();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(req.getReader().readLine());

		Iterator<Entry<String,JsonNode>> allEntries = rootNode.getFields();

		//Check that new password is longer than limit and that they match
		String username = rootNode.get("username").asText();
		String newPass = rootNode.get("newPassword1").asText();
		String newPass2 = rootNode.get("newPassword2").asText();

		if(StringUtils.isEmpty(newPass) || newPass.length() < MIN_PASS_LENGTH) {
			sendErrorMsg(resp, "Password must be at least " + MIN_PASS_LENGTH + " characters.");
			return;
		}

		if(!newPass.equals(newPass2)) {
			sendErrorMsg(resp, "New password does not match repeated password.");
			return;
		}

		try {
			mongoUserDao.createUser(username, newPass);
		}
		catch (CreateUserException e) {
			sendErrorMsg(resp, e.getMessage());
		}
	}

	private void sendErrorMsg(HttpServletResponse resp, String msg) throws IOException {
		resp.setContentType("text/json");
		ObjectMapper mapper = new ObjectMapper();
		PrintWriter writer = resp.getWriter();

		Map<String, String> msgMap = new HashMap<>();
		msgMap.put("errorMsg", msg);

		mapper.writeValue(writer, msgMap);
	}

}
