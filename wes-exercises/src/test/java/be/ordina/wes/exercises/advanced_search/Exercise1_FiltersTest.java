package be.ordina.wes.exercises.advanced_search;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.ordina.wes.common.util.MappingUtil;
import be.ordina.wes.core.service.IndexService;
import be.ordina.wes.exercises.config.TestConfig;
import be.ordina.wes.exercises.model.Person;
import be.ordina.wes.exercises.util.PersonUtil;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class Exercise1_FiltersTest {

	private static final String PERSON_INDEX = "person";
	
	private final int expectedResultsByDateOfBirth = 4;
	private final int expectedResultsByCity = 19;
	private final String expectedSecondPerson = "Lia Rayane";
	private final String expectedLastPerson = "Julia Diana";

	@Autowired
	private Client client;
	@Autowired
	private IndexService indexService;
	
	private Exercise1_Filters exercise1;
	
	@Before
	public void setUp() throws Exception {
		indexService.deleteIndex(PERSON_INDEX);
		PersonUtil.indexPersonDocuments(client);
		indexService.refreshIndices();
		
		exercise1 = new Exercise1_Filters(client);
	}
	
	/**
	 * Test search with range filters
	 */
	@Test
	public void testSearchPersonByDateOfBirth() throws Exception {
		final String startDate = "1946-04-06";
		final String endDate = "1946-04-14";
		
		SearchResponse response = exercise1.searchPersonByDateOfBirth(startDate, endDate);
		
		List<Person> list = MappingUtil.mapSearchResults(response, Person.class);
		
		Assert.assertEquals(expectedResultsByDateOfBirth, list.size());
	}
	
	/**
	 * Test search with boolean filters
	 */
	@Test
	public void testSearchPersonByCity() throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException {
		final String gender = "female";
		final int children = 3;
		final String city = "berlin";
		// Notice that city is in lowercase, otherwise we'd get no results, 
		// because of the use of term filters.
		// Same as term queries, term filters are not analyzed, meaning that your
		// query should be exactly the same as the indexed term.
		
		SearchResponse response = exercise1.searchPersonByCity(gender, children, city);
		
		List<Person> list = MappingUtil.mapSearchResults(response, Person.class);
		
		Assert.assertEquals(expectedResultsByCity, list.size());
		
		// check that results are sorted by age
		Assert.assertEquals(expectedSecondPerson, list.get(1).getName());
		Assert.assertEquals(expectedLastPerson, list.get(18).getName());
	}
	
}
