package be.ordina.wes.exercises.basics;

import java.io.IOException;

import org.elasticsearch.client.Client;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class Exercise2Test {
	
	private static final String PERSON_INDEX = "person";
	private static final String TWITTER_INDEX = "twitter";
	private static final String TEST_INDEX = "test_idx";

	private static Client client;
	
	private final int expectedPersonDocuments = 5000;
	
	@BeforeClass
	public static void setUp() {
		// if a test fails, make sure to cleanup before running tests again
		Exercise2.deleteIndex(TWITTER_INDEX);
		Exercise2.deleteIndex(PERSON_INDEX);
		Exercise2.deleteIndex(TEST_INDEX);
		
		client = Exercise1.getInstance();
	}
	
	@AfterClass
	public static void tearDown() {
		// delete all indices when we're done with tests
		Exercise2.deleteIndex(TWITTER_INDEX);
		Exercise2.deleteIndex(PERSON_INDEX);
		Exercise2.deleteIndex(TEST_INDEX);
	}
	
	/**
	 * Test index creation
	 */
	@Test
	public void testCreateIndex() {
		Assert.assertFalse(Exercise2.indexExists(TWITTER_INDEX));
		
		Exercise2.createIndex(TWITTER_INDEX);
		
		Assert.assertTrue(Exercise2.indexExists(TWITTER_INDEX));
	}
	
	/**
	 * Test indexing one document
	 */
	@Test
	public void testIndexOneDocument() throws IOException {
		Exercise2.indexOneDocument();
		
		// refresh the index prior to performing search operations
		Exercise2.refreshIndex();
		
		long resultCount = client.prepareCount(TWITTER_INDEX).get().getCount();
		
		Assert.assertEquals(1, resultCount);
	}
	
	/**
	 * Test bulk indexing
	 */
	@Test
	public void testIndexMultipleDocuments() throws Exception {
		Exercise2.indexMultipleDocuments();
		
		Assert.assertTrue(Exercise2.indexExists(PERSON_INDEX));
		
		// refresh the index prior to performing search operations
		Exercise2.refreshIndex();
		
		long docCount = client.prepareCount(PERSON_INDEX).get().getCount();
		
		Assert.assertEquals(expectedPersonDocuments, docCount);
	}

	/**
	 * Test index deletion
	 */
	@Test
	public void testDeleteIndex() {
		Exercise2.createIndex(TEST_INDEX);
		
		Assert.assertTrue(Exercise2.indexExists(TEST_INDEX));
		
		boolean indexDeleted = Exercise2.deleteIndex(TEST_INDEX);
		Assert.assertTrue(indexDeleted);
		
		Assert.assertFalse(Exercise2.indexExists(TEST_INDEX));
	}
}
