package be.ordina.wes.exercises.util;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Client;

public final class PersonUtil {

	private static final String PERSON_FILE = "src/test/resources/person.json";

	private PersonUtil() { }
	
	public static void indexPersonDocuments(Client client) throws Exception {
		// reads data from JSON file
		byte[] jsonData = Files.readAllBytes(Paths.get(PERSON_FILE));
		
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		bulkRequest.add(jsonData, 0, jsonData.length, true);
        
		// executes the bulk operation
		bulkRequest.get();
	}
}
