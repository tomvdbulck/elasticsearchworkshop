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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class Exercise4Test {

	private static final Logger LOG = LoggerFactory.getLogger(Exercise4Test.class);
	
	private static final String PERSON_INDEX = "person";
	private static final String PERSON_TYPE = "person";
	private static final String PERSON_ID = "-QnBKlbHRPOO9pbebQMDjw";
	
	private final Integer expectedChildren = 4;

	@Autowired
	private Client client;
	@Autowired
	private IndexService indexService;
	
	private Exercise4 exercise4;
	
	@Before
	public void setUp() throws Exception {
		indexService.deleteIndex(PERSON_INDEX);
		PersonUtil.indexPersonDocuments(client);
		
		exercise4 = new Exercise4(client);
	}
	
	/**
	 * Test updating documents
	 */
	@Test
	public void testUpdatePerson() throws Exception {
		String field = "children";
		int value = 4;
		exercise4.updatePerson(PERSON_ID, field, value);
		
		indexService.refreshIndices();
		
		// build an ids query to find the needed person by ID
		QueryBuilder query = QueryBuilders.idsQuery(PERSON_TYPE).ids(PERSON_ID);
		
		SearchResponse searchResponse = client.prepareSearch()
				.setIndices(PERSON_INDEX)
				.setQuery(query)
				.get();
		
		LOG.trace("Search response: \n{}", searchResponse);
		
		List<Person> list = MappingUtil.mapSearchResults(searchResponse, Person.class);
		Assert.assertTrue(list.size() == 1);
		
		Person updatedPerson = list.get(0);
		// make sure the field has been updated
		Assert.assertEquals(expectedChildren, updatedPerson.getChildren());
	}
	
}
