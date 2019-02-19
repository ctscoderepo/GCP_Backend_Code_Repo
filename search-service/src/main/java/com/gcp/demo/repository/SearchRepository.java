package com.gcp.demo.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcp.demo.Constants;
import com.gcp.demo.model.Facet;
import com.gcp.demo.model.FilterOptions;
import com.gcp.demo.model.Product;
import com.gcp.demo.model.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Repository
@Slf4j
public class SearchRepository {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ElasticsearchTemplate searchTemplate;

    public SearchResult searchByKeyword(final String keyword, final Optional<FilterOptions> filterOptions, final Constants.SortBy sortBy) {
        SearchQuery searchQuery = withDefaultAggregations(new NativeSearchQueryBuilder()
                .withQuery(prepareBaseQuery(keyword, filterOptions)))
                .withSort(prepareSortOptions(sortBy))
                .build();

        SearchResult result = performSearchOperation(searchQuery);

        List<Product> products = searchTemplate.queryForList(searchQuery, Product.class);
        result.setProducts(products);
        return result;
    }

    private SortBuilder prepareSortOptions(Constants.SortBy sortBy) {
        SortBuilder sortBuilder = null;
        switch (sortBy) {
            case NAME_ASC:
                sortBuilder = SortBuilders.fieldSort("productName.keyword").order(SortOrder.ASC);
                break;
            case NAME_DESC:
                sortBuilder = SortBuilders.fieldSort("productName.keyword").order(SortOrder.DESC);
                break;
            case PRICE_LOW_2_HIGH:
                sortBuilder = SortBuilders.fieldSort("price").order(SortOrder.ASC);
                break;
            case PRICE_HIGH_2_LOW:
                sortBuilder = SortBuilders.fieldSort("price").order(SortOrder.DESC);
                break;
            default:
                sortBuilder = SortBuilders.fieldSort("price").order(SortOrder.ASC);
        }
        return sortBuilder;
    }

    private QueryBuilder prepareBaseQuery(final String keyword, final Optional<FilterOptions> filterOptions) {
        MultiMatchQueryBuilder queryBuilder = multiMatchQuery(keyword)
                .field("skuId.keyword")
                .field("brand")
                .field("category1")
                .field("category2")
                .field("productName")
                .field("shortDescription")
                .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
                .fuzziness(Fuzziness.AUTO);
        if (filterOptions.isPresent()) {
            return boolQuery().must(queryBuilder).must(
                    getFilterQuery(filterOptions.get())
            );
        }
        return queryBuilder;
    }

    private QueryBuilder getFilterQuery(final FilterOptions options) {
        BoolQueryBuilder qb = boolQuery();
        if (!CollectionUtils.isEmpty(options.getBrands())) {
            qb.must(termsQuery("brand", options.getBrands()));
        }
        if (!CollectionUtils.isEmpty(options.getL1Categories())) {
            qb.must(termsQuery("category1", options.getL1Categories()));
        }
        if (!CollectionUtils.isEmpty(options.getL2Categories())) {
            qb.must(termsQuery("category2", options.getL2Categories()));
        }
        if (!CollectionUtils.isEmpty(options.getLengths())) {
            qb.must(termsQuery("length", options.getLengths()));
        }
        if (!CollectionUtils.isEmpty(options.getWidths())) {
            qb.must(termsQuery("width", options.getWidths()));
        }
        if (!CollectionUtils.isEmpty(options.getHeights())) {
            qb.must(termsQuery("height", options.getHeights()));
        }
        if (!CollectionUtils.isEmpty(options.getWeights())) {
            qb.must(termsQuery("weight", options.getWeights()));
        }
        if (options.getPriceLow() != null) {
            qb.must(rangeQuery("price").gt(options.getPriceLow()));
        }
        if (options.getPriceHigh() != null) {
            qb.must(rangeQuery("price").lte(options.getPriceHigh()));
        }

        return qb;
    }


    public SearchResult searchByCategory(final String category1, final String category2, final Optional<FilterOptions> filterOptions, final Constants.SortBy sortBy) {
        SearchQuery searchQuery = withDefaultAggregations(new NativeSearchQueryBuilder()
                .withQuery(getCategoryMatchQuery(category1, category2, filterOptions))).withSort(prepareSortOptions(sortBy)).build();

        SearchResult result = performSearchOperation(searchQuery);

        List<Product> products = searchTemplate.queryForList(searchQuery, Product.class);
        result.setProducts(products);
        return result;
    }

