package no.steria.quizzical;


import java.util.ArrayList;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


public class MongoQuizDaoTest {
	@Test
	public void shouldReadQuizzes() throws Exception {
		QuizDao dao = createDao();
		
		ArrayList<Quiz> quizzes = dao.getQuizzes();		
		assertThat(quizzes).hasSize(2);
	}

	private QuizDao createDao() {
		return new MongoQuizDao();
	}
}
