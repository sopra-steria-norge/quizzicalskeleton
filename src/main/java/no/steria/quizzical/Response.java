package no.steria.quizzical;

import java.util.Map;

public class Response {

	private int quizId;
	private String name;
	private String email;
	private Map<String,String[]> quizAnswers;
	private int score;
	
	public Response(){
		
	}
	
	public Response(int quizId, String name, String email, Map<String,String[]> quizAnswers){
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
	
	public Map<String,String[]> getQuizAnswers(){
		return quizAnswers;
	}
	
	public int calculateAndRetrieveScore(){
		// TODO Calculation of score
		this.score = 1000;
		return score;
	}
			
}
