package be.ordina.wes.exercises.aggregations;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In-depth search exercise #3:
 * Aggregations
 */
public class Exercise1_Aggregations {

	private static final Logger LOG = LoggerFactory.getLogger(Exercise1_Aggregations.class);
	
	private static final String PERSON_INDEX = "person";
	private static final String COUNTRY_FIELD = "address.country";
	private static final String CITY_FIELD = "address.city";
	private static final String GENDER_FIELD = "gender";
	private static final String CHILDREN_FIELD = "children";
	private static final String BY_COUNTRY = "by_country";
	private static final String BY_CITY = "by_city";
	private static final String BY_GENDER = "by_gender";
	private static final String BY_CHILDREN = "by_children";
	
	private Client client;
	
	public Exercise1_Aggregations(Client client) {
		this.client = client;
	}
	
	/**
	 * Aggregate all person documents by country, city and gender.
	 * @return Search response with search results and aggregations
	 */
	public SearchResponse aggregatePersonByCountry() {
		TermsBuilder countryAggregation = null;
		// TODO-1: build a terms aggregation (using AggregationBuilders) 
		// based on the 'country' field, and name it 'by_country', 
		// so we can later find the aggregation by name.
		// Assign the operation to the countryAggregation variable.
		
		
		TermsBuilder cityAggregation = null;
		// TODO-2: build a terms aggregation based on the 'city' field, 
		// and name it 'by_city'.
		// Assign the operation to the cityAggregation variable.
		
		
		TermsBuilder genderAggregation = null;
		// TODO-3: build a terms aggregation based on the 'gender' field, 
		// and name it 'by_gender'.
		// Assign the operation to the genderAggregation variable.
		
		
		// TODO-4: add the city aggregation as a sub-aggregation
		// to the country aggregation (using subAggregation method).
		// This means that all found documents will be grouped by country,
		// and all documents falling into a country bucket will be further grouped by city.
		
		
		// TODO-5: We need to go deeper! 
		// Add the gender aggregation as a sub-aggregation 
		// to the city aggregation. 
		// Meaning that all documents falling into a city bucket will be grouped by gender.
		
		
		return buildSearchQuery(countryAggregation);
	}
	
	public SearchResponse aggregatePersonByChildren() {
		HistogramBuilder childrenAggregation = null;
		// TODO-6: build a histogram aggregation based on the 'children' field,
		// and name it 'by_children'.
		// Set the histogram interval to 1, so that every person with the same children 
		// count falls into the same bucket.
		// Assign the operation to the childrenAggregation variable.
		
		
		return buildSearchQuery(childrenAggregation);
	}
	
	private SearchResponse buildSearchQuery(AbstractAggregationBuilder aggregation) {
		SearchRequestBuilder requestBuilder = client.prepareSearch()
				.setIndices(PERSON_INDEX)
				.addAggregation(aggregation);
		// Note: if we set no query, it defaults to using match_all query.
		
		LOG.trace("Search request: \n{}", requestBuilder);
		
		SearchResponse response = requestBuilder.get();
		LOG.trace("Search response: \n{}", response);
		
		return response;
	}
}
