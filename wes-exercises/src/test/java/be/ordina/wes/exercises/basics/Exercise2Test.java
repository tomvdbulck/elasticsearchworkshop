package be.ordina.wes.exercises.basics;

import java.io.IOException;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class Exercise2Test {
	
	private static final String PERSON_INDEX = "person";
	private static final String TWITTER_INDEX = "twitter";

	private static Client client;
	
	private final int expectedPersonDocuments = 5000;
	
	@BeforeClass
	public static void setUp() {
		client = Exercise1.getInstance();
	}
	
	@AfterClass
	public static void tearDown() {
		// delete all indices when we're done with tests
		client.admin().indices().prepareDelete(PERSON_INDEX, TWITTER_INDEX).get();
	}
	
	/**
	 * Test index creation
	 */
	@Test
	public void testCreateIndex() {
		Exercise2.createIndex(TWITTER_INDEX);
		
		boolean indexExists = client.admin().indices().prepareExists(TWITTER_INDEX).get().isExists();
		Assert.assertTrue(indexExists);
	}
	
	/**
	 * Test indexing one document
	 */
	@Test
	public void testIndexOneDocument() throws IOException {
		Exercise2.indexOneDocument();
		
		// refresh the index prior to performing search operations
		Exercise2.refreshIndex();
		
		SearchResponse response = client.prepareSearch(TWITTER_INDEX).get();
		long resultCount = response.getHits().getTotalHits();
		
		Assert.assertEquals(1, resultCount);
	}
	
	/**
	 * Test bulk indexing
	 */
	@Test
	public void testIndexMultipleDocuments() throws Exception {
		Exercise2.indexMultipleDocuments();
		
		boolean indexExists = client.admin().indices().prepareExists(PERSON_INDEX).get().isExists();
		Assert.assertTrue(indexExists);
		
		// refresh the index prior to performing search operations
		Exercise2.refreshIndex();
		
		long docCount = client.prepareCount(PERSON_INDEX).get().getCount();
		
		Assert.assertEquals(expectedPersonDocuments, docCount);
	}
	
}
