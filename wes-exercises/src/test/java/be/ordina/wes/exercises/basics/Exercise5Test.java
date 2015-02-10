package be.ordina.wes.exercises.basics;

import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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
public class Exercise5Test {

	private static final String PERSON_INDEX = "person";
	private static final String PERSON_TYPE = "person";
	private static final String PERSON_ID = "-QnBKlbHRPOO9pbebQMDjw";

	private final int expectedDocumentCount = 735;
	
	@Autowired
	private Client client;
	@Autowired
	private IndexService indexService;
	
	private Exercise5 exercise5;
	
	@Before
	public void setUp() throws Exception {
		indexService.deleteIndex(PERSON_INDEX);
		PersonUtil.indexPersonDocuments(client);
		
		exercise5 = new Exercise5(client);
	}
	
	/**
	 * Test document deletion
	 */
	@Test
	public void testDeletePerson() throws Exception {
		boolean isDeleted = exercise5.deletePerson(PERSON_ID);
		
		Assert.assertTrue(isDeleted);
		
		indexService.refreshIndices();
		
		QueryBuilder query = QueryBuilders.idsQuery(PERSON_TYPE).ids(PERSON_ID);
		
		SearchResponse response = client.prepareSearch()
				.setIndices(PERSON_INDEX)
				.setQuery(query)
				.get();
		
		List<Person> list = MappingUtil.mapSearchResults(response, Person.class);
		
		Assert.assertTrue(list.isEmpty());
	}
	
	/**
	 * Test document deletion by query
	 */
	@Test
	public void testDeletePeopleBornBefore() {
		String date = "2000-01-01";
		exercise5.deletePeopleBornBefore(date);
		
		long docCount = client.prepareCount(PERSON_INDEX).get().getCount();
		Assert.assertEquals(expectedDocumentCount, docCount);
	}
}
