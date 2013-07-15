package no.steria.quizzical;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoRespondentDao{
		
	private DB db;
	
	public MongoRespondentDao() {
		try {
			MongoClient client = new MongoClient();
			db = client.getDB("quizzical");
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	public void setRespondents(Respondent respondent) {
		DBCollection collection = db.getCollection("respondents");
		BasicDBObject document = new BasicDBObject();
		document.put("quizId", respondent.getQuizId());
		BasicDBList respondentArray = new BasicDBList();
		respondentArray.add(new BasicDBObject().append("name", respondent.getName()));
		respondentArray.add(new BasicDBObject().append("email", respondent.getEmail()));
		// set score 100 for testing purposes, needs own method later
		respondentArray.add(new BasicDBObject().append("score", 100));
		collection.insert(document);
	}

}
