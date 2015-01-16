package be.ordina.wes.exercises.basics;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise #5:
 * Deleting data
 */
public class Exercise5 {

	private static final Logger LOG = LoggerFactory.getLogger(Exercise5.class);
	
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
		
		boolean documentDeleted = response.isFound();
		
		if (documentDeleted) {
			LOG.info("Document with ID '{}' successfully deleted", documentId);
		} else {
			LOG.error("Document with ID '{}' not found", documentId);
		}
		return documentDeleted;
	}
	
}
