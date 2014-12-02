package be.ordina.wes.core.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.ordina.wes.core.config.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class IndexServiceTest {

	private static final String INDEX_NAME = "inventory";
	
    @Autowired
    private IndexService indexService;
    
	@Test
	public void testCreateDeleteIndex() {
		Assert.assertFalse(indexService.indexExists(INDEX_NAME));
		
		indexService.createIndex(INDEX_NAME);
		Assert.assertTrue(indexService.indexExists(INDEX_NAME));
		
		indexService.deleteIndex(INDEX_NAME);
		Assert.assertFalse(indexService.indexExists(INDEX_NAME));
	}
	
}
