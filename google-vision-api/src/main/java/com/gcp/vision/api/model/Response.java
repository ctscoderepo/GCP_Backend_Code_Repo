package com.gcp.vision.api.model;

import java.util.List;
import lombok.Data;

/**
 * @author Anuj Kumar
 * 
 * This class is used to hold vision api response
 */
@Data
public class Response {
private List<LabelAnnotations> responses;
}
