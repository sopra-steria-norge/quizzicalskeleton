package no.steria.quizzicale;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebServer {

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		server.setHandler(new WebAppContext("src/main/webapp", "/"));
		server.start();
		System.out.println("http://localhost:8080/");
	}

}
