package com.gcp.demo.service;

import com.gcp.demo.Constants;
import com.gcp.demo.exceptions.InvalidInputException;
import com.gcp.demo.model.FilterOptions;
import com.gcp.demo.model.Product;
import com.gcp.demo.model.SearchResult;
import com.gcp.demo.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class SearchService {
    private SearchRepository repository;
    private Pattern allowedPattern;

    @Autowired
    public SearchService(SearchRepository repository, @Value("${keyword-search.allowed-pattern}") String keywordPattern) {
        this.repository = repository;
        this.allowedPattern = Pattern.compile(keywordPattern, Pattern.CASE_INSENSITIVE);
    }


    public SearchResult searchByKeyword(String keyword, final Optional<FilterOptions> filterOptions, final Constants.SortBy sortBy) throws InvalidInputException {
        if (keyword != null && allowedPattern.matcher(keyword).matches()) {
            return repository.searchByKeyword(keyword, filterOptions, sortBy);
        } else {
            throw new InvalidInputException("Keyword: '" + keyword + "' contains invalid characters or empty");
        }
    }

    public SearchResult searchByCategory(String category1, String category2, final Optional<FilterOptions> filterOptions, final Constants.SortBy  sortBy) throws InvalidInputException {
        if (!StringUtils.isEmpty(category1)) {
            return repository.searchByCategory(category1, category2, filterOptions, sortBy);
        } else {
            throw new InvalidInputException("Category1 should not be empty");
        }

    }

    public Product getProduct(String productId) throws InvalidInputException{
        if (!StringUtils.isEmpty(productId)) {
            return repository.getProduct(productId);
        } else {
            throw new InvalidInputException("ProductId should not be empty");
        }
    }
}
