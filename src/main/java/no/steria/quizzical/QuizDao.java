package no.steria.quizzical;

import java.util.ArrayList;

public interface QuizDao {

	ArrayList<Quiz> getQuizzes();
	
	Quiz getQuiz(int quizId);
	
	void insertQuizToDB(Quiz quiz, int userId);
	
}
