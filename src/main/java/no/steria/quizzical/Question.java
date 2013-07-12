package no.steria.quizzical;

import com.mongodb.BasicDBList;

public class Question {

	private int id;
	private String text;
	private BasicDBList alternatives;
	private int answer;
	
	public Question() {
		
	}

	public Question(int id, String text, BasicDBList alternatives, int answer) {
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

	public BasicDBList getAlternatives() {
		return alternatives;
	}
	
	public int getAnswer() {
		return answer;
	}
	
	

}
