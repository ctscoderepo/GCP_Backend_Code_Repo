package com.gcp.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "category_index", type = "_doc")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
@Setter
public class CategoryDocument {
    private String id;
    private String displayName;
    private String url;
    private List<String> children;

    public Category toCategory() {
        return new Category(id, displayName, url, children, null);
    }
}
