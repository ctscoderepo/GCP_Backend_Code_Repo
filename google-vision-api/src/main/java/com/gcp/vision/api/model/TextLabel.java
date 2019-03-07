package com.gcp.vision.api.model;

import lombok.Data;

/**
 * @author Anuj Kumar
 * 
 * This class is used to hold textabels in vision api response
 */
@Data
public class TextLabel {
	private String mid;
	private String description;
	private double score;
	private double topicality;
}

