package no.steria.quizzical;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

public class QuizzicalServlet extends HttpServlet {

	private QuizDao quizDao;
	private MongoUserDao mongoUserDao;
	private MongoResponseDao mongoResponseDao;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		ObjectMapper mapper = new ObjectMapper();
		Quiz quiz = null;
		PrintWriter writer = resp.getWriter();

		System.out.println(req.getParameter("mode"));
		int mode = Integer.parseInt(req.getParameter("mode"));
		
		if(mode == 1){
			// Retrieves a quiz with quizId
			int quizId = Integer.parseInt(req.getParameter("quizId"));
			try {
				quiz = quizDao.getQuiz(quizId);
				mapper.writeValue(writer, quiz);
				resp.setContentType("text/json");				
			} catch(IllegalArgumentException e){
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.getWriter().print(e.getMessage());
			}			
		}else if(mode == 2){
			// Retrieves all quizzes by a userId
			int userId = Integer.parseInt(req.getParameter("userId"));
			ArrayList<Integer> usersQuizIds = mongoUserDao.getUser(userId).getQuizIds();
			ArrayList<Quiz> requestedQuizzes = new ArrayList<Quiz>();
			for(Integer quizId : usersQuizIds){
				requestedQuizzes.add(quizDao.getQuiz(quizId));
			}			
			mapper.writeValue(writer, requestedQuizzes);
			resp.setContentType("text/json");
		}
		else if(mode == 3){
			// Checks current number of responses. PS: This section should be moved to adminServlet soon!
			int quizId = Integer.parseInt(req.getParameter("quizId"));
			resp.getWriter().write(mongoResponseDao.countResponsesForQuiz(quizId));
			resp.setContentType("text");
		}
	}
	
	public void setQuizDao(QuizDao quizDao) {
		this.quizDao = quizDao;
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
	
		quizDao = new MongoQuizDao();
		mongoUserDao = new MongoUserDao();
		mongoResponseDao = new MongoResponseDao();
	}

}
