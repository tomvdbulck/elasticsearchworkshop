package be.ordina.wes.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be.ordina.wes.core.model.Beer;
import be.ordina.wes.core.service.IndexService;
import be.ordina.wes.core.service.SearchService;

@RestController
public class MainController {
	
	@Autowired
	private IndexService indexService;
	@Autowired
	private SearchService<Beer> beerSearchService;

	@RequestMapping("/beer")
	public List<Beer> beer(@RequestParam(value="brand", defaultValue="Duvel") String brand) {
		return beerSearchService.find("", "beer", Beer.class);
	}
	
	@RequestMapping("/index")
	public String index() {
		Beer beer = new Beer(1, "Jupiler", "blond", "", 6.5, 4.99);
		indexService.index(beer, "beer");
		return "success";
	}
	
}
