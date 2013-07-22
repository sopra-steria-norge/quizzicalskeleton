package no.steria.quizzical;

import java.util.HashMap;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class Response {

	private int quizId;
	private String name;
	private String email;
	private HashMap<String,Integer> quizAnswers;
	private int score;
	
	public Response(){
		
	}
	
	public Response(int quizId, String name, String email, HashMap<String,Integer> quizAnswers){
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
	
	public HashMap<String,Integer> getQuizAnswers(){
		return quizAnswers;
	}
	
	public int getScore(){
		return score;
	}

	public void calculateScore(Quiz quiz){
		int score = 0;
		BasicDBList questions = quiz.getQuestions();
		for (Object question : questions) {
			BasicDBObject bdbo  = (BasicDBObject) question;
			String key = "q" + bdbo.getString("id");
			if (bdbo.getInt("answer") == (quizAnswers.get(key))){
				score++;
			}
		}		
		this.score = score;
	}

}
