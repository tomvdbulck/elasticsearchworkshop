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

	private Client client;
	
	public Exercise3(Client client) {
		this.client = client;
	}
	
	public List<Person> searchPerson(String field, String searchTerm) throws Exception {
		QueryBuilder matchQuery = null;
		// TODO-1: build a match query using field and searchTerm as arguments and assign it
		// to the matchQuery variable.
		// Make use of QueryBuilders class.
		matchQuery = QueryBuilders.matchQuery(field, searchTerm);
		
		SearchRequestBuilder requestBuilder = null;
		// TODO-2: prepare a search operation on the 'person' index, 
		// set the max result count to 50 (using setSize),
		// and set the above matchQuery as the search query (using setQuery).
		// Assign the operation to the requestBuilder.
		requestBuilder = client.prepareSearch(PERSON_INDEX)
				.setSize(MAX_RESULTS)
				.setQuery(matchQuery);
		
		LOG.trace("Search request: \n{}", requestBuilder);
		// executes the search operation using the previously built search request
		// and returns a response with search results
		SearchResponse response = requestBuilder.get();
		
		LOG.trace("Search response: \n{}", response);
		
		List<Person> personList = MappingUtil.mapSearchResults(response, Person.class);
		
		return personList;
	}
}
