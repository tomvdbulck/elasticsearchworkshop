package be.ordina.wes.core.service;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.ordina.wes.core.config.TestConfig;
import be.ordina.wes.core.model.Beer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class IndexServiceTest {

	private static final String INDEX_NAME = "inventory";
	private static final String DOCUMENT_TYPE = "beer";
	
    @Autowired
    private IndexService indexService;
    @Autowired
    private SearchService<Beer> beerSearchService;

    @After
    public void tearDown() {
    	if (indexService.indexExists(INDEX_NAME)) {
            indexService.deleteIndex(INDEX_NAME);
    	}
    }

	@Test
	public void testCreateDeleteIndex() {
		Assert.assertFalse(indexService.indexExists(INDEX_NAME));
		
		indexService.createIndex(INDEX_NAME);
		Assert.assertTrue(indexService.indexExists(INDEX_NAME));
		
		indexService.deleteIndex(INDEX_NAME);
		Assert.assertFalse(indexService.indexExists(INDEX_NAME));
	}
	
	@Test
	public void testIndex() {
		Assert.assertFalse(indexService.indexExists(INDEX_NAME));
		
		indexService.createIndex(INDEX_NAME);
		
		Beer beer = new Beer(2, "Duvel", "Amaï", "", 8.5, 4.55);
		
		indexService.index(beer, DOCUMENT_TYPE);
		
		// refresh index before performing search operations
		indexService.refreshIndices();
		
		List<Beer> resultList = beerSearchService.find("", DOCUMENT_TYPE, Beer.class);
		
		Assert.assertEquals(1, resultList.size());
	}
	
}
