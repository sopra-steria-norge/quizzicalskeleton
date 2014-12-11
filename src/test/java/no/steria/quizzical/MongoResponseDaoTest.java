package no.steria.quizzical;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MongoResponseDaoTest {

	private MongoResponseDao mongoResponseDao;
	private int QUIZ_ID = -1;

	@Before
	public void setUp(){
		mongoResponseDao = new MongoResponseDao();
		MongoDatabasePopulation.getInstance().dropResponsesInDB();
	}

	@Test
	public void shouldSetResponseAndRetrieveFromDB() throws Exception {	
		Response inputResponse = getResponse("test1", 1);
		mongoResponseDao.setResponse(inputResponse);
		
		List<Response> responses = mongoResponseDao.getRespondents(QUIZ_ID);
		Response outputResponse = responses.get(0);
		assertThat(inputResponse.getName()).isEqualTo(outputResponse.getName());
		assertThat(inputResponse.getEmail()).isEqualTo(outputResponse.getEmail());
	}
	
	@Test
	public void shouldCountResponsesForQuiz(){
		mongoResponseDao.setResponse(getResponse("test1", 1));
		mongoResponseDao.setResponse(getResponse("test2", 1));
		assertThat(mongoResponseDao.getRespondents(QUIZ_ID)).hasSize(2);
	}
	
	@Test
	public void shouldDrawRandomWinnerFromBestResponses(){
		mongoResponseDao.setResponse(getResponse("test1", 1));
		mongoResponseDao.setResponse(getResponse("test2", 1));
		mongoResponseDao.setResponse(getResponse("test3", 2));
		Response winner = mongoResponseDao.drawRandomWinner(QUIZ_ID);

		assertEquals("test3", winner.getName());
	}

	private Response getResponse(String username, int score){
		Response response = new Response(QUIZ_ID, username, username + "@domain.com",  "Sopra", "113", new HashMap<String, Integer>());
		response.setScore(score);
		return response;
	}
	
}
