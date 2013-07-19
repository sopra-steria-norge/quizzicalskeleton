package no.steria.quizzical;

import java.util.HashMap;

import com.mongodb.BasicDBList;

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
		BasicDBList questions = quiz.getQuestions();
		//questions.g
		
		this.score = 1000;
	}

}
