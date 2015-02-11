package be.ordina.wes.exercises.advanced_search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.ordina.wes.core.service.IndexService;
import be.ordina.wes.exercises.config.TestConfig;
import be.ordina.wes.exercises.util.PersonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
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
	
	@Autowired
	private Client client;
	@Autowired
	private IndexService indexService;
	
	private Exercise3_Aggregations exercise3;
	
	@Before
	public void setUp() throws Exception {
		indexService.deleteIndex(PERSON_INDEX);
		PersonUtil.indexPersonDocuments(client);
		indexService.refreshIndices();
		
		exercise3 = new Exercise3_Aggregations(client);
	}
	
	/**
	 * Test term aggregations
	 */
	@Test
	public void testAggregatePersonByCountry() {
		SearchResponse response = exercise3.aggregatePersonByCountry();
		
		// fetch 'by_country' aggregation
		Terms countryAggregation = response.getAggregations().get(BY_COUNTRY);
		// extract "england" bucket from the aggregation
		Terms.Bucket englandBucket = countryAggregation.getBucketByKey("england");
		
		Assert.assertEquals(expectedEnglandDocs, englandBucket.getDocCount());
		
		// fetch 'by_city' aggregation from the "england" bucket
		Terms cityAggregation = englandBucket.getAggregations().get(BY_CITY);
		Assert.assertNotNull(cityAggregation);
		// extract "london" bucket from the 'by_city' aggregation
		Terms.Bucket londonBucket = cityAggregation.getBucketByKey("london");
		
		Assert.assertEquals(expectedLondonDocs, londonBucket.getDocCount());

		// fetch 'by_gender' aggregation from the "london" bucket
		Terms londonGenderAgg = londonBucket.getAggregations().get(BY_GENDER);
		Assert.assertNotNull(londonGenderAgg);
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
		SearchResponse response = exercise3.aggregatePersonByChildren();
		
		// fetch 'by_children' aggregation
		Histogram childrenAggregation = response.getAggregations().get(BY_CHILDREN);
		// extract the bucket where children count is 3
		Histogram.Bucket childrenBucket = childrenAggregation.getBucketByKey(3);
		
		Assert.assertEquals(expectedPeopleWith3Children, childrenBucket.getDocCount());
	}
	
}
