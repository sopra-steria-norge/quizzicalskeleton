package no.steria.quizzical;

import java.util.List;

public class Question {

	private int id;
	private String text;
	private List<Alternative> alternatives;
	private int answer;
	
	public Question(){
		
	}
	
	public Question(int id, String text, List<Alternative> alternatives, int answer){
		this.id = id;
		this.text = text;
		this.alternatives = alternatives;
		this.answer = answer;
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
	
}