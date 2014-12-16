package no.steria.quizzical;

import com.mongodb.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MongoResponseDao{
		
	private DB db;
	private DBCollection collection;
	private Random random = new Random();

	public MongoResponseDao() {
		db = MongoConnection.getConnection();
		collection = db.getCollection("responses");
	}

	public void setResponse(Response response) {		
		BasicDBObject document = new BasicDBObject();
		document.put("quizId", response.getQuizId());
		document.put("name", response.getName());
		document.put("email", response.getEmail());
		document.put("company", response.getCompany());
		document.put("phoneNumber", response.getPhoneNumber());
		document.put("score", response.getScore());
		collection.insert(document);
	}
	
	public int countResponsesForQuiz(int quizId){		
		DBCursor cursor = collection.find(new BasicDBObject("quizId",quizId));
		return cursor.count();
	}

	public Response drawRandomWinner(int quizId) {
		DBCursor cursor = collection.find(new BasicDBObject("quizId", quizId)).sort(new BasicDBObject("score", -1));
		Integer maxScore = null;
		List<Response> bestResponses = new ArrayList<Response>();

		while(cursor.hasNext()){
			DBObject document = cursor.next();

			int score = (Integer) document.get("score");

			if(maxScore == null || score == maxScore) {
				Response response = getResponse(document);
				bestResponses.add(response);
				maxScore = score;
			}
			else {
				break;
			}
		}

		if(bestResponses.isEmpty()) {
			return null;
		}

		return bestResponses.get(random.nextInt(bestResponses.size()));
	}
	
	public List<Response> getRespondents(int quizId){
		List<Response> responses = new ArrayList<Response>();
		DBCursor cursor = collection.find(new BasicDBObject("quizId", quizId));
		
		while(cursor.hasNext()){
			DBObject document = cursor.next();
			Response response = getResponse(document);
			responses.add(response);
		}
		return responses;
	}

	private Response getResponse(DBObject document) {
		int quizId = (Integer) document.get("quizId");
		String name = (String) document.get("name");
		String email = (String) document.get("email");
		String company = (String) document.get("company");
		String phoneNumber = (String) document.get("phoneNumber");
		int score = (Integer) document.get("score");
		String id = document.get("_id").toString();

		Response response = new Response(quizId, name, email, company, phoneNumber, null, id);
		response.setScore(score);
		return response;
	}

	public Response getById(String id) {
		if(id == null) {
			return null;
		}

		DBObject document = collection.findOne(new BasicDBObject("_id", new ObjectId(id)));

		if(document == null) {
			return null;
		}

		return getResponse(document);
	}
}
