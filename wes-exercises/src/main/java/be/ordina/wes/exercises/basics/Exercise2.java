package be.ordina.wes.exercises.basics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
		String jsonDocument = loadFile(TWITTER_FILE);
		
		// TODO-1: index the above JSON document into 'twitter' index (prepareIndex),
		// and specify document type as 'tweet'. Load the document using setSource.
		

		LOG.info("indexing '{}' type document into '{}' index", TWEET_TYPE, TWITTER_INDEX);
	}
	
	public static void indexMultipleDocuments() throws Exception {
		// reads data from JSON file
		byte[] jsonData = Files.readAllBytes(Paths.get(PERSON_FILE));
		
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
		// TODO-2: do an index creation operation (prepareCreate) using indexName
		// as index name.
		// Make use of the admin client (i.e. client.admin())
		
		
		LOG.info("Index with name '{}' created", indexName);
	}
	
	/**
	 * Delete an index
	 * @param indexName Index to delete
	 * @return true if the index was deleted
	 */
	public static boolean deleteIndex(String indexName) {
		boolean indexDeleted = false;
		
		if (indexExists(indexName)) {
			// TODO-3: delete an index with the name indexName.
			// Make use of the admin client for index creation/deletion operations.
			
			
			indexDeleted = true;
		}
		
		if (indexDeleted) {
			LOG.info("Index with name '{}' deleted", indexName);
		} else {
			LOG.warn("Index with name '{}' was not found", indexName);
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
	
	/**
	 * Loads file from the specified path
	 * @param filePath The file path
	 * @return Contents of the file as string
	 */
	private static String loadFile(String filePath) throws IOException {
		return new String(Files.readAllBytes(Paths.get(TWITTER_FILE)));
	}
}
