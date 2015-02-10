package be.ordina.wes.exercises.language;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.client.Client;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.ordina.wes.core.model.Beer;
import be.ordina.wes.core.service.IndexService;
import be.ordina.wes.core.service.SearchService;
import be.ordina.wes.exercises.config.TestConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class Exercise3SynonymsTest {
	
	private static final String BEER_INDEX = "beersynonyms";
	private static final String BEER_INDEX_OWN = "beerownsynonyms";

	private static final String BEER_TYPE = "beer";
	
	@Autowired
	private Client client;
    @Autowired
    private IndexService indexService;
    
    @Autowired
    private SearchService<Beer> beerSearchService;
    
    private Exercise3Synonyms exercise3;
	
	@Before
	public void setUp() {
		indexService.deleteIndex(BEER_INDEX);
		indexService.deleteIndex(BEER_INDEX_OWN);
		
		exercise3 = new Exercise3Synonyms(client);
	}
	
	@After
	public void tearDown() {
		// delete all indices when we're done with tests
		//indexService.deleteIndex(BEER_INDEX);
		//indexService.deleteIndex(BEER_INDEX_OWN);
	}
	
	/**
	 * Search on brand will remain case sensitive - important as you will have to create an analyzer with no filters active
	 * 
	 * Search on description will become case insensitive 
	 * 
	 * @throws Exception
	 */
	@Test
	public void lowerCaseSynonyms() throws Exception {
		List<Beer> beers = new ArrayList<>();
		beers.add(new Beer(2, "Duvel", "Amaï", "Duvel is een Belgisch blond speciaalbier van Brouwerij Duvel Moortgat uit Breendonk.", 8.5, 4.55));
		
		exercise3.createIndex(BEER_INDEX, BEER_TYPE);
		Assert.assertTrue(indexService.indexExists(BEER_INDEX));
		
		indexService.indexBulk(beers, BEER_TYPE, BEER_INDEX);
		
		// refresh index before performing any searches, otherwise we'll get no results
		indexService.refreshIndices();
		
		
		List<Beer> searchResults = beerSearchService.find("Duvel"
				, BEER_TYPE, Beer.class, BEER_INDEX, true, "description");
		Assert.assertEquals(1, searchResults.size());
		searchResults = beerSearchService.find("Duvel"
				, BEER_TYPE, Beer.class, BEER_INDEX, true, "brand");
		Assert.assertEquals(1, searchResults.size());
		
		searchResults = beerSearchService.find("duvel"
				, BEER_TYPE, Beer.class, BEER_INDEX, true, "description");
		Assert.assertEquals(1, searchResults.size());
		searchResults = beerSearchService.find("duvel"
				, BEER_TYPE, Beer.class, BEER_INDEX, true, "brand");
		Assert.assertEquals(0, searchResults.size());
		
		searchResults = beerSearchService.find("DUVEL"
				, BEER_TYPE, Beer.class, BEER_INDEX, true, "description");
		Assert.assertEquals(1, searchResults.size());
		searchResults = beerSearchService.find("DUVEL"
				, BEER_TYPE, Beer.class, BEER_INDEX, true, "brand");
		Assert.assertEquals(0, searchResults.size());
		
	}
	
	@Test
	public void defineYourOwnSynonymsAndFilterOrder() throws Exception {
		List<Beer> beers = new ArrayList<>();
		beers.add(new Beer(2, "Duvel", "Amaï", "Duvel is een lekker bier", 8.5, 4.55));
		beers.add(new Beer(4, "Duvel Hop", "Amaï", "Duiveltjesbier", 8.5, 4.55));
		beers.add(new Beer(5, "Duvel Hop", "Amaï", "Duivels bier", 8.5, 4.55));
		beers.add(new Beer(6, "Grimbergen Blond", "Amaï", "Grimbergen", 8.5, 4.55));
		beers.add(new Beer(7, "Grimbergen Blond", "Amaï", "Hee abdijbier", 8.5, 4.55));
		beers.add(new Beer(8, "Grimbergen Blond", "Amaï", "Wel ne nep-trappist", 8.5, 4.55));
		beers.add(new Beer(9, "Grimbergen Blond", "Amaï", "fake-trappist", 8.5, 4.55));
		
		exercise3.createIndexWithSynonyms(BEER_INDEX_OWN, BEER_TYPE);
		Assert.assertTrue(indexService.indexExists(BEER_INDEX_OWN));
		
		indexService.indexBulk(beers, BEER_TYPE, BEER_INDEX_OWN);
		
		// refresh index before performing any searches, otherwise we'll get no results
		indexService.refreshIndices();
		
		//1. show effect without and without fold
		List<Beer> searchResults = beerSearchService.find("Duvel"
				, BEER_TYPE, Beer.class, BEER_INDEX_OWN, true, "description.synonym");
		Assert.assertEquals(3, searchResults.size());
		
		searchResults = beerSearchService.find("Duvel"
				, BEER_TYPE, Beer.class, BEER_INDEX_OWN, true, "description");
		Assert.assertEquals(1, searchResults.size());
		
		searchResults = beerSearchService.find("duiveltjesbier"
				, BEER_TYPE, Beer.class, BEER_INDEX_OWN, true, "description.synonym");
		Assert.assertEquals(3, searchResults.size());
		
		searchResults = beerSearchService.find("duiveltjesbier"
				, BEER_TYPE, Beer.class, BEER_INDEX_OWN, true, "description");
		Assert.assertEquals(1, searchResults.size());
		
		searchResults = beerSearchService.find("duivels"
				, BEER_TYPE, Beer.class, BEER_INDEX_OWN, true, "description.synonym");
		Assert.assertEquals(3, searchResults.size());
		
		searchResults = beerSearchService.find("duivels"
				, BEER_TYPE, Beer.class, BEER_INDEX_OWN, true, "description");
		Assert.assertEquals(1, searchResults.size());
		
		searchResults = beerSearchService.find("fake"
				, BEER_TYPE, Beer.class, BEER_INDEX_OWN, true, "description.synonym");
		Assert.assertEquals(3, searchResults.size());
		
		searchResults = beerSearchService.find("fake"
				, BEER_TYPE, Beer.class, BEER_INDEX_OWN, true, "description");
		Assert.assertEquals(1, searchResults.size());
		
		searchResults = beerSearchService.find("abdijbier"
				, BEER_TYPE, Beer.class, BEER_INDEX_OWN, true, "description.synonym");
		Assert.assertEquals(1, searchResults.size());
		
		searchResults = beerSearchService.find("abdijbier"
				, BEER_TYPE, Beer.class, BEER_INDEX_OWN, true, "description");
		Assert.assertEquals(1, searchResults.size());
			
	}

}
