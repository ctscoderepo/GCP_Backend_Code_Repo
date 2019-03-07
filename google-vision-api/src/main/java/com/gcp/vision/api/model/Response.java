package com.gcp.vision.api.model;

import java.util.List;
import lombok.Data;

@Data
public class Response {
private List<LabelAnnotations> responses;
}
