package be.ordina.wes.exercises.advanced_search;

import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In-depth search exercise #2:
 * Multifield search
 */
public class Exercise2_MultifieldSearch {

	private static final Logger LOG = LoggerFactory.getLogger(Exercise2_MultifieldSearch.class);
	
	private static final String PERSON_INDEX = "person";
	private static final int MAX_RESULTS = 30;

	private Client client;
	
	public Exercise2_MultifieldSearch(Client client) {
		this.client = client;
	}
	
	public SearchResponse searchPersonByMultipleFields(int value, String...searchFields) throws InterruptedException, ExecutionException {
		QueryBuilder multimatchQuery = null;
		// TODO-1: build a multimatch query (using QueryBuilders), 
		// using searchTerm and searchFields variables.
		// Assign the operation to the multimatchQuery variable.
		multimatchQuery = QueryBuilders.multiMatchQuery(value, searchFields);
		
		return buildSearchQuery(multimatchQuery);
	}
	
	public SearchResponse searchPersonByMultipleTerms(String fieldName, String...searchTerms) throws InterruptedException, ExecutionException {
		// Note that term queries are not analyzed
		QueryBuilder termsQuery = null;
		// TODO-2: build a terms query, using fieldName and searchTerms variables.
		// Assign the operation to the termsQuery variable.
		termsQuery = QueryBuilders.termsQuery(fieldName, searchTerms);
		
		return buildSearchQuery(termsQuery);
	}
	// TODO-3: check Exercise2_MultifieldSearchTest
	private SearchResponse buildSearchQuery(QueryBuilder query) throws InterruptedException, ExecutionException {
		SearchRequestBuilder request = client.prepareSearch()
				.setIndices(PERSON_INDEX)
				.setSize(MAX_RESULTS)
				.setQuery(query);
		
		LOG.trace("Search request: \n{}", request);
		
		SearchResponse response = request.get();
		
		LOG.trace("Search response: \n{}", response);
		
		return response;
	}
}
