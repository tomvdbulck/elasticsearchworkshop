package be.ordina.wes.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ordina.wes.common.util.MappingUtil;
import be.ordina.wes.core.config.ElasticsearchConfig;

/**
 * Performs search operations on the indices
 */
@Service
public class ElasticSearchService<T> implements SearchService<T> {

	private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchService.class);
	
	private static final int MAX_RESULTS = 100;
	
    @Autowired
	private Client client;
    
    @Autowired
    private ElasticsearchConfig esConfig;

	@Override
	public List<T> find(String searchTerm, String documentType, Class<T> objectType) {
        List<T> resultList = new ArrayList<>();

        try {
        	String indexName = esConfig.getIndexName();
	        SearchRequestBuilder requestBuilder = buildSearchRequest(indexName, searchTerm, documentType);
	        LOG.trace("Search request: \n{}", requestBuilder.toString());
	        
	        SearchResponse response = client.search(requestBuilder.request()).get();
			LOG.trace("Search response: \n{}", response.toString());
        
			resultList = MappingUtil.mapSearchResults(response, objectType);

		} catch (InterruptedException | ExecutionException | IOException e) {
			LOG.error("Exception", e);
		}
		
        return resultList;
    }
	
    private SearchRequestBuilder buildSearchRequest(String indexName, String searchTerm, String documentType) {
        QueryBuilder query = buildQuery(searchTerm);
        SearchRequestBuilder searchRequest = client.prepareSearch()
        		.setIndices(indexName)
        		.setTypes(documentType)
        		.setQuery(query)
        		.setSize(MAX_RESULTS); // defaults to 10 search results
        
		return searchRequest;
	}

	private QueryBuilder buildQuery(String searchTerm) {
		if (searchTerm.isEmpty()) {
			// return all results if search term is empty
			return QueryBuilders.matchAllQuery();
		} else {
			// _all indicates that search should be performed on all fields
            return QueryBuilders.matchQuery("_all", searchTerm);
		}
	}
	
}
