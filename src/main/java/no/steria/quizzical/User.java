package no.steria.quizzical;

import java.util.List;

public class User {
	private int userId;
	private String username;
	private String password;
	private List<Integer> quizzes;
	private byte[] encryptedPassword;
	private byte[] salt;
	
	public User(int userId, String username, String password, List<Integer> quizzes, byte[] salt, byte[] encryptedPassword) {
		this.userId = userId;
		this.username = username;
		this.password = password;
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
	
	public String getPassword(){
		return password;
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
