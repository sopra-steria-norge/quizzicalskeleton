package no.steria.quizzical;

import com.mongodb.BasicDBList;

public class Quiz {
	private int quizId;
	private String quizName;
	private String quizDesc;
	private String submitMsg;
	private BasicDBList questions;
	private int responses;
	
	public Quiz(){
		
	}
	
	public Quiz(int quizId, String quizName, String quizDesc, String submitMsg, BasicDBList questions) {
		this.quizId = quizId;
		this.quizName = quizName;
		this.quizDesc = quizDesc;
		this.submitMsg = submitMsg;
		this.questions = questions;
	}

	public int getQuizId() {
		return quizId;
	}
	
	public void setQuizId(int quizId){
		this.quizId = quizId;
	}

	public String getQuizName() {
		return quizName;
	}

	public String getQuizDesc() {
		return quizDesc;
	}

	public String getSubmitMsg() {
		return submitMsg;
	}

	public BasicDBList getQuestions() {
		return questions;
	}
	
	public void setResponses(int responses){
		this.responses = responses;
	}
	
	public int getResponses(){
		return responses;
	}
}
