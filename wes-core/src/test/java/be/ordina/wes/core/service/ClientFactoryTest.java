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

	@Autowired
	private Client client;
	
	@Test
	public void testClusterState() {
		ClusterHealthResponse clusterHealth = client.admin().cluster().prepareHealth().get();
		
		Assert.assertEquals(ClusterHealthStatus.GREEN, clusterHealth.getStatus());
	}
}
