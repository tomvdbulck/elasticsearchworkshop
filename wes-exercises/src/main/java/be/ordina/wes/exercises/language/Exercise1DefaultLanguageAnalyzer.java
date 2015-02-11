package be.ordina.wes.exercises.language;

import java.io.IOException;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise #1: 
 * Connecting to Elasticsearch cluster
 */
public class Exercise1DefaultLanguageAnalyzer {

	private static final Logger LOG = LoggerFactory.getLogger(Exercise1DefaultLanguageAnalyzerTest.class);
	
	private Client client;
	
	public Exercise1DefaultLanguageAnalyzer(Client client) {
		this.client = client;
	}
	
	
	/**
	 * Complete the index create function.
	 * 
	 * Add the default "standard" analyzer to the "description" property.
	 * Add a subfield to that field named "dutch" which will use the dutch analyzer
	 * 
	 * You can go to Sense via 
	 * http://localhost:9200/_plugin/marvel/sense/index.html
	 * 
	 * See if the index is create properly in Sense via
	 * 
	 * To see the effect of the analyzer in Sense: 
	 * GET beerdefaultlanguage/_analyze?field=description&text=duvel+is+een+de+het+bier
	 * GET beerdefaultlanguage/_analyze?field=description.dutch&text=duvel+is+een+de+het+bier
	 * 
	 * @param indexName
	 */
	
	
	public void createIndex(String indexName, String type) {
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder()
				.startObject()
		            .startObject(type)
		                .startObject("properties")
		                    .startObject("description")
		                    	.field("type", "string")
		                    	.field("analyzer", "standard")
//		                    	.startObject("fields")
//			                    	.startObject("dutch")
//			                    		.field("type", "string")
//			                    		.field("analyzer", "dutch")
//			                    	.endObject()
//			                     .endObject()
		                     .endObject()
		                .endObject()
			        .endObject()
			    .endObject();
		
			
			
			
			client.admin().indices().prepareCreate(indexName).addMapping(type, builder)
				.execute().actionGet();
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
		
		
		LOG.info("Creating index [{}]", indexName);
	}
}
