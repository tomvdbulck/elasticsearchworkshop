package be.ordina.wes.core.service;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Factory for constructing an embedded Elasticsearch node. Used primarily for unit testing.
 */
@Service
public class ClientFactoryImpl implements ClientFactory {

	private static final Logger LOG = LoggerFactory.getLogger(ClientFactoryImpl.class);

	private static final String CLUSTER_NAME = "elasticsearch.cluster.name";
	private static final String NODE_NAME = "elasticsearch.node.name";
	private static final String NODE_MASTER = "elasticsearch.node.master";
	private static final String NODE_LOCAL = "elasticsearch.node.local";

	private Node node;
	
	@Autowired
	private Environment env;
	
	@Override
	public Client getInstance() {
		if (node == null) {
			final Settings settings = ImmutableSettings.settingsBuilder()
	                .put("node.name", env.getProperty(NODE_NAME))
	                .put("node.master", env.getProperty(NODE_MASTER))
	                .build();

			boolean clientNode = Boolean.getBoolean(env.getProperty(NODE_LOCAL));
			
			node = nodeBuilder()
					.clusterName(CLUSTER_NAME)
					.client(clientNode)
					.settings(settings)
					.node();
			
			// wait until the node starts up
			node.client().admin().cluster().prepareHealth().setWaitForYellowStatus().get();
			
			String nodeName = node.settings().get("name");
			String clusterName = node.settings().get("cluster.name");

			LOG.info("Node [{}] on cluster [{}] started...", nodeName, clusterName);
		}
		return node.client();
	}

	@Override
	public void destroyInstance() {
        try {
        	String nodeName = node.settings().get("name");
            LOG.info("Closing ElasticSearch node [{}]", nodeName);
            
            node.close();
	    } catch (final Exception e) {
	    	LOG.error("Error closing Elasticsearch node: ", e);
	    }
	}

}
