package be.ordina.wes.exercises.language;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.client.Client;
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
public class Exercise2StopwordsTest {
	
	private static final String BEER_INDEX = "beerstopwords";

	private static final String BEER_TYPE = "beer";
	
	@Autowired
	private Client client;
    @Autowired
    private IndexService indexService;
    @Autowired
    private SearchService<Beer> beerSearchService;
    
    private Exercise2Stopwords exercise2;
	
	@Before
	public void setUp() {
		indexService.deleteIndex(BEER_INDEX);
		exercise2 = new Exercise2Stopwords(client);
	}
	
	@Test
	public void testFindBeersWithStopwords() {
		
		List<Beer> beers = new ArrayList<>();
		beers.add(new Beer(1, "Grimbergen blond", "Alken", "Grimbergen is een Belgisch abdijbier, speciaalbier. Het wordt gebrouwen door Alken-Maes te Alken.", 6, 2.65));
		beers.add(new Beer(2, "Duvel", "Amaï", "Duvel is een Belgisch blond speciaalbier van Brouwerij Duvel Moortgat uit Breendonk.", 8.5, 4.55));
		beers.add(new Beer(3, "Duvel Van't Vat", "Amaï", "Duvel Belgisch blond speciaalbier van 't vat", 8.5, 4.55));
		beers.add(new Beer(4, "Duvel Hop", "Amaï", "Duvel blond speciaalbier met extra hop toppings", 8.5, 4.55));
		
		List<String> stopwords = new ArrayList<String>();
		stopwords.add("duvel");
		stopwords.add("speciaalbier");
		
		
		exercise2.createIndex(BEER_INDEX, BEER_TYPE, stopwords);
		Assert.assertTrue(indexService.indexExists(BEER_INDEX));
		
		indexService.indexBulk(beers, BEER_TYPE, BEER_INDEX);
		
		// refresh index before performing any searches, otherwise we'll get no results
		indexService.refreshIndices();
		
		List<Beer> searchResults = beerSearchService.find("abdijbier"
				, BEER_TYPE, Beer.class, BEER_INDEX, true, "description");
		Assert.assertEquals(1, searchResults.size());
		
		searchResults = beerSearchService.find("Grimbergen"
				, BEER_TYPE, Beer.class, BEER_INDEX, true, "description");
		Assert.assertEquals(1, searchResults.size());
		
		searchResults = beerSearchService.find("Duvel"
				, BEER_TYPE, Beer.class, BEER_INDEX, true, "description");
		Assert.assertEquals(0, searchResults.size());
		
		searchResults = beerSearchService.find("speciaalbier"
				, BEER_TYPE, Beer.class, BEER_INDEX, true, "description");
		Assert.assertEquals(0, searchResults.size());
	}

}
