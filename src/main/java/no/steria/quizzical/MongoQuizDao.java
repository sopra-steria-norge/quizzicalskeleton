package no.steria.quizzical;

import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoQuizDao implements QuizDao {
	
	private DB db;
	private DBCollection collection;
	
	public MongoQuizDao() {
		try {
			MongoClient client = new MongoClient();
			db = client.getDB("quizzical");
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		collection = db.getCollection("quizzes");
	}
	
	@Override
	public ArrayList<Quiz> getQuizzes() {
		DBCursor cursor = collection.find();
		ArrayList<Quiz> quizzes = new ArrayList<Quiz>();
		while(cursor.hasNext()){
			DBObject next = cursor.next();
			Integer quizId = (Integer) next.get("quizId");
			String quizName = (String) next.get("name");
			String quizDesc = (String) next.get("desc");
			String submitMsg = (String) next.get("submitMsg");
			
			BasicDBList questions = (BasicDBList) next.get("questions");
			
			Quiz quiz = new Quiz(quizId, quizName, quizDesc, submitMsg, questions);
			quizzes.add(quiz);
		}
		
		return quizzes;
	}

	@Override
	public Quiz getQuiz(int quizId) {
		DBObject quizObject = collection.findOne(new BasicDBObject("quizId", quizId));
		
		if (quizObject == null){
			throw new IllegalArgumentException("The requested quiz (quizId=" + quizId + ") is not available.");
		}
		
		String quizName = (String) quizObject.get("name");
		String quizDesc = (String) quizObject.get("desc");
		String submitMsg = (String) quizObject.get("submitMsg");
		BasicDBList questions = (BasicDBList) quizObject.get("questions");
		
		Quiz quiz = new Quiz(quizId, quizName, quizDesc, submitMsg, questions);
		return quiz;
	}

	@Override
	public void insertQuizToDB(Quiz quiz) {
		int quizId = quiz.getQuizId();
		BasicDBObject document = new BasicDBObject();
		
		if (quizId == -1){
			quizId = getAvailableQuizId();
		} else {
			remove(quizId);
		}
		document.put("quizId", quizId);
		document.put("name", quiz.getQuizName());
		document.put("desc", quiz.getQuizDesc());
		document.put("submitMsg", quiz.getSubmitMsg());
		document.put("questions", quiz.getQuestions());
		collection.insert(document);
	}
	
	private int getAvailableQuizId(){
		int index = 1;
		DBCursor cursor = collection.find();
		while (cursor.hasNext()){
			DBObject dbo = cursor.next();	
			if (dbo.containsField("quizId")){
				int currentIndex = ((Integer) dbo.get("quizId")).intValue();
				if (currentIndex >= index){
					index = currentIndex + 1;
				}
			}
		}
		return index;
	}
	
	public void remove(int quizId){
		DBCursor cursor = collection.find(new BasicDBObject("quizId", quizId));
		if (cursor.hasNext()){
			collection.remove(cursor.next());
		}
		MongoUserDao.removeQuizIdFromUsers(quizId);
		
	}
			
}