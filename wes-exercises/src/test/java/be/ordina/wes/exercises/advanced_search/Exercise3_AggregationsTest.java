package be.ordina.wes.exercises.advanced_search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import be.ordina.wes.exercises.basics.Exercise2;

public class Exercise3_AggregationsTest {

	private static final String PERSON_INDEX = "person";
	private static final String BY_COUNTRY = "by_country";
	private static final String BY_CITY = "by_city";
	private static final String BY_GENDER = "by_gender";
	private static final String BY_CHILDREN = "by_children";
	
	private final int expectedEnglandDocs = 1262;
	private final int expectedLondonDocs = 339;
	private final int expectedFemalesInLondon = 173;
	private final int expectedPeopleWith3Children = 961;
	
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
	 * Test term aggregations
	 */
	@Test
	public void testAggregatePersonByCountry() {
		SearchResponse response = Exercise3_Aggregations.aggregatePersonByCountry();
		
		// fetch 'by_country' aggregation
		Terms countryAggregation = response.getAggregations().get(BY_COUNTRY);
		// extract "england" bucket from the aggregation
		Terms.Bucket englandBucket = countryAggregation.getBucketByKey("england");
		
		Assert.assertEquals(expectedEnglandDocs, englandBucket.getDocCount());
		
		// fetch 'by_city' aggregation from the "england" bucket
		Terms cityAggregation = englandBucket.getAggregations().get(BY_CITY);
		// extract "london" bucket from the 'by_city' aggregation
		Terms.Bucket londonBucket = cityAggregation.getBucketByKey("london");
		
		Assert.assertEquals(expectedLondonDocs, londonBucket.getDocCount());

		// fetch 'by_gender' aggregation from the "london" bucket
		Terms londonGenderAgg = londonBucket.getAggregations().get(BY_GENDER);
		// extract "female" bucket from the 'by_gender' aggregation
		Terms.Bucket femaleBucket = londonGenderAgg.getBucketByKey("female");
		
		// finally make sure that "england / london / female" document count 
		// matches the expected amount
		Assert.assertEquals(expectedFemalesInLondon, femaleBucket.getDocCount());
	}
	
	/**
	 * Test histogram aggregations
	 */
	@Test
	public void testAggregatePersonByChildren() {
		SearchResponse response = Exercise3_Aggregations.aggregatePersonByChildren();
		
		// fetch 'by_children' aggregation
		Histogram childrenAggregation = response.getAggregations().get(BY_CHILDREN);
		// extract the bucket where children count is 3
		Histogram.Bucket childrenBucket = childrenAggregation.getBucketByKey(3);
		
		Assert.assertEquals(expectedPeopleWith3Children, childrenBucket.getDocCount());
	}
	
}
