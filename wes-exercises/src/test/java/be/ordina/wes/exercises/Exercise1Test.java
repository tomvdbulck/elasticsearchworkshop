package be.ordina.wes.exercises;

import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.client.Client;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class Exercise1Test {

	private static Client client;
	
	@BeforeClass
	public static void setUp() {
		client = Exercise1.getInstance();
	}
	
	/**
	 * Test that we can connect to the cluster and that it's ready (i.e. cluster health = green).
	 * 
	 * Red status indicates that a shard is not allocated in the cluster, 
	 * yellow - primary shard is allocated but replicas are not, 
	 * green - all shards are allocated.
	 */
	@Test
	public void testClusterState() throws InterruptedException, ExecutionException {
		ClusterHealthResponse clusterHealth = client.admin().cluster().prepareHealth().get();
		
		Assert.assertEquals(ClusterHealthStatus.GREEN, clusterHealth.getStatus());
	}
	
}
