package be.ordina.wes.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Util for mapping search results into objects
 */
public final class MappingUtil {
  
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	/**
	 * private constructor
	 */
	private MappingUtil() {
		super();
	}
	
	/**
	 * Maps search results into a list of objects
	 * @param response Search response from Elasticsearch
	 * @param objectType Object type to map objects into
	 * @return List of objects
	 */
	public static <T> List<T> mapSearchResults(SearchResponse response, Class<T> objectType) throws JsonParseException, JsonMappingException, IOException {
		// configure mapper not to fail on unrecognized fields
		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		List<T> results = new ArrayList<>();
		
        if (response != null) {
            for (SearchHit hit : response.getHits()) {
			    results.add(fromJson(hit.getSourceAsString(), objectType));
		    }
        }
		return results;
	}
	
	/**
	 * Maps object into a JSON string
	 * @param obj Object to map
	 * @return JSON string
	 */
	public static String toJson(Object obj) throws JsonProcessingException {
		return MAPPER.writeValueAsString(obj);
	}
	
	/**
	 * Maps JSON string into an object
	 * @param json JSON string
	 * @param objectType Object type
	 * @return Mapped object
	 */
	public static <T> T fromJson(String json, Class<T> objectType) throws JsonParseException, JsonMappingException, IOException {
		return MAPPER.readValue(json, objectType);
	}
}