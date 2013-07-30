package no.steria.quizzical;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class AdminServlet extends SecuredServlet {

	private Quiz quiz;
	private MongoQuizDao mongoQuizDao;
	private MongoUserDao mongoUserDao;
	private MongoResponseDao mongoResponseDao;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();		 
		JsonNode rootNode = mapper.readTree(req.getReader().readLine());
		
		int quizId = -1, userId = 0;
		String quizName = "", quizDesc = "", submitMsg = "";
		
		List<Question> questions = new ArrayList<Question>();
		
		Iterator<Entry<String,JsonNode>> allEntries = rootNode.getFields();
		
		while(allEntries.hasNext()){
			Entry<String, JsonNode> entry = allEntries.next();
			if(entry.getKey().equals("questions")){
				Iterator<JsonNode> questionEntries = entry.getValue().getElements();
				
				while(questionEntries.hasNext()){
					JsonNode jsonQuestion = questionEntries.next();
					Iterator<Entry<String, JsonNode>> questionFields = jsonQuestion.getFields();
					
					int questionId = 0, correctAnswer = 0;
					String questionText = "";
					
					List<Alternative> alternatives = new ArrayList<Alternative>();
					
					while(questionFields.hasNext()){
						Entry<String, JsonNode> questionField = questionFields.next();
						
						if (questionField.getKey().equals("id")){
							questionId = questionField.getValue().asInt();
						} else if (questionField.getKey().equals("text")){
							questionText = questionField.getValue().asText();
						} else if (questionField.getKey().equals("alternatives")){
							Iterator<JsonNode> jsonAlternatives = questionField.getValue().getElements();
							
							while(jsonAlternatives.hasNext()){
								JsonNode jsonAlternative = jsonAlternatives.next();
								Iterator<Entry<String, JsonNode>> alternativeField = jsonAlternative.getFields();
								
								int alternativeId = 0;
								String alternativeText = "";
								
								while(alternativeField.hasNext()){
									Entry<String, JsonNode> alternativeKeys = alternativeField.next();
									
									if(alternativeKeys.getKey().equals("aid")){
										alternativeId = alternativeKeys.getValue().asInt();
									} else if (alternativeKeys.getKey().equals("atext")){
										alternativeText = alternativeKeys.getValue().asText();
									}	
								}
								Alternative alternative = new Alternative(alternativeId, alternativeText);
								alternatives.add(alternative);
							}
						} else if (questionField.getKey().equals("answer")){
							correctAnswer = questionField.getValue().asInt();
						}
					}
					Question question = new Question(questionId, questionText, alternatives, correctAnswer);
					questions.add(question);
				}
				
			} else if(entry.getKey().equals("quizId")){
				quizId = entry.getValue().asInt();
			} else if(entry.getKey().equals("quizName")){
				quizName = entry.getValue().asText();
			} else if(entry.getKey().equals("quizDesc")){
				quizDesc = entry.getValue().asText();
			} else if(entry.getKey().equals("submitMsg")){
				submitMsg = entry.getValue().asText();
			} else if(entry.getKey().equals("userId")){
				userId = entry.getValue().asInt();
			}
		}
		quiz = new Quiz(quizId, quizName, quizDesc, submitMsg, questions, true);
		mongoQuizDao.insertQuizIntoDB(quiz, userId);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		
		ObjectMapper mapper = new ObjectMapper();
		PrintWriter writer = resp.getWriter();
		
		int mode = Integer.parseInt(req.getParameter("mode"));
		
		if(mode == 2){
			retriveQuizzesByUserId(req, resp, mapper, writer);
		}else if(mode == 3){
			retrieveNumberOfResponsesByQuizId(req, resp, mapper, writer);
		}else if(mode == 4){
			retrieveRandomWinnerOfQuiz(req, resp, mapper, writer);
		}else if(mode == 5){
			deleteQuiz(req);
		}else if(mode == 6){
			changeActiveStatusOfQuiz(req);
		}else if(mode == 7){
			retrieveListOfRespondents(req, resp, mapper, writer);
		}else if(mode == 8){
			retrieveSessionUser(req, resp, mapper, writer);
		}else if(mode == 9){
			req.getSession().invalidate();
		}
		
	}

	private void retrieveSessionUser(HttpServletRequest req,
			HttpServletResponse resp, ObjectMapper mapper, PrintWriter writer)
			throws IOException, JsonGenerationException, JsonMappingException {
		mapper.writeValue(writer, req.getSession().getAttribute("username"));
		resp.setContentType("text/json");
	}

	private void retrieveListOfRespondents(HttpServletRequest req,
			HttpServletResponse resp, ObjectMapper mapper, PrintWriter writer)
			throws IOException, JsonGenerationException, JsonMappingException {
		int quizId = Integer.parseInt(req.getParameter("quizId"));
		mapper.writeValue(writer, mongoResponseDao.getRespondents(quizId));
		resp.setContentType("text/json");
	}

	private void changeActiveStatusOfQuiz(HttpServletRequest req) {
		int quizId = Integer.parseInt(req.getParameter("quizId"));
		int userId = Integer.parseInt(req.getParameter("userId"));
		Boolean active = Boolean.parseBoolean(req.getParameter("active"));
		Quiz quiz = mongoQuizDao.getQuiz(quizId);
		quiz.setActive(active);
		mongoQuizDao.insertQuizIntoDB(quiz, userId);
	}

	private void deleteQuiz(HttpServletRequest req) {
		int quizId = Integer.parseInt(req.getParameter("quizId"));
		mongoQuizDao.remove(quizId);
	}

	private void retrieveRandomWinnerOfQuiz(HttpServletRequest req,
			HttpServletResponse resp, ObjectMapper mapper, PrintWriter writer)
			throws IOException, JsonGenerationException, JsonMappingException {
		int quizId = Integer.parseInt(req.getParameter("quizId"));
		mapper.writeValue(writer, mongoResponseDao.drawRandomWinner(quizId));
		resp.setContentType("text/json");
	}

	private void retrieveNumberOfResponsesByQuizId(HttpServletRequest req,
			HttpServletResponse resp, ObjectMapper mapper, PrintWriter writer)
			throws IOException, JsonGenerationException, JsonMappingException {
		int quizId = Integer.parseInt(req.getParameter("quizId"));
		mapper.writeValue(writer, mongoResponseDao.countResponsesForQuiz(quizId));
		resp.setContentType("text/json");
	}

	private void retriveQuizzesByUserId(HttpServletRequest req,
			HttpServletResponse resp, ObjectMapper mapper, PrintWriter writer)
			throws IOException, JsonGenerationException, JsonMappingException {
		int userId = Integer.parseInt(req.getParameter("userId"));
		List<Integer> usersQuizIds = mongoUserDao.getUser(userId).getQuizIds();
		List<Quiz> requestedQuizzes = new ArrayList<Quiz>();
		for(Integer quizId : usersQuizIds){
			Quiz quiz = mongoQuizDao.getQuiz(quizId);
			quiz.setResponses(mongoResponseDao.countResponsesForQuiz(quizId));
			requestedQuizzes.add(quiz);
		}			
		mapper.writeValue(writer, requestedQuizzes);
		resp.setContentType("text/json");
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		mongoQuizDao = new MongoQuizDao();
		mongoUserDao = new MongoUserDao();
		mongoResponseDao = new MongoResponseDao();
	}
	
	public void setMongoUserDao(MongoUserDao mongoUserDao) {
		this.mongoUserDao = mongoUserDao;
	}
	
	public void setMongoQuizDao(MongoQuizDao mongoQuizDao) {
		this.mongoQuizDao = mongoQuizDao;
	}
	
	public void setMongoResponseDao(MongoResponseDao mongoResponseDao) {
		this.mongoResponseDao = mongoResponseDao;
	}

}
