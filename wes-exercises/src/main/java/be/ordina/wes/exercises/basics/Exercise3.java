package be.ordina.wes.exercises.basics;

import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ordina.wes.common.util.MappingUtil;
import be.ordina.wes.exercises.model.Person;

/**
 * Exercise #3:
 * Searching data
 */
public class Exercise3 {

	private static final Logger LOG = LoggerFactory.getLogger(Exercise3.class);
	
	private static final String PERSON_INDEX = "person";
	private static final int MAX_RESULTS = 50;

	private static Client client = Exercise1.getInstance();
	
	public static List<Person> searchPerson(String field, String searchTerm) throws Exception {
		QueryBuilder matchQuery = null;
		// TODO-1: build a match query using field and searchTerm as arguments and assign it
		// to the matchQuery variable.
		// Make use of QueryBuilders class.
		
		
		SearchRequestBuilder requestBuilder = null;
		// TODO-2: prepare a search operation on the 'person' index, 
		// set the max result count to 50 (using setSize),
		// and set the above matchQuery as the search query (using setQuery).
		// Assign the operation to the requestBuilder.
		
		
		LOG.trace("Search request: \n{}", requestBuilder);
		// executes the search operation using the previously built search request
		SearchResponse response = requestBuilder.get();
		
		LOG.trace("Search response: \n{}", response);
		
		List<Person> personList = MappingUtil.mapSearchResults(response, Person.class);
		
		return personList;
	}
}
