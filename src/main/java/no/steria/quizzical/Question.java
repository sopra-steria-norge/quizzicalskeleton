package no.steria.quizzical;

import java.util.List;

public class Question {

	private int id;
	private String text;
	private List<Alternative> alternatives;
	private int answer;
	private boolean answerInText;

	public Question(){}
	
	public Question(int id, String text, List<Alternative> alternatives, int answer, boolean answerInText){
		this.id = id;
		this.text = text;
		this.alternatives = alternatives;
		this.answer = answer;
		this.answerInText = answerInText;
	}

	public int getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public List<Alternative> getAlternatives() {
		return alternatives;
	}

	public int getAnswer() {
		return answer;
	}
	
	public void setAnswer(int answer){
		this.answer = answer;
	}

	public boolean isAnswerInText() {
		return answerInText;
	}

	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		if (obj instanceof Question){
			Question question = (Question) obj;
			
			if (this.id == question.id && 
				this.text.equals(question.text) && 
				this.alternatives.equals(question.alternatives) &&
				this.answer == question.answer){
					isEqual = true;
			}	
		}
		return isEqual;
	}
}