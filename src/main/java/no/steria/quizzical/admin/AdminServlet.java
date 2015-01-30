package no.steria.quizzical.admin;

import no.steria.quizzical.*;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;

public class AdminServlet extends SecuredServlet {

	private Quiz quiz;
	private MongoQuizDao mongoQuizDao;
	private MongoUserDao mongoUserDao;
	private MongoResponseDao mongoResponseDao;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();		 
		JsonNode rootNode = mapper.readTree(req.getReader().readLine());
		
		int quizId = -1;
		int questionNum = 0;
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
					questionNum++;
					
					List<Alternative> alternatives = new ArrayList<Alternative>();
					boolean answerInText = jsonQuestion.get("answerInText") != null ? jsonQuestion.get("answerInText").asBoolean() : false;

					while(questionFields.hasNext()){
						Entry<String, JsonNode> questionField = questionFields.next();
						
						if (questionField.getKey().equals("id")){
							questionId = questionField.getValue().asInt();
						} else if (questionField.getKey().equals("text")){
							questionText = questionField.getValue().asText();
						} else if (questionField.getKey().equals("alternatives")){
							Iterator<JsonNode> jsonAlternatives = questionField.getValue().getElements();

							int alt = 0;

							while(jsonAlternatives.hasNext()){
								alt++;

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

								for(int i = 0; i < alternatives.size(); i++) {
									Alternative alternative = alternatives.get(i);
									if(StringUtils.equals(alternativeText, alternative.getAtext())) {
										sendErrorMsg(resp, "Alternative #" + alt + " and #" + (i + 1) + " of question #" + questionNum + " is the same.");
										return;
									}
								}

								Alternative alternative = new Alternative(alternativeId, alternativeText);
								alternatives.add(alternative);
							}
						} else if (questionField.getKey().equals("answer")){
							correctAnswer = questionField.getValue().asInt();
						}
					}

					if(correctAnswer == 0 && !answerInText) {
						sendErrorMsg(resp, "Please set a correct answer to question #" + questionNum);
						return;
					}

					Question question = new Question(questionId, questionText, alternatives, correctAnswer, answerInText);
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
			}
		}

		Integer language = rootNode.get("language") != null && rootNode.get("language").get("id") != null ? rootNode.get("language").get("id").asInt() : null;
		boolean showAnswer = rootNode.get("showAnswer") != null && rootNode.get("showAnswer").asBoolean();
		boolean duplicate = rootNode.get("duplicate") != null && rootNode.get("duplicate").asBoolean();

		//Saving duplicated quiz
		if(duplicate) {
			quiz = mongoQuizDao.getQuiz(quizId);
			quiz.setQuizId(-1);
			quiz.setQuizName(quizName);
			quiz.setQuizDesc(quizDesc);
			quiz.setSubmitMsg(submitMsg);
			quiz.setQuestions(questions);
			quiz.setLanguage(language);
			quiz.setShowAnswer(showAnswer);
			quiz.setWinner(null);
		}
		//Editing existing quiz
		else if(quizId != -1) {
			quiz = mongoQuizDao.getQuiz(quizId);
			quiz.setQuizName(quizName);
			quiz.setQuizDesc(quizDesc);
			quiz.setSubmitMsg(submitMsg);
			quiz.setQuestions(questions);
			quiz.setLanguage(language);
			quiz.setShowAnswer(showAnswer);
		}
		//New quiz
		else {
			quiz = new Quiz(quizId, quizName, quizDesc, submitMsg, questions, language, showAnswer, true, null);
		}

		String username = (String) req.getSession().getAttribute("username");
		mongoQuizDao.insertQuizIntoDB(quiz, mongoUserDao.getUser(username).getUserId());
	}

	private void sendErrorMsg(HttpServletResponse resp, String msg) throws IOException {
		resp.setContentType("text/json");
		ObjectMapper mapper = new ObjectMapper();
		PrintWriter writer = resp.getWriter();

		Map<String, String> msgMap = new HashMap<>();
		msgMap.put("errorMsg", msg);

		mapper.writeValue(writer, msgMap);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		
		ObjectMapper mapper = new ObjectMapper();
		PrintWriter writer = resp.getWriter();
		
		int mode = Integer.parseInt(req.getParameter("mode"));
		
		if(mode == 2){
			retrieveQuizzesByUserId(req, resp, mapper, writer);
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
		}else if(mode == 12){
			retriveQuizzes(req, resp, mapper, writer);
		}
		
	}

	private void retrieveSessionUser(HttpServletRequest req,
			HttpServletResponse resp, ObjectMapper mapper, PrintWriter writer)
			throws IOException, JsonGenerationException, JsonMappingException {
		Map<String, String> user = new HashMap<String, String>();
		String username = (String) req.getSession().getAttribute("username");
		user.put("username", username);
		user.put("userId", ""+mongoUserDao.getUser(username).getUserId());
		mapper.writeValue(writer, user);
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
		int userId = mongoUserDao.getUser((String) req.getSession().getAttribute("username")).getUserId();
		Boolean active = Boolean.parseBoolean(req.getParameter("active"));
		Quiz quiz = mongoQuizDao.getQuiz(quizId);
		quiz.setActive(active);
		mongoQuizDao.insertQuizIntoDB(quiz, userId);
	}

	private void deleteQuiz(HttpServletRequest req) {
		int quizId = Integer.parseInt(req.getParameter("quizId"));
		mongoQuizDao.remove(quizId);
		mongoResponseDao.removeQuizResponses(quizId);
	}

	private void retrieveRandomWinnerOfQuiz(HttpServletRequest req,
			HttpServletResponse resp, ObjectMapper mapper, PrintWriter writer)
			throws IOException, JsonGenerationException, JsonMappingException {
		int quizId = Integer.parseInt(req.getParameter("quizId"));

		Response response = mongoResponseDao.drawRandomWinner(quizId);
		mapper.writeValue(writer, response);
		resp.setContentType("text/json");

		mongoQuizDao.setWinner(quizId, response.getId());
	}

	private void retrieveNumberOfResponsesByQuizId(HttpServletRequest req,
			HttpServletResponse resp, ObjectMapper mapper, PrintWriter writer)
			throws IOException, JsonGenerationException, JsonMappingException {
		int quizId = Integer.parseInt(req.getParameter("quizId"));
		mapper.writeValue(writer, mongoResponseDao.countResponsesForQuiz(quizId));
		resp.setContentType("text/json");
	}

	private void retrieveQuizzesByUserId(HttpServletRequest req,
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
	
	private void retriveQuizzes(HttpServletRequest req,
			HttpServletResponse resp, ObjectMapper mapper, PrintWriter writer)
			throws IOException, JsonGenerationException, JsonMappingException {
		String username = (String) req.getSession().getAttribute("username");
		List<Integer> usersQuizIds = mongoUserDao.getUser(username).getQuizIds();
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
