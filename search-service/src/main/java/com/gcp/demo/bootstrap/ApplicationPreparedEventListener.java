package com.gcp.demo.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
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
    private static final String CATEGORY_INDEX = "category_index";
    private static final String PRODUCT_CATALOG = "product_catalog";
    @Autowired
    private ElasticsearchTemplate esClient;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createIndex("product_index_settings.json", "product_elasticsearch_mapping.json", PRODUCT_CATALOG);
        indexData("product_data.json", PRODUCT_CATALOG, ProductDoc.class);

        createIndex("category_index_settings.json", "category_elasticsearch_mapping.json", CATEGORY_INDEX);
        indexData("category_data.json", CATEGORY_INDEX, Category.class);
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

    private void indexData(String dataFile, String indexName, Class dataType) {
        try {
            String data = readResourceToStringUsingStreams(new ClassPathResource(dataFile));
            ObjectMapper mapper = new ObjectMapper();
            List<Indexable> docs = mapper.readValue(data, mapper.getTypeFactory().constructCollectionType(List.class, dataType));
            docs.forEach(doc -> {
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

    private interface Indexable {
        String getId();
    }

    @ToString
    @Getter
    @Setter
    private static class ProductDoc implements Indexable {
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
            return getSkuId();
        }

        public void setId(String id) {
            setSkuId(id);
        }
    }

    @Getter
    @Setter
    @ToString
    private static class Category implements Indexable {
        private String id;
        private String displayName;
        private String url;
        private List<String> children;
    }
}