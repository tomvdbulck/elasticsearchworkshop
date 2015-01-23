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

import be.ordina.wes.common.util.MappingUtil;
import be.ordina.wes.exercises.model.Person;

public class Exercise5Test {

	private static final String PERSON_INDEX = "person";
	private static final String PERSON_TYPE = "person";
	private static final String PERSON_ID = "-QnBKlbHRPOO9pbebQMDjw";

	private static Client client;
	
	@BeforeClass
	public static void setUp() {
		Exercise2.deleteIndex(PERSON_INDEX);
		client = Exercise1.getInstance();
	}
	
	@AfterClass
	public static void tearDown() {
		Exercise2.deleteIndex(PERSON_INDEX);
	}
	
	/**
	 * Test document deletion
	 */
	@Test
	public void testDeletePerson() throws Exception {
		Exercise2.indexMultipleDocuments();
		
		boolean isDeleted = Exercise5.deletePerson(PERSON_ID);
		
		Assert.assertTrue(isDeleted);
		
		Exercise2.refreshIndex();
		
		QueryBuilder query = QueryBuilders.idsQuery(PERSON_TYPE).ids(PERSON_ID);
		
		SearchRequest request = client.prepareSearch()
				.setIndices(PERSON_INDEX)
				.setQuery(query)
				.request();
		
		SearchResponse response = client.search(request).get();
		
		List<Person> list = MappingUtil.mapSearchResults(response, Person.class);
		
		Assert.assertTrue(list.isEmpty());
	}
}
