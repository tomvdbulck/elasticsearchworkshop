package be.ordina.wes.exercises;

import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import be.ordina.wes.common.util.MappingUtil;
import be.ordina.wes.exercises.model.Person;

/**
 * Exercise #3:
 * Searching data
 */
public class Exercise3 {
	
	private static final String PERSON_INDEX = "person";
	private static final int MAX_RESULTS = 50;

	private static Client client = Exercise1.getInstance();
	
	public static List<Person> searchPersons() throws Exception {
		Exercise2.indexMultipleDocuments();
		
		// refresh the index prior to performing search operations
		client.admin().indices().prepareRefresh().get();
		
		QueryBuilder query = QueryBuilders.matchQuery("name", "Scarlett");
		
		SearchRequest request = client.prepareSearch()
				.setIndices(PERSON_INDEX)
				.setSize(MAX_RESULTS)
				.setQuery(query)
				.request();
		
		SearchResponse response = client.search(request).get();
		
		List<Person> personList = MappingUtil.getObjects(response, Person.class);
		
		return personList;
	}
}
