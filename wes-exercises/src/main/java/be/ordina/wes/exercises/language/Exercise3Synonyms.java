package be.ordina.wes.exercises.language;

import java.io.IOException;
import java.util.ArrayList;
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
public class Exercise3Synonyms {

	private static final Logger LOG = LoggerFactory.getLogger(Exercise1DefaultLanguageAnalyzerTest.class);
	
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
	 * - there does not exist an uppercase filter
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
	 * 
	 * @param indexName
	 */
	
	
	public void createIndex(String indexName, String type) {
		try {
				List<String> foldingOption = new ArrayList<String>();
				foldingOption.add("lowercase");
	    	
	    		Settings settings = ImmutableSettings.settingsBuilder().loadFromSource(XContentFactory.jsonBuilder()
	            .startObject()
	                //Add analyzer settings
	                .startObject("analysis")
	                	.startObject("analyzer")
	                		.startObject("folding")
	                            .field("tokenizer", "standard")
	                            .field("filter",foldingOption)
	                        .endObject()
	                       .startObject("nofolding")
	                            .field("tokenizer", "standard")
	                            .field("filter","")
	                        .endObject()
	                   .endObject()
	                .endObject()
	            .endObject().string()).build();
	    		
	    		XContentBuilder mapBuilder = XContentFactory.jsonBuilder()
				.startObject()
		            .startObject(type)
		                .startObject("properties")
		                    .startObject("description")
		                    	.field("type", "string")
		                    	.field("analyzer", "folding")
		                     .endObject()
		                     .startObject("brand")
			    				.field("type", "string")
			    				.field("analyzer", "nofolding")
		    				.endObject()
		                .endObject()
			        .endObject()
			    .endObject();
	 
	    		CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices()
	    				.prepareCreate(indexName)
	    				.setSettings(settings)
	    				.addMapping("beer", mapBuilder);
	    		createIndexRequestBuilder.execute().actionGet();
			
			
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
	 * Pay attention to the order of these 2 !!!!
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
    		List<String> foldingOptions = new ArrayList<String>();
        	foldingOptions.add("my_synonym_filter");
        	foldingOptions.add("lowercase");
        	
        	List<String> synonyms = new ArrayList<String>();
        	synonyms.add("Duvel,duiveltjesbier,duivels");
        	synonyms.add("grimbergen, abdij, nep, fake");
        	       	
    		Settings settings = ImmutableSettings.settingsBuilder().loadFromSource(XContentFactory.jsonBuilder()
    	            .startObject()
    	                //Add analyzer settings
//    	                .startObject("analysis")
//    	                	.startObject("filter")
//    	                		.startObject("my_synonym_filter")
//    	                			.field("type", "synonym")
//    	                			.field("synonyms", synonyms)
//    	                		.endObject()
//    	                	.endObject()
//    	                	.startObject("analyzer")
//    	                		.startObject("mysynonyms")
//			    					.field("tokenizer", "standard")
//			    					.field("filter",foldingOptions)
//			    				.endObject()
//    	                   .endObject()
//    	                .endObject()
    	            .endObject().string()).build();
    		
    		XContentBuilder builder = XContentFactory.jsonBuilder()
    				.startObject()
    		            .startObject(type)
    		                .startObject("properties")
    		                   .startObject("description")
			                    	.field("type", "string")
			                    	.field("analyzer", "standard")
//			                    	.startObject("fields")
//				                    	.startObject("synonym")
//				                    		.field("type", "string")
//				                    		.field("analyzer", "mysynonyms")
//				                    	.endObject()
//				                     .endObject()
			                     .endObject()
    		                .endObject()
    			        .endObject()
    			    .endObject();
    		
    		CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices()
    				.prepareCreate(indexName)
    				.setSettings(settings)
    				.addMapping(type, builder);
    		
    		createIndexRequestBuilder.execute().actionGet();
    		
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    		LOG.error(e.getMessage());
    	}
    	
    	
    	LOG.info("Creating index [{}]", indexName);
		
	}


}
