package no.steria.quizzical;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class MongoQuestionDao implements QuestionDao {
	
	
	private DB db;
	
	public MongoQuestionDao() {
		try {
			MongoClient client = new MongoClient();
			db = client.getDB("quizzical");
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Question> getQuestions() {
		DBCollection collection = db.getCollection("questions");
		DBCursor cursor = collection.find();
		return null;
	}

}
