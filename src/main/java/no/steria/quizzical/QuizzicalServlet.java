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
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		ObjectMapper mapper = new ObjectMapper();
		Quiz quiz = null;
		PrintWriter writer = resp.getWriter();
		
		int mode = Integer.parseInt(req.getParameter("mode"));
		if(mode == 1){
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
			int userId = Integer.parseInt(req.getParameter("userId"));
			ArrayList<Integer> quizIds = mongoUserDao.getUser(userId).getQuizIds();
			mapper.writeValue(writer, quizIds);
			resp.setContentType("text/json");
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
	}

}
