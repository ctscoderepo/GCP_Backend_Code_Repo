#!/usr/bin/env bash
logstash-plugin update logstash-filter-mutate;

logstash -f /etl/product-import-logstash.conf <  /etl/csv/products.csv;
#logstash -f /etl/attributes-import-logstash.conf <  /etl/json/product-attributes.json;