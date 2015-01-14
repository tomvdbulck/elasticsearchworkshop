package be.ordina.wes.exercises;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import be.ordina.wes.exercises.model.Person;

public class Exercise3Test {

	private final int expectedResultCount = 14;
	
	/**
	 * Test searching functionality
	 */
	@Test
	public void testSearchPerson() throws Exception {
		List<Person> list = Exercise3.searchPersons();
		
		Assert.assertEquals(expectedResultCount, list.size());
	}
}
