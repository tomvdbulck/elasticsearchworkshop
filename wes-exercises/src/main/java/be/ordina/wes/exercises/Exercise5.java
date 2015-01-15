package be.ordina.wes.exercises;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.Client;

/**
 * Exercise #5:
 * Deleting data
 */
public class Exercise5 {

	private static final String PERSON_INDEX = "person";
	private static final String PERSON_TYPE = "person";
	
	private static Client client = Exercise1.getInstance();
	
	/**
	 * Delete person from the index
	 * @param documentId ID of the document
	 * @return true if a doc was found to delete
	 */
	public static boolean deletePerson(String documentId) {
		DeleteResponse response = client.prepareDelete()
				.setIndex(PERSON_INDEX)
				.setType(PERSON_TYPE)
				.setId(documentId)
				.get();
		
		return response.isFound();
	}
	
	/**
	 * Delete an index
	 * @param indexName Index to delete
	 * @return true if the response is acknowledged
	 */
	public static boolean deleteIndex(String indexName) {
		DeleteIndexResponse response = client.admin().indices().prepareDelete(indexName).get();
		
		return response.isAcknowledged();
	}
	
}
