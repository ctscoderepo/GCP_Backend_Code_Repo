package com.gcp.demo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcp.demo.Constants;
import com.gcp.demo.model.FilterOptions;
import com.gcp.demo.model.Product;
import com.gcp.demo.model.SearchResult;
import com.gcp.demo.service.SearchService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@CrossOrigin
@RestController("/api")
public class SearchController {

    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private SearchService service;

    @GetMapping("/keywordsearch")
    @SneakyThrows
    public SearchResult executeKeywordSearch(String keyword,
                                             @RequestParam(required = false) String filterOptions, String sortBy) {
        return service.searchByKeyword(keyword, getFilterOptions(Optional.ofNullable(filterOptions)), Constants.toSortBy(sortBy));
    }

    @GetMapping("/search")
    @SneakyThrows
    public SearchResult executeGuidedSearch(String category1, String category2,
                                            @RequestParam(required = false) String filterOptions, String sortBy) {
        return service.searchByCategory(category1, category2, getFilterOptions(Optional.ofNullable(filterOptions)), Constants.toSortBy(sortBy));
    }

    @GetMapping("/products/{productId}")
    @SneakyThrows
    public Product getProduct(@PathVariable String productId) {
        return service.getProduct(productId);
    }

    @SneakyThrows
    private Optional<FilterOptions> getFilterOptions(Optional<String> filterOptions) throws IOException {
        if (!filterOptions.isPresent())
            return Optional.empty();
        return Optional.of(mapper.readValue(filterOptions.get(), FilterOptions.class));
    }
    
    @GetMapping(path = "/check")
	public ResponseEntity<?> checkServiceStatus() {				
		return new ResponseEntity<String>("true", HttpStatus.OK);
	}	

}
