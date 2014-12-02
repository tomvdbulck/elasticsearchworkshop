package be.ordina.wes.core.service;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Factory for constructing an embedded Elasticsearch node. Used primarily for unit testing.
 */
@Service
public class ClientFactoryImpl implements ClientFactory {

	private static final Logger LOG = LoggerFactory.getLogger(ClientFactoryImpl.class);

	private static final String SETTINGS_PATH = "elasticsearch.yml";
	
	private Node node;
	
	@Override
	public Client getInstance() {
		if (node == null) {
			Settings settings = ImmutableSettings.settingsBuilder().loadFromClasspath(SETTINGS_PATH).build();
			node = nodeBuilder().settings(settings).node();
			
			// wait until the node starts up
			node.client().admin().cluster().prepareHealth().setWaitForYellowStatus().get();
			
			String nodeName = node.settings().get("name");
			String clusterName = node.settings().get("cluster.name");

			LOG.info("Node [" + nodeName + "] on [" + clusterName + "] cluster started...");
		}
		return node.client();
	}

	@Override
	public void destroyInstance() {
        try {
        	String nodeName = node.settings().get("name");
            LOG.info("Closing ElasticSearch node [" + nodeName + "]");
            
            node.close();
	    } catch (final Exception e) {
	    	LOG.error("Error closing Elasticsearch node: ", e);
	    }
	}

}
