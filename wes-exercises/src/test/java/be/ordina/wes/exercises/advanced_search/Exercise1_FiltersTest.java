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
	private final int expectedResultsByCity = 19;

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
	public void testSearchPersonByCity() throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException {
		final String gender = "female";
		final int children = 3;
		final String city = "berlin";
		// Notice that city is in lowercase, otherwise we'd get no results, 
		// because of the use of term filters.
		// Same as term queries, term filters are not analyzed, meaning that your
		// query should be exactly the same as the indexed term.
		
		SearchResponse response = Exercise1_Filters
				.searchPersonByCity(gender, children, city);
		
		List<Person> list = MappingUtil.mapSearchResults(response, Person.class);
		
		Assert.assertEquals(expectedResultsByCity, list.size());
	}
	
}
