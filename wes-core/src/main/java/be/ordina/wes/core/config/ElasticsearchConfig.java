package be.ordina.wes.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ElasticsearchConfig {

	private static final String CLUSTER_NAME = "elasticsearch.cluster.name";
	private static final String NODE_NAME = "elasticsearch.node.name";
	private static final String NODE_MASTER = "elasticsearch.node.master";
	private static final String NODE_LOCAL = "elasticsearch.node.local";
	private static final String INDEX_NAME = "elasticsearch.index.name";
	private static final String SERVER_HOST = "elasticsearch.host";
	private static final String SERVER_PORT = "elasticsearch.port";
	
	@Autowired
	private Environment env;
    
	
    public String getClusterName() {
    	return env.getProperty(CLUSTER_NAME);
    }
    
    public String getNodeName() {
    	return env.getProperty(NODE_NAME);
    }
    
    public String getNodeMaster() {
    	return env.getProperty(NODE_MASTER);
    }
    
    public boolean isNodeLocal() {
    	return Boolean.getBoolean(env.getProperty(NODE_LOCAL));
    }

    public String getIndexName() {
    	return env.getProperty(INDEX_NAME);
    }
    
    public String getServerHost() {
    	return env.getProperty(SERVER_HOST);
    }
    
    public int getServerPort() {
    	return Integer.parseInt(env.getProperty(SERVER_PORT));
    }
    
}
