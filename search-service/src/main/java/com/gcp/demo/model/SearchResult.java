package com.gcp.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchResult {
    private long total;
    private List<Product> products;
    private List<Facet> facets;
}

