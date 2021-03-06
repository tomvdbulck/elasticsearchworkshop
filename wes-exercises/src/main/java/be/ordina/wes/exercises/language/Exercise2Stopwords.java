package be.ordina.wes.exercises.language;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Language exercise #2: 
 * Stopwords
 */
public class Exercise2Stopwords {

	private static final Logger LOG = LoggerFactory.getLogger(Exercise2Stopwords.class);
	
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
	 * See if the index is created properly in Sense
	 * 
	 * To see if the settings have been configured properly in Sense: 
	 * GET beerstopwords/_settings
	 * 
	 * To test the effect of a stopword
	 * GET beerstopwords/_analyze?field=description&text=Duvel'
	 */
	
	public void createIndex(String indexName, String type, List<String> stopwords) {
		try {
			Settings settings = ImmutableSettings.settingsBuilder().loadFromSource(XContentFactory.jsonBuilder()
		            .startObject()
		                //TODO Add analyzer settings
		            	
		            .endObject().string()).build();
		    		
		    		
		    		XContentBuilder mapBuilder = XContentFactory.jsonBuilder()
					.startObject()
			            .startObject(type)
			                .startObject("properties")
			                    .startObject("description")
			                    	.field("type", "string")
			                    	//TODO add the analyzer
			                    	
			                     .endObject()
			                .endObject()
				        .endObject()
				    .endObject();
		 
		    		//TODO complete the index creation by adding settings to it
		    		client.admin().indices()
		    				.prepareCreate(indexName)
		    				
		    				.addMapping(type, mapBuilder)
		    				.get();
			
			
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
		
		LOG.info("Creating index [{}]", indexName);
	}
}
