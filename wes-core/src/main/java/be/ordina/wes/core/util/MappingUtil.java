package be.ordina.wes.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import com.fasterxml.jackson.core.JsonParseException;
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
	
	public static <T> List<T> getObjects(SearchResponse response, Class<T> objectType) throws JsonParseException, JsonMappingException, IOException {
		// configure mapper not to fail on unrecognized fields
		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		List<T> results = new ArrayList<>();
		
        if (response != null) {
            for (SearchHit hit : response.getHits()) {
			    results.add(getObject(hit, objectType));
		    }
        }
		return results;
	}

    private static <T> T getObject(SearchHit hit, Class<T> objectType) throws JsonParseException, JsonMappingException, IOException {
		return MAPPER.readValue(hit.getSourceAsString(), objectType);
	}

}