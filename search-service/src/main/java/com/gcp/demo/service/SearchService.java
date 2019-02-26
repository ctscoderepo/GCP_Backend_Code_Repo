package com.gcp.demo.service;

import com.gcp.demo.Constants;
import com.gcp.demo.exceptions.InvalidInputException;
import com.gcp.demo.model.*;
import com.gcp.demo.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    public SearchResult searchByCategory(String category1, String category2, final Optional<FilterOptions> filterOptions, final Constants.SortBy sortBy) throws InvalidInputException {
        if (!StringUtils.isEmpty(category1)) {
            return repository.searchByCategory(category1, category2, filterOptions, sortBy);
        } else {
            throw new InvalidInputException("Category1 should not be empty");
        }

    }

    public SearchResult getCategoryResults(String categoryId, String subCategoryId, final Optional<FilterOptions> filterOptions, final Constants.SortBy sortBy) throws InvalidInputException {
        if (!StringUtils.isEmpty(categoryId)) {
            List<CategoryDocument> categories =  repository.getCategoryAndParentCategory(categoryId);
            if (!CollectionUtils.isEmpty(categories)) {
                String categoryName = categories.get(0).getDisplayName();
                String subCategoryName = categories.size()>1 ? categories.get(1).getDisplayName() : null;
                return repository.searchByCategory(categoryName, subCategoryName, filterOptions, sortBy);
            }
        }

        throw new InvalidInputException("No results for given category id");

    }

    public Product getProduct(String productId) throws InvalidInputException {
        if (!StringUtils.isEmpty(productId)) {
            return repository.getProduct(productId);
        } else {
            throw new InvalidInputException("ProductId should not be empty");
        }
    }

    public List<Category> getCategories(String categoryId) throws InvalidInputException {
        List<CategoryDocument> categoryDocuments = null;
        if (Optional.ofNullable(categoryId).isPresent()) {
            categoryDocuments = repository.getCategoryAndChildCategories(categoryId);
        } else {
            categoryDocuments = repository.getAllCategories();
        }
        if (!CollectionUtils.isEmpty(categoryDocuments)) {
            Map<String, CategoryDocument> categoryMap = categoryDocuments.stream().collect(Collectors.toMap(CategoryDocument::getId, Function.identity()));
            return processCategoryDocumentMapToCategoryList(categoryMap);
        } else {
            throw new InvalidInputException("No categories found for given request");
        }

    }

    private List<Category> processCategoryDocumentMapToCategoryList(Map<String, CategoryDocument> categoryMap) {
        List<Category> parentCategories =  categoryMap.entrySet().stream()
                .filter(entry->entry.getValue() != null)
                .filter(entry-> !CollectionUtils.isEmpty(entry.getValue().getChildren()))
                .map(entry->entry.getValue()).map(CategoryDocument::toCategory).collect(Collectors.toList());
        parentCategories.forEach(
                category -> {
                    if (!CollectionUtils.isEmpty(category.getChildCatIds())) {
                        category.setChildren( category.getChildCatIds().stream().map(catId -> categoryMap.get(catId)).filter(Objects::nonNull)
                                .map(CategoryDocument::toCategory).sorted(Comparator.comparing(Category::getId)).collect(Collectors.toList()));
                    }
                }
        );
        if (CollectionUtils.isEmpty(parentCategories)) {
            parentCategories = categoryMap.values().stream().map(CategoryDocument::toCategory).collect(Collectors.toList());
        }
        parentCategories.sort(Comparator.comparing(Category::getId));
        return parentCategories;
    }
}
