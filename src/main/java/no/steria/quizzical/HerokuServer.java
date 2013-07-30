package no.steria.quizzical;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class HerokuServer {
	
	public static void main(String[] args) throws Exception {
		String portString = System.getenv("PORT");
		Server server = new Server(Integer.valueOf(portString == null ? "5000" : portString));
		server.setHandler(new WebAppContext("src/main/webapp", "/"));
		server.start();
	}
}
