package be.ordina.wes.exercises.advanced_search;

import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In-depth search exercise #1:
 * Search with filters (and inner objects)
 */
public class Exercise1_Filters {

	private static final Logger LOG = LoggerFactory.getLogger(Exercise1_Filters.class);
	
	private static final String PERSON_INDEX = "person";
	private static final String DATE_OF_BIRTH_FIELD = "dateOfBirth";
	private static final String GENDER_FIELD = "gender";
	private static final String CHILDREN_FIELD = "children";
	private static final String CITY_FIELD = "address.city"; // inner object field
	private static final int MAX_RESULTS = 30;

	private Client client;
	
	public Exercise1_Filters(Client client) {
		this.client = client;
	}
	
	/**
	 * Search for person documents filtered by date of birth
	 * @param startDate The start date
	 * @param endDate The end date
	 * @return Search response with search results
	 */
	public SearchResponse searchPersonByDateOfBirth(String startDate, String endDate) throws InterruptedException, ExecutionException {
		RangeFilterBuilder rangeFilter = null;
		// TODO-1: build a range filter (using FilterBuilders), 
		// and set the date range from startDate to endDate.
		// Filter on dateOfBirth field, and assign the operation to rangeFilter variable.
		
		
		return buildSearchQuery(rangeFilter);
	}
	
	/**
	 * Search for person documents filtered by gender, children count and city
	 * @param gender Gender
	 * @param children Children count
	 * @param city City
	 * @return Search response with search results
	 */
	public SearchResponse searchPersonByCity(String gender, int children, String city) throws InterruptedException, ExecutionException {
		BoolFilterBuilder boolFilter = null;
		// TODO-2: build a bool filter which consists of 3 sub-filters (term filters),
		// filter on 'gender', 'children' and 'address.city' fields.
		// All 3 filters should be inside a "must" clause.
		// Assign the operation to the boolFilter variable.
		
		
		return buildSearchQuery(boolFilter);
	}
	
	private SearchResponse buildSearchQuery(FilterBuilder filter) throws InterruptedException, ExecutionException {
		QueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
		QueryBuilder filteredQuery = QueryBuilders.filteredQuery(matchAllQuery, filter);

		// TODO-3: sort results by date of birth in ascending order (using addSort)
		SearchRequestBuilder requestBuilder = client.prepareSearch()
			.setIndices(PERSON_INDEX)
			.setQuery(filteredQuery)
			.setSize(MAX_RESULTS);
		
		LOG.trace("Search request: \n{}", requestBuilder);
		
		SearchResponse response = requestBuilder.get();
		
		LOG.trace("Search response: \n{}", response);
		
		return response;
	}
}
