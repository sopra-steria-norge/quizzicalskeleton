package no.steria.quizzical;


import java.util.List;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;



public class MongoQuestionDaoTest {
	@Test
	public void shouldReadQuestions() throws Exception {
		QuestionDao dao = createDao();
		
		List<Question> questions = dao.getQuestions();
		
		assertThat(questions).hasSize(2);
	}

	private QuestionDao createDao() {
		return new MongoQuestionDao();
	}
}
