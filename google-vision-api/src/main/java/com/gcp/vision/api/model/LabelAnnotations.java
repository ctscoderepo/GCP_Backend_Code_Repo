package com.gcp.vision.api.model;

import java.util.List;
import lombok.Data;

/**
 * @author Anuj Kumar
 * 
 * This class is used to LabelAnnotations in vision api response
 */
@Data
public class LabelAnnotations {
	private List<TextLabel> labelAnnotations;

}