    private QueryBuilder getCategoryMatchQuery(String cat1, String cat2, Optional<FilterOptions> filterOptions) {
        BoolQueryBuilder qb = QueryBuilders.boolQuery().must(termQuery("category1", cat1));
        if (!StringUtils.isEmpty(cat2)) {
            return qb.must(termQuery("category2", cat2));
        }
        if (filterOptions.isPresent()) {
            return boolQuery().must(qb).must(
                    getFilterQuery(filterOptions.get())
            );
        }
        return qb;
    }

    private SearchResult performSearchOperation(SearchQuery searchQuery) {
        return searchTemplate.query(searchQuery, response -> {
            SearchResult sr = new SearchResult();
            sr.setTotal(response.getHits().getTotalHits());
            SearchHit[] hits = response.getHits().getHits();
            sr.setProducts(Arrays.stream(hits).map(this::mapSearchHitToProduct).filter(Objects::nonNull).collect(Collectors.toList()));
            Optional.ofNullable(response.getAggregations()).ifPresent(aggregations -> {
                Map<String, Aggregation> aggs = aggregations.asMap();
                if (!CollectionUtils.isEmpty(aggs)) {
                    sr.setFacets(aggs.entrySet().stream()
                            .filter(entry -> entry.getValue() instanceof MultiBucketsAggregation)
                            .map(this::mapAggregationToFacet).collect(Collectors.toList()));
                }
            });
            return sr;
        });
    }

    private Facet mapAggregationToFacet(Map.Entry<String, Aggregation> entry) {
        MultiBucketsAggregation agg = (MultiBucketsAggregation) entry.getValue();
        Facet facet = new Facet();
        facet.setName(entry.getKey());
        facet.setBuckets(agg.getBuckets().stream().collect(Collectors.toMap(b -> b.getKeyAsString(), b -> b.getDocCount(), (e1, e2) -> e1,
                LinkedHashMap::new)));
        return facet;
    }

    private Product mapSearchHitToProduct(SearchHit hit) {
        Product product = null;
        String source = hit.getSourceAsString();
        try {
            product = objectMapper.readValue(source, Product.class);
        } catch (Exception e) {
            log.error("Error occurred while parsing search hit. Source: {}. Error={}", source, e);
        }
        return product;
    }

    private NativeSearchQueryBuilder withDefaultAggregations(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        TermsAggregationBuilder categoryAgg = AggregationBuilders.terms("Category").field("category1").order(BucketOrder.key(true));
        TermsAggregationBuilder subCategoryAgg = AggregationBuilders.terms("Sub category").field("category2").order(BucketOrder.key(true));
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("Brand").field("brand").order(BucketOrder.key(true));
        TermsAggregationBuilder heightAgg = AggregationBuilders.terms("Height").field("height").order(BucketOrder.key(true));
        TermsAggregationBuilder widthAgg = AggregationBuilders.terms("Width").field("width").order(BucketOrder.key(true));
        TermsAggregationBuilder lengthAgg = AggregationBuilders.terms("Length").field("length").order(BucketOrder.key(true));
        TermsAggregationBuilder weightAgg = AggregationBuilders.terms("Weight").field("weight").order(BucketOrder.key(true));
        RangeAggregationBuilder priceAgg = AggregationBuilders.range("Price ranges").field("price")
                .addRange("$0 - $10", 0, 9.99)
                .addRange("$10 - $20", 10, 19.99)
                .addRange("$20 - $30", 20, 29.99)
                .addRange("$30 - $40", 30, 39.99)
                .addRange("$40 - $50", 40, 49.99)
                .addUnboundedFrom("$50 - Above $50", 50.0);
        nativeSearchQueryBuilder
                .addAggregation(categoryAgg)
                .addAggregation(subCategoryAgg)
                .addAggregation(brandAgg)
                .addAggregation(priceAgg)
                .addAggregation(heightAgg)
                .addAggregation(widthAgg)
                .addAggregation(lengthAgg)
                .addAggregation(weightAgg);
        return nativeSearchQueryBuilder;
    }

    public Product getProduct(String productId) {
        GetQuery query = new GetQuery();
        query.setId(productId);
        return searchTemplate.queryForObject(query, Product.class);
    }
}
