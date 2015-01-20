package be.ordina.wes.exercises.basics;

import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
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
	
	public static List<Person> searchPerson(String field, String value) throws Exception {
		QueryBuilder query = null;
		// TODO-1: build a match query with field and value as arguments and assign it
		// to the query variable.
		
		
		SearchRequestBuilder requestBuilder = null;
		// TODO-2: prepare a search operation inside the index 'person', 
		// set the max result count to 50 (using setSize),
		// and set the above query as the search query (using setQuery)
		
		
		LOG.trace("Search request: \n{}", requestBuilder);
		
		SearchRequest request = requestBuilder.request();
		
		SearchResponse response = client.search(request).get();
		
		LOG.trace("Search response: \n{}", response);
		
		List<Person> personList = MappingUtil.mapSearchResults(response, Person.class);
		
		return personList;
	}
}
