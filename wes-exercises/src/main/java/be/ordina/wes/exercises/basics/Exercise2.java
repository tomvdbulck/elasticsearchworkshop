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
	
	private static final String PERSON_FILE = "src/test/resources/person.json";
	private static final String TWITTER_FILE = "src/test/resources/tweet.json";
	private static final String TWITTER_INDEX = "twitter";
	private static final String TWEET_TYPE = "tweet";
	
	private static Client client = Exercise1.getInstance();
	
	public static void indexOneDocument() throws IOException {
		// reads data from JSON file
		String jsonDocument = new String(Files.readAllBytes(Paths.get(TWITTER_FILE)));
		
		// TODO-1: index the above JSON document into 'twitter' index 
		// and specify document type as 'tweet'. Load the document using setSource.
		

		LOG.info("indexing '{}' type document into '{}' index", TWEET_TYPE, TWITTER_INDEX);
	}
	
	public static void indexMultipleDocuments() throws Exception {
		// reads data from JSON file
		byte[] jsonData = Files.readAllBytes(Paths.get(PERSON_FILE));
		
		// TODO-2: WORK IN PROGRESS
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		bulkRequest.add(jsonData, 0, jsonData.length, true);
        
		// executes the bulk operation
		bulkRequest.get();
	}

	/**
	 * Create an index
	 * @param indexName Index to create
	 */
	public static void createIndex(String indexName) {
		// TODO-3: do an index creation operation (prepareCreate) using indexName
		// as index name.
		// Make use of the admin client (i.e. client.admin())
		
		
		LOG.info("Index with name '{}' successfully created", indexName);
	}
	
	/**
	 * Delete an index
	 * @param indexName Index to delete
	 * @return true if the response is acknowledged
	 */
	public static boolean deleteIndex(String indexName) {
		DeleteIndexResponse response = null;
		// TODO-4: delete an index with the name indexName and assign the action 
		// to the response variable.
		// Make use of the admin client for index creation/deletion operations.
		
		
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
	
	/**
	 * Check if index exists
	 * @param indexName The index name
	 * @return true if index exists
	 */
	public static boolean indexExists(String indexName) {
		return client.admin().indices().prepareExists(indexName).get().isExists();
	}
}
