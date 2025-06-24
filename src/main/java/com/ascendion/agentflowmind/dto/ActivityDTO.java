package com.ascendion.agentflowmind.dto;

import lombok.Data;

@Data
public class ActivityDTO {
    String activityName;
    String activityType;
    int stepNo;
    String domain;

    public ActivityDTO(String activityName, String activityType, int stepNo, String domain) {
        this.activityName = activityName;
        this.activityType = activityType;
        this.stepNo = stepNo;
        this.domain = domain;

    }

}
