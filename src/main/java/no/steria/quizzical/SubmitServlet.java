package no.steria.quizzical;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class SubmitServlet extends HttpServlet {

	private MongoResponseDao mongoResponseDao;
	private Response quizResponse;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();		 
		JsonNode rootNode = mapper.readTree(req.getReader().readLine());
		
		int quizId=0; 
		String name="", email="";
		HashMap<String,String> answersToDB = new HashMap<String,String>();
		
		Iterator<Entry<String,JsonNode>> allEntries = rootNode.getFields();
		while(allEntries.hasNext()){
			Entry<String, JsonNode> entry = allEntries.next();
			if(entry.getKey().equals("answers")){
				Iterator<Entry<String,JsonNode>> answerEntries = entry.getValue().getFields();
				while(answerEntries.hasNext()){
					Entry<String,JsonNode> answer = answerEntries.next();
					answersToDB.put(answer.getKey(), answer.getValue().toString());					
				}
			}else if(entry.getKey().equals("quizId")){
				quizId = Integer.parseInt(entry.getValue().toString());
			}else if(entry.getKey().equals("name")){
				name = entry.getValue().toString();
			}else if(entry.getKey().equals("email")){
				email = entry.getValue().toString();
			}
		}
		
		quizResponse = new Response(quizId, name, email, answersToDB);
		//quizResponse.calculateScore(mongoQuizDao.getQuiz(quizResponse.getQuizId()));
		mongoResponseDao.setResponse(quizResponse);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		Map<String,String[]> immutableParameters = req.getParameterMap();
//		HashMap<String,String[]> parameters = new HashMap<String,String[]>();
//		parameters.putAll(immutableParameters);
//		int quizId = Integer.parseInt(parameters.remove("quizId")[0]);
//		String name = parameters.remove("name")[0];
//		String email = parameters.remove("email")[0];
//		
//		Enumeration<String> allParameterNames = req.getParameterNames();
//		while(allParameterNames.hasMoreElements()){
//			String element = allParameterNames.nextElement();
//			if(!element.matches("^q[1-9][0-9]*$")){				
//				parameters.remove(element);
//			}
//		}
//					
//		quizResponse = new Response(quizId, name, email, parameters);
//		quizResponse.calculateScore(mongoQuizDao.getQuiz(quizResponse.getQuizId()));
//		mongoResponseDao.setResponse(quizResponse);		
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		mongoResponseDao = new MongoResponseDao();
	}

}
