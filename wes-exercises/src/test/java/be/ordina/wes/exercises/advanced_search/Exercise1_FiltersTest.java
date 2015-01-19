package be.ordina.wes.exercises.advanced_search;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.search.SearchResponse;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import be.ordina.wes.common.util.MappingUtil;
import be.ordina.wes.exercises.basics.Exercise2;
import be.ordina.wes.exercises.model.Person;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class Exercise1_FiltersTest {

	private static final String PERSON_INDEX = "person";
	
	private final int expectedResultsByDateOfBirth = 4;
	private final int expectedResultsByGender = 494;

	@BeforeClass
	public static void setUp() throws Exception {
		Exercise2.indexMultipleDocuments();
		Exercise2.refreshIndex();
	}
	
	@AfterClass
	public static void tearDown() {
		Exercise2.deleteIndex(PERSON_INDEX);
	}
	
	/**
	 * Test search with range filters
	 */
	@Test
	public void testSearchPersonByDateOfBirth() throws Exception {
		final String startDate = "1946-04-06";
		final String endDate = "1946-04-14";
		
		SearchResponse response = Exercise1_Filters
				.searchPersonByDateOfBirth(startDate, endDate);
		
		List<Person> list = MappingUtil.mapSearchResults(response, Person.class);
		
		Assert.assertEquals(expectedResultsByDateOfBirth, list.size());
	}
	
	/**
	 * Test search with boolean filters
	 */
	@Test
	public void testSearchPersonByGenderAndChildren() throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException {
		final String gender = "female";
		final int children = 3;
		
		SearchResponse response = Exercise1_Filters
				.searchPersonByGenderAndChildren(gender, children);
		
		List<Person> list = MappingUtil.mapSearchResults(response, Person.class);
		
		Assert.assertEquals(expectedResultsByGender, list.size());
	}
	
}
