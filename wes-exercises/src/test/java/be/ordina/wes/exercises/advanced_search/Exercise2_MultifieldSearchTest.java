package be.ordina.wes.exercises.advanced_search;

import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import be.ordina.wes.common.util.MappingUtil;
import be.ordina.wes.exercises.basics.Exercise2;
import be.ordina.wes.exercises.model.Person;

public class Exercise2_MultifieldSearchTest {

	private static final String PERSON_INDEX = "person";
	
	private final int expectedResultsByMultifield = 5;
	private final int expectedResultsByTerms = 8;
	
	private final Integer expectedCarsValue = 456;
	
	@BeforeClass
	public static void setUp() throws Exception {
		Exercise2.deleteIndex(PERSON_INDEX);
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
	public void testSearchPersonByMultipleFields() throws Exception  {
		int marketingValue = 456;
		// TODO-3 (cont): boost 'cars' field with value of 2 (using ^2)
		String[] searchFields = { "marketing.cars", "marketing.toys", "marketing.music" };
		
		SearchResponse response = Exercise2_MultifieldSearch.searchPersonByMultipleFields(marketingValue, searchFields);
		List<Person> list = MappingUtil.mapSearchResults(response, Person.class);
		
		Assert.assertEquals(expectedResultsByMultifield, list.size());
		
		// check that 'cars' field is boosted (i.e. comes on top of search results)
		Assert.assertEquals(expectedCarsValue, list.get(0).getMarketing().getCars());
		Assert.assertEquals(expectedCarsValue, list.get(1).getMarketing().getCars());
	}
	
	/**
	 * Test query with multiple search terms
	 */
	@Test
	public void testSearchPersonByMultipleTerms() throws Exception {
		String fieldName = "name";
		String[] searchTerms = { "simon", "Sophie" };
		
		SearchResponse response = Exercise2_MultifieldSearch.searchPersonByMultipleTerms(fieldName, searchTerms);
		List<Person> list = MappingUtil.mapSearchResults(response, Person.class);
		
		Assert.assertEquals(expectedResultsByTerms, list.size());
	}
}
