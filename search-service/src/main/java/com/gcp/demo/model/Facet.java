package com.gcp.demo.model;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Facet {
    private String name;
    private LinkedHashMap<String, Long> buckets;
}
