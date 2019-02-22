package com.gcp.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FilterOptions {
    private List<String> brands;
    private List<String> l1Categories;
    private List<String> l2Categories;
    private List<String> lengths;
    private List<String> widths;
    private List<String> heights;
    private List<String> weights;
    private Double priceLow;
    private Double priceHigh;
}
