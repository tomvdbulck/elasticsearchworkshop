package be.ordina.wes.exercises.advanced_search;

import java.util.List;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class Exercise2_MultifieldSearchTest {

	private static final String PERSON_INDEX = "person";
	
	private final int expectedResultsByMultifield = 5;
	private final int expectedResultsByTerms = 8;
	
	private final Integer expectedCarsValue = 456;
	
	@Autowired
	private Client client;
	@Autowired
	private IndexService indexService;
	
	private Exercise2_MultifieldSearch exercise2;
	
	@Before
	public void setUp() throws Exception {
		indexService.deleteIndex(PERSON_INDEX);
		PersonUtil.indexPersonDocuments(client);
		indexService.refreshIndices();
		
		exercise2 = new Exercise2_MultifieldSearch(client);
	}
	
	/**
	 * Test multifield search
	 */
	@Test
	public void testSearchPersonByMultipleFields() throws Exception  {
		int marketingValue = 456;
		// TODO-3 (cont): boost 'cars' field by a factor of 2 (using ^2)
		String[] searchFields = { "marketing.cars", "marketing.toys", "marketing.music" };
		
		SearchResponse response = exercise2.searchPersonByMultipleFields(marketingValue, searchFields);
		List<Person> list = MappingUtil.mapSearchResults(response, Person.class);
		
		Assert.assertEquals(expectedResultsByMultifield, list.size());
		
		// check that 'cars' field is boosted (i.e. comes on top of search results)
		Assert.assertEquals(expectedCarsValue, list.get(0).getMarketing().getCars());
		Assert.assertEquals(expectedCarsValue, list.get(1).getMarketing().getCars());
	}
	
	/**
	 * Test query with multiple search terms
	 */
	@Test
	public void testSearchPersonByMultipleTerms() throws Exception {
		String fieldName = "name";
		String[] searchTerms = { "simon", "Sophie" };
		
		SearchResponse response = exercise2.searchPersonByMultipleTerms(fieldName, searchTerms);
		List<Person> list = MappingUtil.mapSearchResults(response, Person.class);
		
		Assert.assertEquals(expectedResultsByTerms, list.size());
	}
}
