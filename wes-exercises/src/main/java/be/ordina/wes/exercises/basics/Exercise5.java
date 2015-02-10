package be.ordina.wes.exercises.basics;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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
	private static final String DATE_OF_BIRTH_FIELD = "dateOfBirth";
	
	private Client client;
	
	public Exercise5(Client client) {
		this.client = client;
	}
	
	/**
	 * Delete person from the index
	 * @param documentId ID of the document
	 * @return true if a doc was found to delete
	 */
	public boolean deletePerson(String documentId) {
		DeleteResponse response = null;
		// TODO-1: prepare a delete operation, setting the index to 'person',
		// document type to 'person', and the provided documentId as ID,
		// and assign the operation to response.
		
		
		boolean documentDeleted = response.isFound();
		
		if (documentDeleted) {
			LOG.info("Deleting document with ID '{}'", documentId);
		} else {
			LOG.error("Document with ID '{}' not found", documentId);
		}
		return documentDeleted;
	}
	
	/**
	 * Delete all people born before specific date
	 * @param date Date (exclusive)
	 */
	public void deletePeopleBornBefore(String date) {
		QueryBuilder query = null;
		// TODO-2: build a range query (using QueryBuilders), 
		// where dateOfBirth is smaller than the provided date (dateOfBirth < date)
		// Assign the operation to the query variable.
		
		
		// TODO-3: prepare a deleteByQuery operation on 'person' index,
		// and set the previously built query using setQuery.
		
		
		LOG.info("Deleting '{}' documents where '{}' is before '{}'", PERSON_TYPE, DATE_OF_BIRTH_FIELD, date);
	}
	
}
