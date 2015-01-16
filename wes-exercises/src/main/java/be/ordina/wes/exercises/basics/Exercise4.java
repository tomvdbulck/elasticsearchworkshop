package be.ordina.wes.exercises.basics;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

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
	
	private static Client client = Exercise1.getInstance();
	
	/**
	 * Updates a person document
	 * @param documentId ID of the document
	 * @param field Field to update
	 * @param value New value to store
	 */
	public static void updatePerson(String documentId, String field, Object value) throws IOException, InterruptedException, ExecutionException {
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index(PERSON_INDEX);
		updateRequest.type(PERSON_TYPE);
		updateRequest.id(documentId);
		
		updateRequest.doc(XContentFactory.jsonBuilder()
		        .startObject()
	            	.field(field, value)
	            .endObject());
		
		client.update(updateRequest).get();
		
		LOG.debug("Updating document of type '{}' with ID '{}'", PERSON_TYPE, documentId);
	}
	
}
