package be.ordina.wes.core.service;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Factory for constructing an Elasticsearch transport client. 
 * The TransportClient connects remotely to an elasticsearch cluster using the transport module. 
 * It does not join the cluster, but simply gets one or more initial transport addresses and 
 * communicates with them in round robin fashion on each action.
 */
@Service
public class TransportClientFactory implements ClientFactory {

	private static final Logger LOG = LoggerFactory.getLogger(TransportClientFactory.class);

	private static final String CLUSTER_NAME = "elasticsearch.cluster.name";
	private static final String NODE_NAME = "elasticsearch.node.name";
	private static final String SERVER_HOST = "elasticsearch.host";
	private static final String SERVER_PORT = "elasticsearch.port";
	
	private Client client;
	
	@Autowired
	private Environment env;

	@Override
	public Client getInstance() {
		final Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", env.getProperty(CLUSTER_NAME))
                .put("node.name", env.getProperty(NODE_NAME))
				.build();
		
		String serverHost = env.getProperty(SERVER_HOST);
		int serverPort = Integer.parseInt(env.getProperty(SERVER_PORT));
		
		client = new TransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(serverHost, serverPort));
		
		LOG.debug("Starting up Elasticsearch transport client...");
		
		return client;
	}

	@Override
	public void destroyInstance() {
        try {
            LOG.info("Closing ElasticSearch transport client");
            
            client.close();
	    } catch (final Exception e) {
	    	LOG.error("Error closing Elasticsearch transport client", e);
	    }
	}

}
