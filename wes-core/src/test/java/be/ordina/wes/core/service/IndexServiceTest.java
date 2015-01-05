package be.ordina.wes.core.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.ordina.wes.core.config.ElasticsearchConfig;
import be.ordina.wes.core.config.TestConfig;
import be.ordina.wes.core.model.Beer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class IndexServiceTest {

	private static final String DOCUMENT_TYPE = "beer";
	
	private String indexName;
	
	@Autowired
	private ElasticsearchConfig esConfig;
    @Autowired
    private IndexService indexService;
    @Autowired
    private SearchService<Beer> beerSearchService;

    @Before
    public void setUp() {
    	indexName = esConfig.getIndexName();
    	
    	if (indexService.indexExists(indexName)) {
    		indexService.deleteIndex(indexName);
    	}
    }
    
    @After
    public void tearDown() {
    	if (indexService.indexExists(indexName)) {
            indexService.deleteIndex(indexName);
    	}
    }

	@Test
	public void testCreateDeleteIndex() {
		Assert.assertFalse(indexService.indexExists(indexName));
		
		indexService.createIndex(indexName);
		Assert.assertTrue(indexService.indexExists(indexName));
		
		indexService.deleteIndex(indexName);
		Assert.assertFalse(indexService.indexExists(indexName));
	}
	
	@Test
	public void testIndex() {
		Assert.assertFalse(indexService.indexExists(indexName));
		
		indexService.createIndex(indexName);
		
		Beer beer = new Beer(2, "Duvel", "Amaï", "", 8.5, 4.55);
		
		indexService.index(beer, DOCUMENT_TYPE);
		
		// refresh index before performing search operations
		indexService.refreshIndices();
		
		List<Beer> resultList = beerSearchService.find(StringUtils.EMPTY, DOCUMENT_TYPE, Beer.class);
		
		Assert.assertEquals(1, resultList.size());
	}
	
}
