package no.steria.quizzical;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MongoQuizDaoTest {

	private MongoQuizDao mongoQuizDao;
	
	@Before
	public void setUp(){
		MongoDatabasePopulation.insertTestQuizzes();
		mongoQuizDao = new MongoQuizDao();
	}
	
	@Test
	public void shouldReadQuizzes() throws Exception {
		List<Quiz> quizzes = mongoQuizDao.getQuizzes();				
		assertThat(quizzes).hasSize(3);
	}
	
	@Test
	public void shouldGetQuiz() throws Exception {		
		Quiz quiz = MongoDatabasePopulation.testQuiz1();		
		assertThat(mongoQuizDao.getQuiz(1)).isEqualTo(quiz);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldGetExceptionWhenQuizIdNotAvailable() {		
		assertThat(mongoQuizDao.getQuiz(10));
	}
	

}