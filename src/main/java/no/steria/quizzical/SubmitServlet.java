package no.steria.quizzical;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remove;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

public class SubmitServlet extends HttpServlet {

	private MongoResponseDao mongoRespondentDao;
	private Response quizResponse;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Map<String,String[]> immutableParameters = req.getParameterMap();
		HashMap<String,String[]> parameters = new HashMap<String,String[]>();
		parameters.putAll(immutableParameters);
		int quizId = Integer.parseInt(parameters.remove("quizId")[0]);
		String name = parameters.remove("name")[0];
		String email = parameters.remove("email")[0];
		
		Enumeration<String> allParameterNames = req.getParameterNames();
		while(allParameterNames.hasMoreElements()){
			String element = allParameterNames.nextElement();
			if(!element.matches("^q[1-9][0-9]*$")){				
				parameters.remove(element);
			}
		}
								
		quizResponse = new Response(quizId, name, email, parameters);
		mongoRespondentDao.setResponse(quizResponse);
		
	}
	
	private String stringify(HttpServletRequest req) throws IOException {
		BufferedReader reader = req.getReader();
		StringBuilder sb = new StringBuilder();

		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			sb.append(line);
		}
		
		return sb.toString();
	}
	
	@Override
	public void init() throws ServletException {
		super.init();

		mongoRespondentDao = new MongoResponseDao();
	}

}
