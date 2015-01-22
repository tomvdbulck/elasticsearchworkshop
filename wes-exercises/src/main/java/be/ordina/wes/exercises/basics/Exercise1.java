package be.ordina.wes.exercises.basics;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise #1: 
 * Connecting to Elasticsearch cluster
 */
public class Exercise1 {

	private static final Logger LOG = LoggerFactory.getLogger(Exercise1.class);
	
	private static final String SERVER_HOST = "192.168.33.10";
	private static final int SERVER_PORT = 9300;
	private static final String CLUSTER_NAME = "ordina-dev-001";
	private static final String NODE_NAME = "ordiNode";
	
	private static Client client;
	
	public static Client getInstance() {
		if (client == null) {
			final Settings settings = ImmutableSettings.settingsBuilder()
					// TODO-1: put "cluster.name" and "node.name" properties here.
					// Use the values provided above.
					// Tip for all exercises: check that no variables/imports 
					// are left unused (underlined yellow in Eclipse).
					
					.build();
			
			// TODO-2: construct a new TransportClient with the created settings, 
			// add a transport address with the provided server host and port to it (hint: use InetSocketTransportAddress).
			// Assign the transport client to the client variable.
			
			
			LOG.debug("Starting up Elasticsearch transport client");
		}
		return client;
	}
	
	public static void destroyInstance() {
		client.close();
		
		LOG.debug("Closing Elasticsearch transport client instance");
	}

}
