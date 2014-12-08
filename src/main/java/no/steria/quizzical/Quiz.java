package no.steria.quizzical;

import java.util.List;

public class Quiz {
	private int quizId;
	private String quizName;
	private String quizDesc;
	private String submitMsg;
	private List<Question> questions;
	private int responses;
	private boolean active;
	
	public Quiz(){
		
	}
	
	public Quiz(int quizId, String quizName, String quizDesc, String submitMsg, List<Question> questions, boolean active) {
		this.quizId = quizId;
		this.quizName = quizName;
		this.quizDesc = quizDesc;
		this.submitMsg = submitMsg;
		this.questions = questions;
		this.active = active;
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

	public List<Question> getQuestions() {
		return questions;
	}
	
	public void setResponses(int responses){
		this.responses = responses;
	}
	
	public int getResponses(){
		return responses;
	}
	
	public boolean getActive(){
		return active;
	}
	
	public void setActive(boolean active){
		this.active = active;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		
		if (obj instanceof Quiz){
			Quiz quiz = (Quiz) obj;
			
			if (this.quizId == quiz.quizId && 
				this.quizName.equals(quiz.quizName) && 
				this.quizDesc.equals(quiz.quizDesc) && 
				this.submitMsg.equals(quiz.submitMsg) && 
				this.questions.equals(quiz.questions) &&
				this.active == quiz.active ){
					isEqual = true;
			}
		}
		return isEqual;
	}

	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}

	public void setQuizDesc(String quizDesc) {
		this.quizDesc = quizDesc;
	}

	public void setSubmitMsg(String submitMsg) {
		this.submitMsg = submitMsg;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
}
