package no.steria.quizzical;

import java.util.Iterator;
import java.util.Map;

public class Response {

	private int quizId;
	private String name;
	private String email;
	private String company;
	private String phoneNumber;
	private Map<String,Integer> quizAnswers;
	private int score;
	
	public Response(){
	}
	
	public Response(int quizId, String name, String email, String company, String phoneNumber, Map<String,Integer> quizAnswers){
		this.quizId = quizId;
		this.name = name;
		this.email = email;
		this.company = company;
		this.phoneNumber = phoneNumber;
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

	public String getCompany() {
		return company;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Map<String,Integer> getQuizAnswers(){
		return quizAnswers;
	}
	
	public int getScore(){
		return score;
	}
	
	public void setScore(int score){
		this.score = score;
	}

	public void calculateScore(Quiz quiz){
		int score = 0;
		Iterator<Question> iterator = quiz.getQuestions().iterator();
		while(iterator.hasNext()){
			Question question = iterator.next();
			Integer userAnswer = quizAnswers.get("q"+question.getId());
			if(userAnswer != null){
				if(question.getAnswer() == userAnswer){
					score++;
				}				
			}
		}
		this.score = score;
	}

}
