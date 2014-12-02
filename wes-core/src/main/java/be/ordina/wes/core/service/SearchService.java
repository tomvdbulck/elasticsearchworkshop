package be.ordina.wes.core.service;

import java.util.List;

/**
 * Used for performing search operations on indices
 */
public interface SearchService<T> {

	/**
	 * Perform search on all indices
	 * @param searchTerm The search term
	 * @param objectType Object type to search for
	 * @return Results from the search query
	 */
    List<T> find(String searchTerm, Class<T> objectType);

}