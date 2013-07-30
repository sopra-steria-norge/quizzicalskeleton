package no.steria.quizzical;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoURI;

public class MongoConnection {
	public static DB getConnection() {
		DB db;
		String env = System.getenv("MONGOHQ_URL");
		if (env == null) {
			try {
				MongoClient client = new MongoClient();
				db = client.getDB("quizzical");
			} catch (UnknownHostException e) {
				throw new RuntimeException(e);
			}
		} else {
			MongoURI mongoURI = new MongoURI(env);
			try {
				db = mongoURI.connectDB();
				db.authenticate(mongoURI.getUsername(), mongoURI.getPassword());
			} catch (UnknownHostException e) {
				throw new RuntimeException(e);
			}
		}
		return db;
	}
}
