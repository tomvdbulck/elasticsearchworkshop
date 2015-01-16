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
		QueryBuilder query = QueryBuilders.matchQuery(field, value);
		
		SearchRequestBuilder requestBuilder = client.prepareSearch()
				.setIndices(PERSON_INDEX)
				.setSize(MAX_RESULTS)
				.setQuery(query);
		
		LOG.trace("Search request: \n{}", requestBuilder);
		
		SearchRequest request = requestBuilder.request();
		
		SearchResponse response = client.search(request).get();
		
		LOG.trace("Search response: \n{}", response);
		
		List<Person> personList = MappingUtil.mapSearchResults(response, Person.class);
		
		return personList;
	}
}
