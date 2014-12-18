package no.steria.quizzical;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import no.steria.quizzical.admin.MongoUserDao;

public class MongoQuizDao {

	private DB db;
	private DBCollection collection;
	private MongoUserDao mongoUserDao;
	private MongoResponseDao mongoResponseDao;
	private static final Integer DEFAULT_LANG = 0;
	private static final String QUIZ_ID = "quizId";
	private static final String WINNER_ID = "winnerId";

	public MongoQuizDao() {
		mongoUserDao = new MongoUserDao();
		mongoResponseDao = new MongoResponseDao();

		db = MongoConnection.getConnection();

		collection = db.getCollection("quizzes");
	}

	public Quiz getQuiz(int quizId) {
		DBObject quizObject = collection.findOne(new BasicDBObject(QUIZ_ID, quizId));

		if (quizObject == null) {
			throw new IllegalArgumentException("The requested quiz (quizId="
					+ quizId + ") is not available.");
		}

		String quizName = (String) quizObject.get("name");
		String quizDesc = (String) quizObject.get("desc");
		String submitMsg = (String) quizObject.get("submitMsg");
		BasicDBList questions = (BasicDBList) quizObject.get("questions");

		Integer language;
		try {
			language = quizObject.get("language") != null ? (Integer) quizObject.get("language") : DEFAULT_LANG;
		}
		catch (Exception e) {
			language = DEFAULT_LANG;
		}
		boolean active = (boolean) quizObject.get("active");

		String winnerId = (String) quizObject.get(WINNER_ID);
		Response winner = mongoResponseDao.getById(winnerId);

		Quiz quiz = new Quiz(quizId, quizName, quizDesc, submitMsg,
				createQuestionObject(questions), language, active, winner);
		return quiz;
	}

	public List<Question> createQuestionObject(BasicDBList questionsBasicDBList) {
		List<Question> questionsList = new ArrayList<Question>();
		Iterator<Object> questionsIterator = questionsBasicDBList.iterator();
		while (questionsIterator.hasNext()) {
			BasicDBObject questionBasicDBObject = (BasicDBObject) questionsIterator
					.next();
			int id = (int) questionBasicDBObject.get("id");
			String text = (String) questionBasicDBObject.get("text");
			BasicDBList alternativesBasicDBList = (BasicDBList) questionBasicDBObject
					.get("alternatives");
			int answer = (int) questionBasicDBObject.get("answer");

			List<Alternative> alternativesList = new ArrayList<Alternative>();
			Iterator<Object> alternativesIterator = alternativesBasicDBList
					.iterator();
			while (alternativesIterator.hasNext()) {
				BasicDBObject alternativeBasicDBObject = (BasicDBObject) alternativesIterator
						.next();
				int aid = (int) alternativeBasicDBObject.get("aid");
				String atext = (String) alternativeBasicDBObject.get("atext");
				Alternative alternative = new Alternative(aid, atext);
				alternativesList.add(alternative);
			}
			Question question = new Question(id, text, alternativesList, answer);
			questionsList.add(question);
		}
		return questionsList;
	}

	public void insertQuizIntoDB(Quiz quiz, int userId) {
		if (quiz.getQuizId() == -1) {
			quiz.setQuizId(getAvailableQuizId());
		} else {
			remove(quiz.getQuizId());
		}
		BasicDBObject quizToDB = new BasicDBObject();
		quizToDB.put(QUIZ_ID, quiz.getQuizId());
		quizToDB.put("name", quiz.getQuizName());
		quizToDB.put("desc", quiz.getQuizDesc());
		quizToDB.put("submitMsg", quiz.getSubmitMsg());
		BasicDBList questionsToDB = new BasicDBList();
		for (Question questionData : quiz.getQuestions()) {
			BasicDBObject questionToDB = new BasicDBObject();
			questionToDB.put("id", questionData.getId());
			questionToDB.put("text", questionData.getText());
			questionToDB.put("answer", questionData.getAnswer());
			BasicDBList alternativesToDB = new BasicDBList();
			for (Alternative alternativeData : questionData.getAlternatives()) {
				alternativesToDB.add(new BasicDBObject().append("aid",
						alternativeData.getAid()).append("atext",
						alternativeData.getAtext()));
			}
			questionToDB.put("alternatives", alternativesToDB);
			questionsToDB.add(questionToDB);
		}
		quizToDB.put("questions", questionsToDB);
		quizToDB.put("language", quiz.getLanguage());
		quizToDB.put("active", quiz.getActive());
		collection.insert(quizToDB);
		mongoUserDao.addQuizIdToUser(quiz.getQuizId(), userId);
	}

	private int getAvailableQuizId() {
		int index = 1;
		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			if (dbo.containsField(QUIZ_ID)) {
				int currentIndex = ((Integer) dbo.get(QUIZ_ID)).intValue();
				if (currentIndex >= index) {
					index = currentIndex + 1;
				}
			}
		}
		return index;
	}

	public void remove(int quizId) {
		DBCursor cursor = collection.find(new BasicDBObject(QUIZ_ID, quizId));
		if (cursor.hasNext()) {
			collection.remove(cursor.next());
		}
		mongoUserDao.removeQuizIdFromUsers(quizId);

	}

	public void setWinner(int quizId, String winnerId) {
		try {
			DBObject quizIdObj = new BasicDBObject(QUIZ_ID, quizId);
			DBObject userObj = collection.findOne(quizIdObj);
			userObj.put(WINNER_ID, winnerId);
			collection.update(quizIdObj, userObj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}