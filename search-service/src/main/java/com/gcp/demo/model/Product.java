package com.gcp.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Arrays;
import java.util.List;


@Document(indexName = "product_catalog", type = "_doc")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {
    private String id;
    private String productName;
    private String shortDescription;
    private String longDescription;
    private String canonicalUrl;
    private String brand;
    private String skuId;
    private String currencyCode;
    private String weight;
    private String length;
    private String width;
    private String height;
    private List<String> categories;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String category1;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String category2;
    private int stockQuantity;
    private double price;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String image1;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String image2;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String image3;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String image4;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String image5;
    private List<String> images;

    public List<String> getCategories() {
      return Arrays.asList(this.getCategory1(), this.getCategory2());
    }

    public List<String> getImages() {
        return Arrays.asList(image1, image2, image3, image4, image5);
    }
}
