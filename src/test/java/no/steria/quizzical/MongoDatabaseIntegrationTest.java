package no.steria.quizzical;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class MongoDatabaseIntegrationTest {

	@Test
	public void insertToAndRetrieveFromDatabase(){
		
		MongoDemo.insertTestQuizzes();
		Quiz retrievedQuiz = MongoDemo.getQuizHelper(1);
		
		assertThat(MongoDemo.testQuiz1().getQuizId()).isEqualTo(retrievedQuiz.getQuizId());
		assertThat(MongoDemo.testQuiz1().getQuizName()).isEqualTo(retrievedQuiz.getQuizName());
	}
	
}