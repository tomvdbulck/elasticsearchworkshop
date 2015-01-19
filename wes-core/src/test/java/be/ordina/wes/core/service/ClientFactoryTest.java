package be.ordina.wes.core.service;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.client.Client;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.ordina.wes.core.config.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class ClientFactoryTest {

	private static final String IMPORTED_INDEX = "inventory";
	
	private final int expectedDocumentCount = 1218;
	
	@Autowired
	private Client client;
	
	/**
	 * Test that we can connect to the cluster and that it's ready (cluster health = green).
	 * 
	 * Red status indicates that a shard is not allocated in the cluster, 
	 * yellow means that the primary shard is allocated but replicas are not, 
	 * and green means that all shards are allocated.
	 */
	@Test
	public void testClusterState() {
		ClusterHealthResponse clusterHealth = client.admin().cluster().prepareHealth().get();
		
		Assert.assertEquals(ClusterHealthStatus.GREEN, clusterHealth.getStatus());
	}
	
	/**
	 * Test that all the test data was imported
	 */
	@Test
	public void testImportedData() {
		Assert.assertTrue(client.admin().indices().prepareExists(IMPORTED_INDEX).get().isExists());
		
		long actualDocumentCount = client.prepareCount(IMPORTED_INDEX).get().getCount();
		Assert.assertEquals(expectedDocumentCount, actualDocumentCount);
	}
}
