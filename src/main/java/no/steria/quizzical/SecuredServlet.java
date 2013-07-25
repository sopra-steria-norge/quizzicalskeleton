package no.steria.quizzical;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;

public abstract class SecuredServlet extends HttpServlet {
	@Override
	protected final void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		DateTime validUntil = (DateTime) session.getAttribute("valid");
		if (validUntil == null || validUntil.isBeforeNow()) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		super.service(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}
}
