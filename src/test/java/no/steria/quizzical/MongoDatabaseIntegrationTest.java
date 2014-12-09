package no.steria.quizzical;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class MongoDatabaseIntegrationTest {

	@Test
	public void insertToAndRetrieveFromDatabase(){
		
		MongoDatabasePopulation.getInstance().insertTestQuizzesIntoDB();
		Quiz retrievedQuiz = MongoDatabasePopulation.getInstance().getQuizHelper(1);
		
		assertThat(MongoDatabasePopulation.getInstance().testQuiz1().getQuizId()).isEqualTo(retrievedQuiz.getQuizId());
		assertThat(MongoDatabasePopulation.getInstance().testQuiz1().getQuizName()).isEqualTo(retrievedQuiz.getQuizName());
	}
	
}