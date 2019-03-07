package com.gcp.vision.api.model;

import lombok.Data;

@Data
public class TextLabel {
	private String mid;
	private String description;
	private double score;
	private double topicality;
}

