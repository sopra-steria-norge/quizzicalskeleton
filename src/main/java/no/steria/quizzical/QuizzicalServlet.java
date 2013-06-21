package no.steria.quizzical;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

public class QuizzicalServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
	    
	    ObjectMapper mapper = new ObjectMapper();
	    AddRequest addRequest = mapper.readValue(stringify(req), AddRequest.class);
	    AddResult addResult = addNumbers(addRequest);
	    String jsonResult = mapper.writeValueAsString(addResult);
	    
	    resp.setContentType("text/json");
	    resp.getWriter().append(jsonResult);
	}

    private AddResult addNumbers(AddRequest addRequest) {
        AddResult addResult = new AddResult(addRequest.getOne() + addRequest.getTwo());
        return addResult;
    }

    private String stringify(HttpServletRequest req)
            throws IOException {
        BufferedReader reader = req.getReader();
	    StringBuilder sb = new StringBuilder();
	    
	    for (String line=reader.readLine();line!=null;line=reader.readLine()) {
	        sb.append(line);
	    }
	    
        return sb.toString();
    }
}
