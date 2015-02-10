package be.ordina.wes.exercises.language;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.Client;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.ordina.wes.core.model.Beer;
import be.ordina.wes.core.service.IndexService;
import be.ordina.wes.core.service.SearchService;
import be.ordina.wes.exercises.basics.Exercise1;
import be.ordina.wes.exercises.basics.Exercise2;
import be.ordina.wes.exercises.config.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class Exercise1DefaultLanguageAnalyzerTest {
	
	private static final Logger LOG = LoggerFactory.getLogger(Exercise1DefaultLanguageAnalyzer.class);
	
	private static final String BEER_INDEX = "beerdefaultlanguage";

	private static final String BEER_TYPE = "beer";
	
	@Autowired
	private Client client;
    @Autowired
    private IndexService indexService;
    
    @Autowired
    private SearchService<Beer> beerSearchService;
    
    private Exercise1DefaultLanguageAnalyzer exercise1DefaultLanguageAnalyzer;
	
	@Before
	public void setUp() {
		indexService.deleteIndex(BEER_INDEX);
		
		exercise1DefaultLanguageAnalyzer = new Exercise1DefaultLanguageAnalyzer(client);
	}
	
	@After
	public void tearDown() {
		// delete all indices when we're done with tests
		//indexService.deleteIndex(BEER_INDEX);
	}
	
	
	@Test
	public void testDefaultLanguageAnalyzer() throws Exception {
		
		List<Beer> beers = new ArrayList<>();
        beers.add(new Beer(1, "Grimbergen blond", "Alken", "Grimbergen is een Belgisch abdijbier. Het wordt gebrouwen door Alken-Maes te Alken.", 6, 2.65));
        beers.add(new Beer(2, "Duvel", "Amaï", "Duvel is een Belgisch blond speciaalbier van Brouwerij Duvel Moortgat uit Breendonk.", 8.5, 4.55));
        beers.add(new Beer(3, "Duvel Van't Vat", "Amaï", "Duvel Belgisch blond speciaalbier van 't vat", 8.5, 4.55));
        beers.add(new Beer(4, "Duvel Hop", "Amaï", "Duvel blond speciaalbier met extra hop toppings", 8.5, 4.55));
        
        
        exercise1DefaultLanguageAnalyzer.createIndex(BEER_INDEX, BEER_TYPE);
        
        Assert.assertTrue(indexService.indexExists(BEER_INDEX));
        
        indexService.indexBulk(beers, BEER_TYPE, BEER_INDEX);
        
        // refresh index before performing any searches, otherwise we'll get no results
        indexService.refreshIndices();
        
        List<Beer> searchResults = new ArrayList<>();
        searchResults = beerSearchService.find("grimberg", BEER_TYPE, Beer.class, BEER_INDEX, true, "description.dutch");
        Assert.assertEquals(1, searchResults.size());

        searchResults = new ArrayList<>();
        searchResults = beerSearchService.find("grimbergen", BEER_TYPE, Beer.class, BEER_INDEX, true, "description.dutch");
        Assert.assertEquals(1, searchResults.size());
        
        searchResults = new ArrayList<>();
        searchResults = beerSearchService.find("duv is", BEER_TYPE, Beer.class, BEER_INDEX, true, "description.dutch");
        Assert.assertEquals(0, searchResults.size());
        
        searchResults = new ArrayList<>();
        searchResults = beerSearchService.find("duvel is een de het bier", BEER_TYPE, Beer.class, BEER_INDEX, true, "description.dutch");
		Assert.assertEquals(3, searchResults.size());
		

        // check if empty string returns all results
		searchResults = new ArrayList<>();
        searchResults = beerSearchService.find(StringUtils.EMPTY, BEER_TYPE, Beer.class, BEER_INDEX, true, "description.dutch");
        Assert.assertEquals(4, searchResults.size());

		
		
		
		
		
		
	}
	
	
	

}
