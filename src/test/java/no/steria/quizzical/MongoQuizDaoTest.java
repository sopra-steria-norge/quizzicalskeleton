package no.steria.quizzical;


import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


public class MongoQuizDaoTest {

	@Before
	public void setUp(){
		Quiz[] quizzesToAdd = new Quiz[2];
		quizzesToAdd[0] = new Quiz(1,"Geography Quiz","This is a quiz about Norwegian geography","Thank you for taking the quiz",null);
		quizzesToAdd[1] = new Quiz(2,"SecondQuiz","QuizDesc2","QuizMsg2",null);		

		MongoDemo.insertDataIntoDB(quizzesToAdd);

	}
	
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
