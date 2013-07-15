package no.steria.quizzical;

public class Respondent {

	private int quizId;
	private String name;
	private String email;
	private int score;
	
	public Respondent(){
		
	}
	
	public Respondent(int quizId, String name, String email, int score){
		this.quizId = quizId;
		this.name = name;
		this.email = email;
		this.score = score;
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
	
	public int getScore(){
		return score;
	}
	
}
