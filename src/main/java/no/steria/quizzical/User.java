package no.steria.quizzical;

import java.util.List;

public class User {
	private int userId;
	private String username;
	private String password;
	private List<Integer> quizzes;
	
	public User(int userId, String username, String password, List<Integer> quizzes) {
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

	public List<Integer> getQuizIds() {
		return quizzes;
	}
	
}
