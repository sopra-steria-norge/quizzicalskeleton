package no.steria.quizzical;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class Response {

	private int quizId;
	private String name;
	private String email;
	private HashMap<String,String> quizAnswers;
	private int score;
	
	public Response(){
		
	}
	
	public Response(int quizId, String name, String email, HashMap<String,String> quizAnswers){
		this.quizId = quizId;
		this.name = name;
		this.email = email;
		this.quizAnswers = quizAnswers;
	}

	public int getQuizId(){
		return quizId;
	}
	
	public String getName(){
		return name;
	}
	
	public String getEmail(){
		return email;
	}
	
	public HashMap<String,String> getQuizAnswers(){
		return quizAnswers;
	}
	
	public int getScore(){
		return score;
	}

	public void calculateScore(Quiz quiz){
		int score = 0;
		BasicDBList questions = quiz.getQuestions();
		
		for (Object question : questions) {
			
			// maa hente ut id-verdien til dette feltet,
			// deretter hente ut feltet answer som er ritig svaralternativs-id
			// bruke questionid til aa finne tilsvarende id i hashmappet
			
			BasicDBObject bdbo  = (BasicDBObject) question;
			
			String key = bdbo.getString("id");
			// mulig denne maa hentes ut med ("q" + idnr)
			
			if (bdbo.get("answer").equals(quizAnswers.get(key))){
				score++;
			}
		}
		
		this.score = score;
		
		/*
		Set<String> keys = quiz.getQuestions().keySet();
		Iterator<String> keyIterator = keys.iterator();
		
		while(keyIterator.hasNext()){
			String key = keyIterator.next();
			
			if (questions.get(key).equals(quizAnswers.get(key))) {
				score++;
			}
			
		}
		*/
	}

}
