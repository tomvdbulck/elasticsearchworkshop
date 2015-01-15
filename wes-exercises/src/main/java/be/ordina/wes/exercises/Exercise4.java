package be.ordina.wes.exercises;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;

/**
 * Exercise #4:
 * Updating data
 */
public class Exercise4 {
	
	private static final String PERSON_INDEX = "person";
	private static final String PERSON_TYPE = "person";
	private static final String PERSON_ID = "-QnBKlbHRPOO9pbebQMDjw";
	
	private static Client client = Exercise1.getInstance();
	
	public static void updatePerson() throws IOException, InterruptedException, ExecutionException {
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index(PERSON_INDEX);
		updateRequest.type(PERSON_TYPE);
		updateRequest.id(PERSON_ID);
		
		updateRequest.doc(XContentFactory.jsonBuilder()
		        .startObject()
	            	.field("children", 4)
	            .endObject());
		
		client.update(updateRequest).get();
	}
	
}
