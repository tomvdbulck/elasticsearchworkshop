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
					.put("cluster.name", CLUSTER_NAME)
	                .put("node.name", NODE_NAME)
					.build();
			
			client = new TransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(SERVER_HOST, SERVER_PORT));
			
			LOG.debug("Starting up Elasticsearch transport client");
		}
		return client;
	}
	
	public static void destroyInstance() {
		client.close();
	}

}
