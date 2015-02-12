package be.ordina.wes.exercises.language;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise #1: 
 * Connecting to Elasticsearch cluster
 */
public class Exercise2Stopwords {

	private static final Logger LOG = LoggerFactory.getLogger(Exercise1DefaultLanguageAnalyzerTest.class);
	
	private Client client;
	
	public Exercise2Stopwords(Client client) {
		this.client = client;
	}
	
	
	/**
	 * Complete the index create function.
	 * 
	 * Create an analyzer with the stopwords being passed in (via the settings configuration of an index)
	 * 
	 * Next create an index on the field description using the analyzer you just configured.
	 * 
	 * You can go to Sense via 
	 * http://localhost:9200/_plugin/marvel/sense/index.html
	 * 
	 * See if the index is create properly in Sense via
	 * 
	 * To see if the settings have been configured properly in Sense: 
	 * GET beerstopwords/_settings
	 * 
	 * To test the effect of a stopword
	 * GET beerstopwords/_analyze?field=description&text=Duvel'
	 * 
	 * @param indexName
	 */
	
	
	public void createIndex(String indexName, String type, List<String> stopwords) {
		try {
			Settings settings = ImmutableSettings.settingsBuilder().loadFromSource(XContentFactory.jsonBuilder()
		            .startObject()
		                //TODO Add analyzer settings
		            
//		                .startObject("analysis")
//		                	.startObject("analyzer")
//		                		.startObject("my_analyzer")
//		                            .field("type", "standard")
//		                            .field("stopwords",stopwords)
//		                        .endObject()
//		                   .endObject()
//		                .endObject()
		            .endObject().string()).build();
		    		
		    		
		    		XContentBuilder mapBuilder = XContentFactory.jsonBuilder()
					.startObject()
			            .startObject(type)
			                .startObject("properties")
			                    .startObject("description")
			                    	.field("type", "string")
			                    	//TODO add the analyzer
			                    	
//			                    	.field("analyzer", "my_analyzer")
			                     .endObject()
			                .endObject()
				        .endObject()
				    .endObject();
		 
		    		//TODO complete the createIndexRequest
		    		CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices()
		    				.prepareCreate(indexName)
//		    				.setSettings(settings)
		    				.addMapping(type, mapBuilder);
		    		createIndexRequestBuilder.execute().actionGet();	
			
			
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
		
		
		LOG.info("Creating index [{}]", indexName);
	}
}
