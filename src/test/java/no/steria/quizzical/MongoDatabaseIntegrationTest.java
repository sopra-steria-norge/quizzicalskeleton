package no.steria.quizzical;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class MongoDatabaseIntegrationTest {

	@Test
	public void insertToAndRetrieveFromDatabase(){
		
		Quiz[] quizzesToAdd = new Quiz[2];
		quizzesToAdd[0] = new Quiz(1,"Geography Quiz","This is a quiz about Norwegian geography","Thank you for taking the quiz",null);
		quizzesToAdd[1] = new Quiz(2,"SecondQuiz","QuizDesc2","QuizMsg2",null);		

		MongoDemo.insertTestQuizzesIntoDB(quizzesToAdd);
		Quiz retrievedQuiz = MongoDemo.getQuizHelper(1);
		
		assertThat(quizzesToAdd[0].getQuizId()).isEqualTo(retrievedQuiz.getQuizId());
		assertThat(quizzesToAdd[0].getQuizName()).isEqualTo(retrievedQuiz.getQuizName());
	}

	
}
