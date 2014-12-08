package be.ordina.wes.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("elasticsearch.properties")
public class ElasticsearchConfig {

	private static final String ES_INDEX_NAME = "elasticsearch.index.name";
	
	@Autowired
	private Environment environment;
    
    public String getIndexName() {
    	return environment.getProperty(ES_INDEX_NAME);
    }
    
}
