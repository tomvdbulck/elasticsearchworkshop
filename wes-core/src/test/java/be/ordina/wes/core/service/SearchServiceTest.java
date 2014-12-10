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

import be.ordina.wes.core.config.TestConfig;
import be.ordina.wes.core.model.Beer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class SearchServiceTest {

	private static final String INDEX_NAME = "inventory";
	private static final String DOCUMENT_TYPE_BEER = "beer";
	
    @Autowired
    private IndexService indexService;
    @Autowired
    private SearchService<Beer> beerSearchService;

    @Before
    public void setUp() {
    	if (indexService.indexExists(INDEX_NAME)) {
    		indexService.deleteIndex(INDEX_NAME);
    	}
    }
    
    @After
    public void tearDown() {
    	if (indexService.indexExists(INDEX_NAME)) {
            indexService.deleteIndex(INDEX_NAME);
    	}
    }

	@Test
	public void testFindBeers() {

        List<Beer> beers = new ArrayList<>();
        beers.add(new Beer(1, "Grimbergen blond", "Alken", "Grimbergen is een Belgisch abdijbier. Het wordt gebrouwen door Alken-Maes te Alken.", 6, 2.65));
        beers.add(new Beer(2, "Duvel", "Amaï", "Duvel is een Belgisch blond speciaalbier van Brouwerij Duvel Moortgat uit Breendonk.", 8.5, 4.55));
        
        indexService.createIndex(INDEX_NAME);
        
        Assert.assertTrue(indexService.indexExists(INDEX_NAME));
        
        indexService.indexBulk(beers, DOCUMENT_TYPE_BEER);
        
        // refresh index before performing any searches, otherwise we'll get no results
        indexService.refreshIndices();
        
        List<Beer> searchResults = beerSearchService.find("Duvel", DOCUMENT_TYPE_BEER, Beer.class);
		Assert.assertEquals(1, searchResults.size());

        searchResults = beerSearchService.find("grimberg", DOCUMENT_TYPE_BEER, Beer.class);
        Assert.assertEquals(0, searchResults.size());

        searchResults = beerSearchService.find("grimbergen", DOCUMENT_TYPE_BEER, Beer.class);
        Assert.assertEquals(1, searchResults.size());

        searchResults = beerSearchService.find("blond", DOCUMENT_TYPE_BEER, Beer.class);
        Assert.assertEquals(2, searchResults.size());

        searchResults = beerSearchService.find("abdijbier", DOCUMENT_TYPE_BEER, Beer.class);
        Assert.assertEquals(1, searchResults.size());

        searchResults = beerSearchService.find("belgisch", DOCUMENT_TYPE_BEER, Beer.class);
        Assert.assertEquals(2, searchResults.size());

        searchResults = beerSearchService.find(StringUtils.EMPTY, DOCUMENT_TYPE_BEER, Beer.class);
        Assert.assertEquals(2, searchResults.size());
	}

}
