package no.steria.quizzical;

import java.util.ArrayList;

public class User {
	private int userId;
	private ArrayList<Integer> quizIds;
	
	public User(int userId, ArrayList<Integer> quizIds) {
		this.userId = userId;
		this.quizIds = quizIds;
	}

	public int getUserId() {
		return userId;
	}

	public ArrayList<Integer> getQuizIds() {
		return quizIds;
	}
	
}
