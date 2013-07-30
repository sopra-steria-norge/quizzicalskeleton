package no.steria.quizzical;

import java.util.List;

public class User {
	private int userId;
	private String username;
	private List<Integer> quizzes;
	private byte[] encryptedPassword;
	private byte[] salt;
	
	public User(int userId, String username, byte[] salt, byte[] encryptedPassword, List<Integer> quizzes) {
		this.userId = userId;
		this.username = username;
		this.quizzes = quizzes;
		this.salt = salt;
		this.encryptedPassword = encryptedPassword;
	}

	public int getUserId() {
		return userId;
	}
	
	public String getUsername(){
		return username;
	}
	

	public List<Integer> getQuizIds() {
		return quizzes;
	}
	
	public byte[] getSalt() {
		return salt;
	}
	
	public byte[] getEncryptedPassword() {
		return encryptedPassword;
	}
}
