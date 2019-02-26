package com.gcp.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category {
    private String id;
    private String displayName;
    private String url;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> childCatIds;
    private List<Category> children;
}
