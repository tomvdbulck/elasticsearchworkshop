package be.ordina.wes.core.service;

import java.util.ArrayList;
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
public class SearchServiceTest {

	private static final String BEER_TYPE = "beer";
	
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
	public void testFindBeers() {

        List<Beer> beers = new ArrayList<>();
        beers.add(new Beer(1, "Grimbergen blond", "Alken", "Grimbergen is een Belgisch abdijbier. Het wordt gebrouwen door Alken-Maes te Alken.", 6, 2.65));
        beers.add(new Beer(2, "Duvel", "Amaï", "Duvel is een Belgisch blond speciaalbier van Brouwerij Duvel Moortgat uit Breendonk.", 8.5, 4.55));
        
        indexService.createIndex(indexName);
        
        Assert.assertTrue(indexService.indexExists(indexName));
        
        indexService.indexBulk(beers, BEER_TYPE);
        
        // refresh index before performing any searches, otherwise we'll get no results
        indexService.refreshIndices();
        
        List<Beer> searchResults = beerSearchService.find("Duvel", BEER_TYPE, Beer.class);
		Assert.assertEquals(1, searchResults.size());

		// partial string shouldn't return any results
        searchResults = beerSearchService.find("grimberg", BEER_TYPE, Beer.class);
        Assert.assertEquals(0, searchResults.size());

        searchResults = beerSearchService.find("grimbergen", BEER_TYPE, Beer.class);
        Assert.assertEquals(1, searchResults.size());

        searchResults = beerSearchService.find("grimbergen duvel", BEER_TYPE, Beer.class);
        Assert.assertEquals(2, searchResults.size());

        searchResults = beerSearchService.find("4.55", BEER_TYPE, Beer.class);
        Assert.assertEquals(1, searchResults.size());

        searchResults = beerSearchService.find("BELGISCH", BEER_TYPE, Beer.class);
        Assert.assertEquals(2, searchResults.size());

        // check if empty string returns all results
        searchResults = beerSearchService.find(StringUtils.EMPTY, BEER_TYPE, Beer.class);
        Assert.assertEquals(2, searchResults.size());
	}

}
