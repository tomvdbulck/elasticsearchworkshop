package be.ordina.wes.exercises;

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

import be.ordina.wes.common.util.MappingUtil;
import be.ordina.wes.exercises.model.Person;

public class Exercise4Test {

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
		// delete the index when we're done with tests
		client.admin().indices().prepareDelete(PERSON_INDEX).get();
	}
	
	/**
	 * Test updating documents
	 */
	@Test
	public void testUpdatePerson() throws Exception {
		Exercise2.indexMultipleDocuments();
		Exercise4.updatePerson();
		Exercise2.refreshIndex();
		
		QueryBuilder query = QueryBuilders.idsQuery(PERSON_TYPE).ids(PERSON_ID);
		
		SearchRequest searchRequest = client.prepareSearch()
				.setIndices(PERSON_INDEX)
				.setQuery(query)
				.request();
		
		SearchResponse searchResponse = client.search(searchRequest).get();
		
		List<Person> list = MappingUtil.mapSearchResults(searchResponse, Person.class);
		Assert.assertTrue(list.size() == 1);
		
		Person updatedPerson = list.get(0);
		Assert.assertEquals(expectedChildren, updatedPerson.getChildren());
	}
	
}
