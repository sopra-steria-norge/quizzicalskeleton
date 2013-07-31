package no.steria.quizzical;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MongoResponseDaoTest {

	private MongoResponseDao mongoResponseDao;
	
	@Before
	public void setUp(){
		mongoResponseDao = new MongoResponseDao();
		MongoDatabasePopulation.dropResponsesInDB();
	}

	@Test
	public void shouldSetResponseAndRetrieveFromDB() throws Exception {	
		Response inputResponse = MongoDatabasePopulation.testResponse1();
		mongoResponseDao.setResponse(inputResponse);
		
		List<Response> responses = mongoResponseDao.getRespondents(1);
		Response outputResponse = responses.get(0);
		assertThat(inputResponse.getName()).isEqualTo(outputResponse.getName());
		assertThat(inputResponse.getEmail()).isEqualTo(outputResponse.getEmail());
	}
	
	@Test
	public void shouldCountResponsesForQuiz(){
		mongoResponseDao.setResponse(MongoDatabasePopulation.testResponse1());
		mongoResponseDao.setResponse(MongoDatabasePopulation.testResponse2());
		assertThat(mongoResponseDao.getRespondents(1)).hasSize(2);
	}
	
	@Test
	public void shouldDrawRandomWinnerFromAvailableResponses(){
		mongoResponseDao.setResponse(MongoDatabasePopulation.testResponse1());
		mongoResponseDao.setResponse(MongoDatabasePopulation.testResponse2());
		String[] winner = mongoResponseDao.drawRandomWinner(1);
		assertThat(mongoResponseDao.getRespondents(1)).hasSize(2);
		assertThat(winner[0]).isNotEmpty();
		assertThat(winner[1]).isNotEmpty();
	}
	
}
