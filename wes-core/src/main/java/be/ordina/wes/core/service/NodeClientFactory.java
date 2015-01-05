package be.ordina.wes.core.service;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ordina.wes.core.config.ElasticsearchConfig;

/**
 * Factory for constructing an Elasticsearch node. 
 * The NodeClient is a node within the cluster (but does not hold data, and cannot become master). 
 * Because it is a node, it knows the entire cluster state — where all the nodes reside, which 
 * shards live in which nodes, etc.
 */
@Service
public class NodeClientFactory implements ClientFactory {

	private static final Logger LOG = LoggerFactory.getLogger(NodeClientFactory.class);

	private Node node;
	
	@Autowired
	private ElasticsearchConfig esConfig;
	
	@Override
	public Client getInstance() {
		if (node == null) {
			final Settings settings = ImmutableSettings.settingsBuilder()
	                .put("node.name", esConfig.getNodeName())
	                .put("node.master", esConfig.getNodeMaster())
	                .build();

			node = NodeBuilder.nodeBuilder()
					.clusterName(esConfig.getClusterName())
					.client(esConfig.isNodeLocal())
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
	    	LOG.error("Error closing Elasticsearch node", e);
	    }
	}

}
