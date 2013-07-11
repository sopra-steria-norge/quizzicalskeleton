package no.steria.quizzical;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

public class QuizzicalServlet extends HttpServlet {

	private QuestionDao questionDao;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		IntHolder intHolder = mapper.readValue(stringify(req), IntHolder.class);
		Sum sum = new Sum(intHolder.getOne() + intHolder.getTwo());
		String jsonResult = mapper.writeValueAsString(sum);
		resp.setContentType("text/json");
		resp.getWriter().append(jsonResult);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<Question> questions = questionDao.getQuestions();
		PrintWriter writer = resp.getWriter();
		
		mapper.writeValue(writer, questions);
	}
	
	private String stringify(HttpServletRequest req) throws IOException {
		BufferedReader reader = req.getReader();
		StringBuilder sb = new StringBuilder();

		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			sb.append(line);
		}
		
		return sb.toString();
	}

	public void setQuestionDao(QuestionDao questionDao) {
		this.questionDao = questionDao;
		
	}
}
