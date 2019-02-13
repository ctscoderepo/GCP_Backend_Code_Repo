package com.gcp.demo.bootstrap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcp.demo.model.Product;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ApplicationPreparedEventListener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private ElasticsearchTemplate esClient;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String indexName = "product_catalog";
        createIndex("settings.json", "product_elasticsearch_mapping.json", indexName);
        indexData("data.json", indexName);
    }

    private void createIndex(String settingsFile, String mappingFile, String indexName) {

        try {
            if (!esClient.indexExists(indexName)) {
                Resource settingsResource = new ClassPathResource(settingsFile);
                Resource mappingResource = new ClassPathResource(mappingFile);
                String settings = readResourceToStringUsingStreams(settingsResource);
                esClient.createIndex(indexName, settings);
                String mapping = readResourceToStringUsingStreams(mappingResource);
                esClient.putMapping(indexName, "_doc", mapping);
                log.info("ElasticSearch Index={} created", indexName);
            } else {
                log.info("ElasticSearch Index={} exists already.", indexName);
            }
        } catch (ResourceAlreadyExistsException e) {
            log.info("ElasticSearch Index={} already exists", indexName, e);
        } catch (Exception e) {
            log.error("Unable to read the elasticSearch mappings file. Error={}", e);
        }
    }

    private void indexData(String dataFile, String indexName) {
        try {
            String data = readResourceToStringUsingStreams(new ClassPathResource(dataFile));
            ObjectMapper mapper = new ObjectMapper();
            List<ProductDoc> docs = mapper.readValue(data, new TypeReference<List<ProductDoc>>() {
            });
            docs.stream().forEach(doc -> {
                        try {
                            String source = mapper.writeValueAsString(doc);
                            IndexQuery query = new IndexQuery();
                            query.setSource(source);
                            query.setId(doc.getId());
                            query.setType("_doc");
                            query.setIndexName(indexName);
                            esClient.index(query);
                            log.info("Indexing successful for \"{}\"", doc);
                        } catch (Exception e) {
                            log.error("Indexing failed for \"{}\"", doc, e);
                        }
                    }
            );
        } catch (Exception e) {
            log.error("Unable to load the data file to ES", e);
        }

    }

    private String readResourceToStringUsingStreams(final Resource resource) throws IOException {
        InputStream inputStream = resource.getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
    }

    @ToString
    @Getter
    @Setter
    private static class ProductDoc {
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
        private String category1;
        private String category2;
        private int stockQuantity;
        private double price;
        private String image1;
        private String image2;
        private String image3;
        private String image4;
        private String image5;

        public String getId() {
            return skuId;
        }

        public void setId(String id) {
            this.skuId = id;
        }
    }
}
