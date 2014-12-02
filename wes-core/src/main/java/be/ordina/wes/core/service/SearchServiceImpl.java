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

import be.ordina.wes.core.config.ElasticsearchConfig;
import be.ordina.wes.core.util.MappingUtil;

/**
 * Performs search operations on the indices
 */
@Service
public class SearchServiceImpl<T> implements SearchService<T> {

	private static final Logger LOG = LoggerFactory.getLogger(SearchServiceImpl.class);
	
    @Autowired
	private Client client;
    
    @Autowired
    private ElasticsearchConfig esConfig;

	@Override
	public List<T> find(String searchTerm, Class<T> objectType) {
        List<T> resultList = new ArrayList<>();

        try {
	        SearchRequestBuilder request;
	        SearchResponse response;
	
	        request = buildSearchRequest(searchTerm);

			response = client.search(request.request()).get();
        
			resultList = MappingUtil.getObjects(response, objectType);

		} catch (InterruptedException | ExecutionException | IOException e) {
			LOG.error("Exception", e);
		}
		
        return resultList;
    }

    private SearchRequestBuilder buildSearchRequest (String searchTerm) {
        QueryBuilder query = buildQuery(searchTerm);
        SearchRequestBuilder searchRequest = client.prepareSearch().setQuery(query);

		return searchRequest;
	}

	private QueryBuilder buildQuery (String searchTerm) {
		if (searchTerm.isEmpty()) {
			return QueryBuilders.matchAllQuery();
		} else {
            return QueryBuilders.matchQuery("_all", searchTerm);
		}
	}
	
}
