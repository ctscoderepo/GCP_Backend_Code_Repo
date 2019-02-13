#!/usr/bin/env bash
logstash-plugin update logstash-filter-mutate;

logstash -f /etl/product-import-logstash.conf < /etl/products.txt;
