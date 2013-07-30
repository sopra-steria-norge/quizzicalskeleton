package no.steria.quizzical;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StatusServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		writer.append("<html><title>Quizzical status debug</title><body>");
		writer.append("<h1>Enviroment</h1>");
		Properties properties = System.getProperties();
		writer.append("<ul>");
		for (Entry<Object, Object> entry : properties.entrySet()) {
			writer.append("<li>" + entry.getKey() + " = '" + entry.getValue() + "'</li>");
		}
		writer.append("</ul></body></html>");
	}
}
