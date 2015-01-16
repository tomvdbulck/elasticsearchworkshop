package be.ordina.wes.exercises.basics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise #2:
 * Creating index & indexing data
 */
public class Exercise2 {

	private static final Logger LOG = LoggerFactory.getLogger(Exercise2.class);
	
	private static final String PERSON_FILE = "../sample_data/person.json";
	private static final String TWITTER_FILE = "../sample_data/tweet.json";
	private static final String TWITTER_INDEX = "twitter";
	private static final String TWEET_TYPE = "tweet";
	
	private static Client client = Exercise1.getInstance();
	
	public static void indexOneDocument() throws IOException {
		// read data from JSON file
		String jsonDocument = new String(Files.readAllBytes(Paths.get(TWITTER_FILE)));
		
		client.prepareIndex(TWITTER_INDEX, TWEET_TYPE)
				.setSource(jsonDocument)
				.get();

		LOG.info("indexing '{}' type document into '{}' index", TWEET_TYPE, TWITTER_INDEX);
	}
	
	public static void indexMultipleDocuments() throws Exception {
		// read data from JSON file
		byte[] jsonData = Files.readAllBytes(Paths.get(PERSON_FILE));
		
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		bulkRequest.add(jsonData, 0, jsonData.length, true);
        
		// perform the bulk operation
		bulkRequest.get();
	}

	/**
	 * Create an index
	 * @param indexName Index to create
	 */
	public static void createIndex(String indexName) {
		client.admin().indices().prepareCreate(indexName).get();
		
		LOG.info("Index with name '{}' successfully created", indexName);
	}
	
	/**
	 * Delete an index
	 * @param indexName Index to delete
	 * @return true if the response is acknowledged
	 */
	public static boolean deleteIndex(String indexName) {
		DeleteIndexResponse response = client.admin().indices().prepareDelete(indexName).get();
		
		boolean indexDeleted = response.isAcknowledged();
		
		if (indexDeleted) {
			LOG.info("Index with name '{}' successfully deleted", indexName);
		} else {
			LOG.error("Failed to deleted index with name '{}'", indexName);
		}
		return indexDeleted;
	}
	
	/**
	 * Explicitly refresh all indices (making the content 
	 * indexed since the last refresh searchable).
	 */
	public static void refreshIndex() {
		client.admin().indices().prepareRefresh().get();
	}
}
