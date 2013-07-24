package no.steria.quizzical;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class MongoDatabaseIntegrationTest {

	@Test
	public void insertToAndRetrieveFromDatabase(){
		
		Quiz[] quizzesToAdd = new Quiz[2];
		quizzesToAdd[0] = new Quiz(1,"Geography Quiz","This is a quiz about Norwegian geography. The questions span from fantastic cities to amazing lakes.", "Thank you for taking the quiz. The winner will be announced on 2. august at 4 PM.", null);
		quizzesToAdd[1] = new Quiz(2,"Science Quiz For Kids","This quiz contains question relevant for children with interest in science. Use this quiz vicely as kids can be extremely interested in science, and possibly learn too much.","Thank you for taking the quiz. The winner will be announced on 2. august at 4 PM year 2017",null);		

		MongoDemo.insertTestQuizzesIntoDB(quizzesToAdd);
		Quiz retrievedQuiz = MongoDemo.getQuizHelper(1);
		
		assertThat(quizzesToAdd[0].getQuizId()).isEqualTo(retrievedQuiz.getQuizId());
		assertThat(quizzesToAdd[0].getQuizName()).isEqualTo(retrievedQuiz.getQuizName());
	}
	
}