package no.steria.quizzical;

import java.util.List;

public interface QuizDao {

	List<Quiz> getQuizzes();
	
	Quiz getQuiz(int quizId);
	
}