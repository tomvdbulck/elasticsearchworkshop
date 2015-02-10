package be.ordina.wes.exercises.basics;

import java.util.List;

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
import be.ordina.wes.exercises.model.Person;
import be.ordina.wes.exercises.util.PersonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class Exercise3Test {

	private static final String PERSON_INDEX = "person";

	private final int expectedResultCount = 14;
	
	@Autowired
	private Client client;
	@Autowired
	private IndexService indexService;
	
	private Exercise3 exercise3;
	
	@Before
	public void setUp() throws Exception {
		indexService.deleteIndex(PERSON_INDEX);
		PersonUtil.indexPersonDocuments(client);
		// refresh the index prior to performing search operations
		indexService.refreshIndices();
		
		exercise3 = new Exercise3(client);
	}
	
	/**
	 * Test searching functionality
	 */
	@Test
	public void testSearchPerson() throws Exception {
		String field = "_all"; // will search in all fields
		String value = "Scarlett";
		List<Person> list = exercise3.searchPerson(field, value);
		
		Assert.assertEquals(expectedResultCount, list.size());
	}
}
