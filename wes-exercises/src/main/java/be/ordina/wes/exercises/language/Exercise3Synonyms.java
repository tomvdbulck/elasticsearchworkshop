package be.ordina.wes.exercises.language;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Language exercise #3: 
 * Synonyms
 */
public class Exercise3Synonyms {

	private static final Logger LOG = LoggerFactory.getLogger(Exercise3Synonyms.class);
	
	private Client client;
	
	public Exercise3Synonyms(Client client) {
		this.client = client;
	}
	
	/**
	 * Complete the index create function.
	 * 
	 * Create a folding analyzer which will filter on lowercase
	 * Create a nofolding analyzer which will do no filtering - important as the default will do the lowercase stuff
	 * 
	 * - an uppercase filter does not exist
	 * 
	 * 
	 * Next create an index on the field description using folding analyzer and another one using the nofolding analyzer.
	 * 
	 * You can go to Sense via 
	 * http://localhost:9200/_plugin/marvel/sense/index.html
	 * 
	 * Verify the settings:
	 * GET beersynonyms/_settings
	 * 
	 * To see the effect of the analyzers in Sense: 
	 * GET beersynonyms/_analyze?field=description&text=Duvel'
	 * GET beersynonyms/_analyze?field=brand&text=Duvel'
	 */
	
	public void createIndex(String indexName, String type) {
		try {
				List<String> foldingOption = new ArrayList<String>();
				foldingOption.add("lowercase");
	    	
	    		Settings settings = ImmutableSettings.settingsBuilder().loadFromSource(XContentFactory.jsonBuilder()
	            .startObject()
	                //TODO Add analyzer settings
	            	
	            .endObject().string()).build();
	    		
	    		//TODO complete the mapping
	    		XContentBuilder mapBuilder = XContentFactory.jsonBuilder()
				.startObject()
		            .startObject(type)
		                .startObject("properties")
		                	
		                .endObject()
			        .endObject()
			    .endObject();
	 
	    		
	    		client.admin().indices()
	    				.prepareCreate(indexName)
	    				.setSettings(settings)
	    				.addMapping("beer", mapBuilder)
	    				.get();
			
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
		
		LOG.info("Creating index [{}]", indexName);
	}

	/**
	 * Complete the index create function.
	 * 
	 * Create your own synonym filter
	 * 
	 * Create an analyzer which use the lowercase filter and you synonym filter
	 * Note: Pay attention to the order of these 2 !!!!
	 * 
	 * 
	 * Next create an index on the field description using folding analyzer and another one using the nofolding analyzer.
	 * 
	 * You can go to Sense via 
	 * http://localhost:9200/_plugin/marvel/sense/index.html
	 * 
	 * Verify the settings:
	 * GET beerownsynonyms/_settings
	 * 
	 * To see the effect of the analyzers in Sense: 
	 * GET beerownsynonyms/_analyze?field=description&text=Duvel'
	 * 
	 * GET beerownsynonyms/_analyze?field=description.synonym&text=Duvel'
	 */
	public void createIndexWithSynonyms(String indexName, String type) {
		try {
			//TODO define the foldingoptions
    		List<String> foldingOptions = new ArrayList<String>();
    		
        	
        	List<String> synonyms = new ArrayList<String>();
        	synonyms.add("Duvel,duiveltjesbier,duivels");
        	synonyms.add("grimbergen, abdij, nep, fake");
        	       	
    		Settings settings = ImmutableSettings.settingsBuilder().loadFromSource(XContentFactory.jsonBuilder()
    	            .startObject()
    	                //TODO Add analyzer settings
    	                .startObject("analysis")
    	                	
    	                .endObject()
    	            .endObject().string()).build();
    		
    		//TODO complete the mapping
    		XContentBuilder builder = XContentFactory.jsonBuilder()
    				.startObject()
    		            .startObject(type)
    		                .startObject("properties")
    		                	
    		                .endObject()
    			        .endObject()
    			    .endObject();
    		
    		
    		client.admin().indices()
    				.prepareCreate(indexName)
    				.setSettings(settings)
    				.addMapping(type, builder)
    				.get();
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    		LOG.error(e.getMessage());
    	}
    	
    	LOG.info("Creating index [{}]", indexName);
	}

}
