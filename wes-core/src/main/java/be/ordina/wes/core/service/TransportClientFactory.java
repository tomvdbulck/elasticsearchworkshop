package be.ordina.wes.core.service;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ordina.wes.core.config.ElasticsearchConfig;

/**
 * Factory for constructing an Elasticsearch transport client. 
 * The TransportClient connects remotely to an elasticsearch cluster using the transport module. 
 * It does not join the cluster, but simply gets one or more initial transport addresses and 
 * communicates with them in round robin fashion on each action.
 */
@Service
public class TransportClientFactory implements ClientFactory {

	private static final Logger LOG = LoggerFactory.getLogger(TransportClientFactory.class);

	private Client client;
	
	@Autowired
	private ElasticsearchConfig esConfig;

	@Override
	public Client getInstance() {
		if (client == null) {
			final Settings settings = ImmutableSettings.settingsBuilder()
					.put("cluster.name", esConfig.getClusterName())
	                .put("node.name", esConfig.getNodeName())
					.build();
			
			String serverHost = esConfig.getServerHost();
			int serverPort = esConfig.getServerPort();
			
			client = new TransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(serverHost, serverPort));
			
			LOG.debug("Starting up Elasticsearch transport client...");
		}
		return client;
	}

	@Override
	public void destroyInstance() {
        try {
            LOG.debug("Closing ElasticSearch transport client");
            
            client.close();
	    } catch (final Exception e) {
	    	LOG.error("Error closing Elasticsearch transport client", e);
	    }
	}

}
