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

public class Exercise2_MultifieldSearchTest {

	private static final String PERSON_INDEX = "person";
	
	private final int expectedSearchResults = 5;
	
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
	 * Test multifield search
	 */
	@Test
	public void testSearchPersonByMultipleFields() throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException  {
		String searchQuery = "456";
		String[] searchFields = { "marketing.cars", "marketing.toys", "marketing.music" };
		
		SearchResponse response = Exercise2_MultifieldSearch.searchPersonByMultipleFields(searchQuery, searchFields);
		List<Person> list = MappingUtil.mapSearchResults(response, Person.class);
		
		Assert.assertEquals(expectedSearchResults, list.size());
	}
}
