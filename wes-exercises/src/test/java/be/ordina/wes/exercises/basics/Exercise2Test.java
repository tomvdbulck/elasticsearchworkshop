package be.ordina.wes.exercises.basics;

import java.io.IOException;

import org.elasticsearch.client.Client;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.ordina.wes.core.service.IndexService;
import be.ordina.wes.exercises.config.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class Exercise2Test {
	
	private static final String PERSON_INDEX = "person";
	private static final String TWITTER_INDEX = "twitter";
	private static final String TEST_INDEX = "test_idx";

	@Autowired
	private Client client;
    @Autowired
    private IndexService indexService;
    
    private Exercise2 ex2;
	
	private final int expectedPersonDocuments = 5000;
	
	@Before
	public void setUp() {
		// if a test fails, make sure to cleanup before running tests again
		indexService.deleteIndex(TWITTER_INDEX);
		indexService.deleteIndex(PERSON_INDEX);
		indexService.deleteIndex(TEST_INDEX);
		
		ex2 = new Exercise2(client);
	}
	
	@After
	public void tearDown() {
		// delete all indices when we're done with tests
		indexService.deleteIndex(TWITTER_INDEX);
		indexService.deleteIndex(PERSON_INDEX);
		indexService.deleteIndex(TEST_INDEX);
	}
	
	/**
	 * Test index creation
	 */
	@Test
	public void testCreateIndex() {
		Assert.assertFalse(ex2.indexExists(TWITTER_INDEX));
		
		ex2.createIndex(TWITTER_INDEX);
		
		Assert.assertTrue(ex2.indexExists(TWITTER_INDEX));
	}
	
	/**
	 * Test indexing one document
	 */
	@Test
	public void testIndexOneDocument() throws IOException {
		ex2.indexOneDocument();
		
		// refresh the index prior to performing search operations
		ex2.refreshIndex();
		
		long resultCount = client.prepareCount(TWITTER_INDEX).get().getCount();
		
		Assert.assertEquals(1, resultCount);
	}
	
	/**
	 * Test bulk indexing
	 */
	@Test
	public void testIndexMultipleDocuments() throws Exception {
		ex2.indexMultipleDocuments();
		
		Assert.assertTrue(ex2.indexExists(PERSON_INDEX));
		
		// refresh the index prior to performing search operations
		ex2.refreshIndex();
		
		long docCount = client.prepareCount(PERSON_INDEX).get().getCount();
		
		Assert.assertEquals(expectedPersonDocuments, docCount);
	}

	/**
	 * Test index deletion
	 */
	@Test
	public void testDeleteIndex() {
		ex2.createIndex(TEST_INDEX);
		
		Assert.assertTrue(ex2.indexExists(TEST_INDEX));
		
		boolean indexDeleted = ex2.deleteIndex(TEST_INDEX);
		Assert.assertTrue(indexDeleted);
		
		Assert.assertFalse(ex2.indexExists(TEST_INDEX));
	}
}
