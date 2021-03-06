package be.ordina.wes.core.service;

import java.util.List;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
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
public class ElasticIndexService implements IndexService {

	private static final Logger LOG = LoggerFactory.getLogger(ElasticIndexService.class);
	
    @Autowired
	private Client client;
    
    @Autowired
    private ElasticsearchConfig esConfig;
    
    // JSON mapper
    private ObjectMapper mapper = new ObjectMapper();
    
    @Override
	public void createIndex(String indexName) {
		client.admin().indices().prepareCreate(indexName).get();
		
		LOG.info("Creating index [{}]", indexName);
	}

	@Override
	public boolean deleteIndex(String indexName) {
		boolean indexDeleted = false;
		
		if (indexExists(indexName)) {
			client.admin().indices().prepareDelete(indexName).get();
			
			indexDeleted = true;
		}
		
		if (indexDeleted) {
			LOG.info("Index [{}] deleted", indexName);
		}
		return indexDeleted;
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
    		String indexName = esConfig.getIndexName();
    		String jsonDoc = mapper.writeValueAsString(object);
			client.prepareIndex(indexName, documentType).setSource(jsonDoc).get();
			
			LOG.debug("indexing [{}] document into [{}]", documentType, indexName);
			LOG.trace("Indexed document: \n{}", jsonDoc);
		} catch (ElasticsearchException | JsonProcessingException e) {
			LOG.error("Exception", e);
		}
	}
    
	@Override
    public void indexBulk(List<?> objectList, String documentType) {
        indexBulk(objectList, documentType, esConfig.getIndexName());
    }
	
	@Override
	public void indexBulk(List<?> objectList, String documentType, String indexName) {
		try {
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			for (Object object : objectList) {
				IndexRequest indexRequest = new IndexRequest(indexName, documentType);
				indexRequest.source(mapper.writeValueAsString(object));
				bulkRequest.add(indexRequest);
			}
			
			bulkRequest.get();
			
			LOG.debug("bulk indexing [{}] documents into [{}]", documentType, indexName);
		} catch (JsonProcessingException e) {
			LOG.error("Exception", e);
		}
	}

	@Override
	public boolean remove(String docType, String id) {
		DeleteResponse response = client.prepareDelete(esConfig.getIndexName(), docType, id).get();
		return response.isFound();
	}
    
}
