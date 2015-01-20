package be.ordina.wes.core.service;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
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
public class LanguageServiceImpl implements LanguageService {

	private static final Logger LOG = LoggerFactory.getLogger(LanguageServiceImpl.class);
	
    @Autowired
	private Client client;
    
    @Autowired
    private ElasticsearchConfig esConfig;
    
    // JSON mapper
    private ObjectMapper mapper = new ObjectMapper();
    
    @Override
	public void createIndex(String indexName) {
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder()
				.startObject()
		            .startObject("beer")
		                .startObject("properties")
		                    .startObject("description")
		                    	.field("type", "string")
		                    	.field("analyzer", "dutch")
		                     .endObject()
		                .endObject()
			        .endObject()
			    .endObject();
		
			
			
			System.out.println(builder.toString());
			
			
			client.admin().indices().prepareCreate(indexName).addMapping("beer", builder)
			.execute().actionGet();
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
		
		
		LOG.info("Creating index [{}]", indexName);
	}
    
    @Override
    public void createIndexWithoutAnalyzer(String indexName) {
    	try {
    		XContentBuilder builder = XContentFactory.jsonBuilder()
				.startObject()
		            .startObject("beer")
		                .startObject("properties")
		                    .startObject("description")
		                    	.field("type", "string")
		                    	.field("analyzer", "whitespace")
		                     .endObject()
		                .endObject()
			        .endObject()
			    .endObject();
    		
    		client.admin().indices().prepareCreate(indexName).addMapping("beer", builder)
    		.execute().actionGet();
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    		LOG.error(e.getMessage());
    	}
    	
    	
    	LOG.info("Creating index [{}]", indexName);
    }

	@Override
	public void deleteIndex(String indexName) {
		client.admin().indices().prepareDelete(indexName).get();
		
		LOG.info("Deleting index [{}]", indexName);
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
	public void index(Object object, String documentType, String indexName) {
		try {
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
        try {
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			String indexName = esConfig.getIndexName();
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
