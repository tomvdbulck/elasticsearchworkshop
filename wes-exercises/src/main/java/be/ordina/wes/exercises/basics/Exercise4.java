package be.ordina.wes.exercises.basics;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise #4:
 * Updating data
 */
public class Exercise4 {

	private static final Logger LOG = LoggerFactory.getLogger(Exercise4.class);
	
	private static final String PERSON_INDEX = "person";
	private static final String PERSON_TYPE = "person";
	
	private Client client;
	
	public Exercise4(Client client) {
		this.client = client;
	}
	
	/**
	 * Updates a person document
	 * @param documentId ID of the document
	 * @param field Field to update
	 * @param value New value to store
	 */
	public void updatePerson(String documentId, String field, Object value) throws Exception {
		UpdateRequest updateRequest = null;
		// TODO-1: construct a new UpdateRequest, setting the index to 'person',
		// document type to 'person', and the provided documentId as ID,
		// and assign the operation to updateRequest.
		
		
		// builds a JSON document with the updated field/value
		updateRequest.doc(XContentFactory.jsonBuilder()
		        .startObject()
	            	.field(field, value)
	            .endObject());
		
		// executes the update operation
		client.update(updateRequest).get();
		
		LOG.debug("Updating document of type '{}' with ID '{}'", PERSON_TYPE, documentId);
	}
	
}
