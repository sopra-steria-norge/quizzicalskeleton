package no.steria.quizzical;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoResponseDao{
		
	private DB db;
	private DBCollection collection;
	
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
	
	public String[] drawRandomWinner(int quizId){
		DBCursor cursor = collection.find(new BasicDBObject("quizId",quizId));
		Random rand = new Random();
		int length = cursor.count();
		String[] winner = new String[2];
		if(length>0){
			cursor.skip(rand.nextInt(length));
			DBObject next = cursor.next();
			winner[0] = (String) next.get("name");
			winner[1] = (String) next.get("email");
		}else{
			winner[0] = "No responses yet!";
			winner[1] = "";
		}
		return winner;
	}
	
	public List<Response> getRespondents(int quizId){
		List<Response> responses = new ArrayList<Response>();
		DBCursor cursor = collection.find(new BasicDBObject("quizId", quizId));
		
		while(cursor.hasNext()){
			DBObject document = cursor.next();
			
			String name = (String) document.get("name");
			String email = (String) document.get("email");
			String company = (String) document.get("company");
			String phoneNumber = (String) document.get("phoneNumber");
			int score = (Integer) document.get("score");
			
			Response response = new Response(quizId, name, email, company, phoneNumber, null);
			response.setScore(score);
			responses.add(response);
		}
		return responses;
	}
	
}
