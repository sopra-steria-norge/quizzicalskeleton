package no.steria.quizzical;

import com.mongodb.BasicDBList;

public class Question {
	
	private int id;
	private String text;
	private BasicDBList alternatives;
	
	public Question() {
		
	}
	
	public Question(int id, String text, BasicDBList alternatives) {
		this.id = id;
		this.text = text;
		this.alternatives = alternatives;
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
	
}
