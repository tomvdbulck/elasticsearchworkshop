package be.ordina.wes.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ElasticsearchConfig {

	private static final String INDEX_NAME = "elasticsearch.index.name";
	
	@Autowired
	private Environment env;
    
    public String getIndexName() {
    	return env.getProperty(INDEX_NAME);
    }
    
}
