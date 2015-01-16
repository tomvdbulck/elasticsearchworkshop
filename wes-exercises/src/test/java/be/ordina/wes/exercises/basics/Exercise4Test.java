package be.ordina.wes.exercises.basics;

import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ordina.wes.common.util.MappingUtil;
import be.ordina.wes.exercises.model.Person;

public class Exercise4Test {

	private static final Logger LOG = LoggerFactory.getLogger(Exercise4Test.class);
	
	private static final String PERSON_INDEX = "person";
	private static final String PERSON_TYPE = "person";
	private static final String PERSON_ID = "-QnBKlbHRPOO9pbebQMDjw";
	
	private static Client client;
	
	private final Integer expectedChildren = 4;
	
	@BeforeClass
	public static void setUp() {
		client = Exercise1.getInstance();
	}
	
	@AfterClass
	public static void tearDown() {
		Exercise2.deleteIndex(PERSON_INDEX);
	}
	
	/**
	 * Test updating documents
	 */
	@Test
	public void testUpdatePerson() throws Exception {
		Exercise2.indexMultipleDocuments();
		
		String field = "children";
		int value = 4;
		Exercise4.updatePerson(PERSON_ID, field, value);
		
		Exercise2.refreshIndex();
		
		QueryBuilder query = QueryBuilders.idsQuery(PERSON_TYPE).ids(PERSON_ID);
		
		SearchRequest searchRequest = client.prepareSearch()
				.setIndices(PERSON_INDEX)
				.setQuery(query)
				.request();
		
		SearchResponse searchResponse = client.search(searchRequest).get();
		
		LOG.trace("Search response: \n{}", searchResponse);
		
		List<Person> list = MappingUtil.mapSearchResults(searchResponse, Person.class);
		Assert.assertTrue(list.size() == 1);
		
		Person updatedPerson = list.get(0);
		Assert.assertEquals(expectedChildren, updatedPerson.getChildren());
	}
	
}
