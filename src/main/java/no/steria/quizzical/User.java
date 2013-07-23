package no.steria.quizzical;

import java.util.ArrayList;

public class User {
	private int userId;
	private String username;
	private String password;
	private ArrayList<Integer> quizzes;
	
	public User(int userId, String username, String password, ArrayList<Integer> quizzes) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.quizzes = quizzes;
	}

	public int getUserId() {
		return userId;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getPassword(){
		return password;
	}

	public ArrayList<Integer> getQuizIds() {
		return quizzes;
	}
	
}
