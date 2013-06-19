package no.steria.quizzical;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class QuizServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
	    String res = readJsonRequest(req);
	    
	    Gson gson = new Gson();
	    AddRequest addRequest = gson.fromJson(res, AddRequest.class);
	    AddResult addResult = addNumbers(addRequest);
	    
	    String jsonResult = gson.toJson(addResult);
	    
	    resp.setContentType("text/json");
	    resp.getWriter().append(jsonResult);
	}

    private AddResult addNumbers(AddRequest addRequest) {
        AddResult addResult = new AddResult(addRequest.getOne() + addRequest.getTwo());
        return addResult;
    }

    private String readJsonRequest(HttpServletRequest req)
            throws IOException {
        BufferedReader reader = req.getReader();
	    StringBuilder res = new StringBuilder();
	    for (String line=reader.readLine();line!=null;line=reader.readLine()) {
	        res.append(line);
	    }
        return res.toString();
    }
}
