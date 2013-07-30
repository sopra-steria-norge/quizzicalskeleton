package no.steria.quizzical;


import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


public class MongoQuizDaoTest {

	@Before
	public void setUp(){
		MongoDemo.insertTestQuizzes();
	}
	
	@Test
	public void shouldReadQuizzes() throws Exception {
		QuizDao dao = createDao();
		ArrayList<Quiz> quizzes = dao.getQuizzes();		
		
		assertThat(quizzes).hasSize(3);
	}

	private QuizDao createDao() {
		return new MongoQuizDao();
	}
}
