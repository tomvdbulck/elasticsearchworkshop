package be.ordina.wes.core.service;

import java.util.List;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ordina.wes.core.config.ElasticsearchConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Performs various operations on the indices
 */
@Service
public class IndexServiceImpl implements IndexService {

	private static final Logger LOG = LoggerFactory.getLogger(IndexServiceImpl.class);
	
    @Autowired
	private Client client;
    
    @Autowired
    private ElasticsearchConfig esConfig;
    
    // JSON mapper
    ObjectMapper mapper = new ObjectMapper();
    
    @Override
	public void createIndex(String indexName) {
		client.admin().indices().prepareCreate(indexName).get();
		
		LOG.info("Creating index [" + indexName + "]");
	}

	@Override
	public void deleteIndex(String indexName) {
		client.admin().indices().prepareDelete(indexName).get();
		
		LOG.info("Deleting index [" + indexName + "]");
	}

	@Override
	public void refreshIndices() {
        client.admin().indices().prepareRefresh().get();
	}
	
	@Override
	public boolean indexExists(String indexName) {
		return client.admin().indices().prepareExists(indexName).get().isExists();
	}

	@Override
	public void index(Object object, String documentType) {
    	try {
			client.prepareIndex(esConfig.getIndexName(), documentType).setSource(mapper.writeValueAsString(object)).get();

			LOG.debug("indexing object into [" + esConfig.getIndexName() + "]");
		} catch (ElasticsearchException | JsonProcessingException e) {
			LOG.error("Exception", e);
		}
	}
    
	@Override
    public void indexBulk(List<?> objectList, String documentType) {
        try {
			BulkRequestBuilder bulkRequest = client.prepareBulk();
	        for (Object object : objectList) {
				IndexRequest indexRequest = new IndexRequest(esConfig.getIndexName(), documentType);
					indexRequest.source(mapper.writeValueAsString(object));
				bulkRequest.add(indexRequest);
			}
	        
			bulkRequest.get();
			
			LOG.debug("bulk indexing into [" + esConfig.getIndexName() + "]");
		} catch (JsonProcessingException e) {
			LOG.error("Exception", e);
		}
    }
    
}
