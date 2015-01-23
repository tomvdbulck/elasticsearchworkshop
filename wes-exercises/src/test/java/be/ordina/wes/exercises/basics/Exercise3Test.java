package be.ordina.wes.exercises.basics;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import be.ordina.wes.exercises.model.Person;

public class Exercise3Test {

	private static final String PERSON_INDEX = "person";

	private final int expectedResultCount = 14;
	
	@BeforeClass
	public static void setUp() throws Exception {
		Exercise2.deleteIndex(PERSON_INDEX);
		Exercise2.indexMultipleDocuments();
		// refresh the index prior to performing search operations
		Exercise2.refreshIndex();
	}
	
	@AfterClass
	public static void tearDown() {
		// delete the index when we're done with tests
		Exercise2.deleteIndex(PERSON_INDEX);
	}
	
	/**
	 * Test searching functionality
	 */
	@Test
	public void testSearchPerson() throws Exception {
		String field = "_all"; // will search in all fields
		String value = "Scarlett";
		List<Person> list = Exercise3.searchPerson(field, value);
		
		Assert.assertEquals(expectedResultCount, list.size());
	}
}
