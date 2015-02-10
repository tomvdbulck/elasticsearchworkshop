package be.ordina.wes.exercises.basics;

import java.io.IOException;

import org.elasticsearch.client.Client;
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

	private final int expectedPersonDocuments = 5000;
	
	@Autowired
	private Client client;
    @Autowired
    private IndexService indexService;
    
    private Exercise2 exercise2;
	
	@Before
	public void setUp() {
		// if a test fails, make sure to cleanup before running tests again
		indexService.deleteIndex(TWITTER_INDEX);
		indexService.deleteIndex(PERSON_INDEX);
		indexService.deleteIndex(TEST_INDEX);
		
		exercise2 = new Exercise2(client);
	}
	
	/**
	 * Test index creation
	 */
	@Test
	public void testCreateIndex() {
		indexService.deleteIndex(TWITTER_INDEX);

		Assert.assertFalse(exercise2.indexExists(TWITTER_INDEX));
		
		exercise2.createIndex(TWITTER_INDEX);
		
		Assert.assertTrue(exercise2.indexExists(TWITTER_INDEX));
	}
	
	/**
	 * Test indexing one document
	 */
	@Test
	public void testIndexOneDocument() throws IOException {
		exercise2.indexOneDocument();
		
		// refresh the index prior to performing search operations
		exercise2.refreshIndex();
		
		long resultCount = client.prepareCount(TWITTER_INDEX).get().getCount();
		
		Assert.assertEquals(1, resultCount);
	}
	
	/**
	 * Test bulk indexing
	 */
	@Test
	public void testIndexMultipleDocuments() throws Exception {
		exercise2.indexMultipleDocuments();
		
		Assert.assertTrue(exercise2.indexExists(PERSON_INDEX));
		
		// refresh the index prior to performing search operations
		exercise2.refreshIndex();
		
		long docCount = client.prepareCount(PERSON_INDEX).get().getCount();
		
		Assert.assertEquals(expectedPersonDocuments, docCount);
	}

	/**
	 * Test index deletion
	 */
	@Test
	public void testDeleteIndex() {
		exercise2.createIndex(TEST_INDEX);
		
		Assert.assertTrue(exercise2.indexExists(TEST_INDEX));
		
		boolean indexDeleted = exercise2.deleteIndex(TEST_INDEX);
		Assert.assertTrue(indexDeleted);
		
		Assert.assertFalse(exercise2.indexExists(TEST_INDEX));
	}
}
