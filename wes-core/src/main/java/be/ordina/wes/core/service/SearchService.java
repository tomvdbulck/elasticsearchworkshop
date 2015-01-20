package be.ordina.wes.core.service;

import java.util.List;

/**
 * Used for performing search operations on indices
 */
public interface SearchService<T> {

	/**
	 * Perform search on default index
	 * @param searchTerm The search term
	 * @param documentType Document type
	 * @param objectType Object type to search for
	 * @return Results from the search query
	 */
    List<T> find(String searchTerm, String documentType, Class<T> objectType);

    /**
     * Perform search on the passed through index
     * @param searchTerm
     * @param documentType
     * @param objectType
     * @param indexName
     * @param exactQuery
     * @return Results from the search query
     */
	List<T> find(String searchTerm, String documentType, Class<T> objectType, String indexName, boolean exactQuery, String fieldName);

}