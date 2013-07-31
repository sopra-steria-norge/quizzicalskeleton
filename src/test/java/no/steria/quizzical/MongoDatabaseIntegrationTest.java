package no.steria.quizzical;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class MongoDatabaseIntegrationTest {

	@Test
	public void insertToAndRetrieveFromDatabase(){
		
		MongoDatabasePopulation.insertTestQuizzes();
		Quiz retrievedQuiz = MongoDatabasePopulation.getQuizHelper(1);
		
		assertThat(MongoDatabasePopulation.testQuiz1().getQuizId()).isEqualTo(retrievedQuiz.getQuizId());
		assertThat(MongoDatabasePopulation.testQuiz1().getQuizName()).isEqualTo(retrievedQuiz.getQuizName());
	}
	
}