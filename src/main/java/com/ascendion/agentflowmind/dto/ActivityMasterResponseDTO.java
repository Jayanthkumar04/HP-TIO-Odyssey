package com.ascendion.agentflowmind.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityMasterResponseDTO {

 private Integer activityId;
 private Integer stepNum;
 private String activityName;
 private String activityType;
 private String decisionCode;
 private Integer rightChild;
 private Integer leftChild;
 private Integer parentId;

}
