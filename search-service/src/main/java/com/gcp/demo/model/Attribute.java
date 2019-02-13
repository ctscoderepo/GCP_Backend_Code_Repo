package com.gcp.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Attribute {
    private String productId;
    private String id, name, value, textPrompt, attrType, colorSquaresRgb;
    private boolean required, allowFiltering;
    private int attrOrder, attrSubOrder;
}
