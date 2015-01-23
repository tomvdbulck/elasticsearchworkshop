package be.ordina.wes.exercises.advanced_search;

import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ordina.wes.exercises.basics.Exercise1;

/**
 * In-depth search exercise #2:
 * Multifield search
 */
public class Exercise2_MultifieldSearch {

	private static final Logger LOG = LoggerFactory.getLogger(Exercise2_MultifieldSearch.class);
	
	private static final String PERSON_INDEX = "person";

	private static Client client = Exercise1.getInstance();
	
	public static SearchResponse searchPersonByMultipleFields(String searchTerm, String...searchFields) throws InterruptedException, ExecutionException {
		QueryBuilder query = null;
		// TODO-1: build a multimatch query (using QueryBuilders), 
		// using searchTerm and searchFields variables.
		// Assign the operation to the query variable.
		
		
		return buildSearchQuery(query);
	}
	
	public static SearchResponse searchPersonByMultipleTerms(String fieldName, String...searchTerms) throws InterruptedException, ExecutionException {
		// Note that term queries are not analyzed
		QueryBuilder query = null;
		// TODO-2: build a terms query, using fieldName and searchTerms variables.
		// Assign the operation to the query variable.
		
		
		return buildSearchQuery(query);
	}
	
	private static SearchResponse buildSearchQuery(QueryBuilder query) throws InterruptedException, ExecutionException {
		SearchRequestBuilder builder = client.prepareSearch()
				.setIndices(PERSON_INDEX)
				.setSize(30)
				.setQuery(query);
		
		LOG.trace("Search request: \n{}", builder);
		
		SearchResponse response = client.search(builder.request()).get();
		
		LOG.trace("Search response: \n{}", response);
		
		return response;
	}
}
