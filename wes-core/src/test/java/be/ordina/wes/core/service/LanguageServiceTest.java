package be.ordina.wes.core.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
public class LanguageServiceTest {

	private static final String BEER_TYPE = "beer";
	
	private String indexName;
	private String indexWithoutAnalyzerName;
	private String indexWithStopwords;
	
	@Autowired
	private ElasticsearchConfig esConfig;
	
    @Autowired
    private LanguageService languageService;
    @Autowired
    private SearchService<Beer> beerSearchService;

    @Before
    public void setUp() {
    	indexName = "beerlanguageindex";
    	indexWithoutAnalyzerName = "beerindex";
    	indexWithStopwords = "stopwordbeerindex";
    	
    	if (languageService.indexExists(indexName)) {
    		languageService.deleteIndex(indexName);
    	}
    	if (languageService.indexExists(indexWithoutAnalyzerName)) {
    		languageService.deleteIndex(indexWithoutAnalyzerName);
    	}
    	if (languageService.indexExists(indexWithStopwords)) {
    		languageService.deleteIndex(indexWithStopwords);
    	}
    }
    
	@Test
	public void testFindBeersWithDutchLanguageAnalyzer() {

        List<Beer> beers = new ArrayList<>();
        beers.add(new Beer(1, "Grimbergen blond", "Alken", "Grimbergen is een Belgisch abdijbier. Het wordt gebrouwen door Alken-Maes te Alken.", 6, 2.65));
        beers.add(new Beer(2, "Duvel", "Amaï", "Duvel is een Belgisch blond speciaalbier van Brouwerij Duvel Moortgat uit Breendonk.", 8.5, 4.55));
        beers.add(new Beer(3, "Duvel Van't Vat", "Amaï", "Duvel Belgisch blond speciaalbier van 't vat", 8.5, 4.55));
        beers.add(new Beer(4, "Duvel Hop", "Amaï", "Duvel blond speciaalbier met extra hop toppings", 8.5, 4.55));
        
        
        languageService.createIndex(indexName);
        
        Assert.assertTrue(languageService.indexExists(indexName));
        
        languageService.indexBulk(beers, BEER_TYPE, indexName);
        
        // refresh index before performing any searches, otherwise we'll get no results
        languageService.refreshIndices();
        
        List<Beer> searchResults = beerSearchService.find("duvel is een blond speciaalbier"
        		, BEER_TYPE, Beer.class, indexName, true, "description");

        searchResults = beerSearchService.find("grimberg", BEER_TYPE, Beer.class, indexName, true, "description");
        Assert.assertEquals(1, searchResults.size());

        searchResults = beerSearchService.find("grimbergen", BEER_TYPE, Beer.class, indexName, true, "description");
        Assert.assertEquals(1, searchResults.size());

        searchResults = beerSearchService.find("grimbergen duvel", BEER_TYPE, Beer.class, indexName, true, "description");
        Assert.assertEquals(4, searchResults.size());
        
        searchResults = beerSearchService.find("duv is", BEER_TYPE, Beer.class, indexName, true, "description");
        Assert.assertEquals(0, searchResults.size());
        
        searchResults = beerSearchService.find("duvel is een de het bier", BEER_TYPE, Beer.class, indexName, true, "description");
		Assert.assertEquals(3, searchResults.size());
		

        // check if empty string returns all results
        searchResults = beerSearchService.find(StringUtils.EMPTY, BEER_TYPE, Beer.class, indexName, true, "description");
        Assert.assertEquals(4, searchResults.size());
	}
	@Test
	public void testFindBeersWithNoLanguageAnalyzer() {
		
		List<Beer> beers = new ArrayList<>();
		beers.add(new Beer(1, "Grimbergen blond", "Alken", "Grimbergen is een Belgisch abdijbier. Het wordt gebrouwen door Alken-Maes te Alken.", 6, 2.65));
		beers.add(new Beer(2, "Duvel", "Amaï", "Duvel is een Belgisch blond speciaalbier van Brouwerij Duvel Moortgat uit Breendonk.", 8.5, 4.55));
		beers.add(new Beer(3, "Duvel Van't Vat", "Amaï", "Duvel Belgisch blond speciaalbier van 't vat", 8.5, 4.55));
		beers.add(new Beer(4, "Duvel Hop", "Amaï", "Duvel blond speciaalbier met extra hop toppings", 8.5, 4.55));
		
		 
        languageService.createIndexWithoutAnalyzer(indexWithoutAnalyzerName);
        Assert.assertTrue(languageService.indexExists(indexWithoutAnalyzerName));
        
        languageService.indexBulk(beers, BEER_TYPE, indexWithoutAnalyzerName);
        
        // refresh index before performing any searches, otherwise we'll get no results
        languageService.refreshIndices();
        
		List<Beer> searchResults = beerSearchService.find("duvel is een blond speciaalbier"
				, BEER_TYPE, Beer.class, indexWithoutAnalyzerName, true, "description");
		Assert.assertEquals(4, searchResults.size());
		
		searchResults = beerSearchService.find("grimberg", BEER_TYPE, Beer.class, indexWithoutAnalyzerName, true, "description");
		Assert.assertEquals(0, searchResults.size());
		
		searchResults = beerSearchService.find("grimbergen", BEER_TYPE, Beer.class, indexWithoutAnalyzerName, true, "description");
		Assert.assertEquals(0, searchResults.size());
		
		searchResults = beerSearchService.find("Grimbergen", BEER_TYPE, Beer.class, indexWithoutAnalyzerName, true, "description");
		Assert.assertEquals(1, searchResults.size());
		
		searchResults = beerSearchService.find("Grimbergen duvel", BEER_TYPE, Beer.class, indexWithoutAnalyzerName, true, "description");
		Assert.assertEquals(1, searchResults.size());
		
		searchResults = beerSearchService.find("Grimbergen Duvel", BEER_TYPE, Beer.class, indexWithoutAnalyzerName, true, "description");
		Assert.assertEquals(4, searchResults.size());
		
		
		searchResults = beerSearchService.find("Duvel is een de het bier", BEER_TYPE, Beer.class, indexWithoutAnalyzerName, true, "description");
		Assert.assertEquals(4, searchResults.size());
		
		// check if empty string returns all results
		searchResults = beerSearchService.find(StringUtils.EMPTY, BEER_TYPE, Beer.class, indexWithoutAnalyzerName, true, "description");
		Assert.assertEquals(4, searchResults.size());
	}
	@Test
	public void testFindBeersWithStopwords() {
		
		List<Beer> beers = new ArrayList<>();
		beers.add(new Beer(1, "Grimbergen blond", "Alken", "Grimbergen is een Belgisch abdijbier, speciaalbier. Het wordt gebrouwen door Alken-Maes te Alken.", 6, 2.65));
		beers.add(new Beer(2, "Duvel", "Amaï", "Duvel is een Belgisch blond speciaalbier van Brouwerij Duvel Moortgat uit Breendonk.", 8.5, 4.55));
		beers.add(new Beer(3, "Duvel Van't Vat", "Amaï", "Duvel Belgisch blond speciaalbier van 't vat", 8.5, 4.55));
		beers.add(new Beer(4, "Duvel Hop", "Amaï", "Duvel blond speciaalbier met extra hop toppings", 8.5, 4.55));
		
		List<String> stopword = new ArrayList<String>();
		stopword.add("duvel");
		stopword.add("speciaalbier");
		
		
		languageService.createIndexWithStopword(indexWithStopwords, stopword);
		Assert.assertTrue(languageService.indexExists(indexWithStopwords));
		
		languageService.indexBulk(beers, BEER_TYPE, indexWithStopwords);
		
		// refresh index before performing any searches, otherwise we'll get no results
		languageService.refreshIndices();
		
		List<Beer> searchResults = beerSearchService.find("abdijbier"
				, BEER_TYPE, Beer.class, indexWithStopwords, true, "description");
		Assert.assertEquals(1, searchResults.size());
		
		searchResults = beerSearchService.find("Grimbergen"
				, BEER_TYPE, Beer.class, indexWithStopwords, true, "description");
		Assert.assertEquals(1, searchResults.size());
		
		searchResults = beerSearchService.find("Duvel"
				, BEER_TYPE, Beer.class, indexWithStopwords, true, "description");
		Assert.assertEquals(0, searchResults.size());
		
		searchResults = beerSearchService.find("speciaalbier"
				, BEER_TYPE, Beer.class, indexWithStopwords, true, "description");
		Assert.assertEquals(0, searchResults.size());
		
		
	}

}
