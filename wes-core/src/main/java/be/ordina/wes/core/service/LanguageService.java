package be.ordina.wes.core.service;

import java.util.List;

/**
 * Used for performing operations on indices
 */
public interface LanguageService {
	
	/**
	 * Index one object
	 * @param object Object to index
	 * @param documentType Document type
	 */
	void index(Object object, String documentType);
	
	/**
	 * Remove an object from the index
	 * @param documentType Document type
	 * @param id Document ID
	 * @return true if a document was found to delete.
	 */
	boolean remove(String documentType, String id);
	
	/**
	 * Do a bulk index
	 * @param objectList A list of objects
	 * @param documentType Document type
	 */
	void indexBulk(List<?> objectList, String documentType);
	
    /**
     * Create an index
     * @param indexName Name of the index
     */
	void createIndex(String indexName);
	
	/**
	 * Delete an index
	 * @param indexName Name of the index
	 */
	void deleteIndex(String indexName);
	
	/**
	 * Refresh all indices
	 */
	void refreshIndices();
	
    /**
     * Check if index exists
     * @param indexName Name of the index
     * @return True if index exists, otherwise false
     */
	boolean indexExists(String indexName);

	void createIndexWithoutAnalyzer(String indexName);

	/**
	 * index bulk on a given index
	 * 
	 * @param objectList
	 * @param documentType
	 * @param indexName
	 */
	void indexBulk(List<?> objectList, String documentType, String indexName);

	/**
	 * index a single object on a given index
	 * @param object
	 * @param documentType
	 * @param indexName
	 */
	void index(Object object, String documentType, String indexName);

	void createIndexWithStopword(String indexName, List<String> stopword);

	void createIndexWithOutNormalizing(String indexName);

	void createIndexWithNormalizingLowerCase(String indexName);

	void createIndexWithFolding(String indexName);
	
	void createIndexWithSynonyms(String indexName);
	
	
}
