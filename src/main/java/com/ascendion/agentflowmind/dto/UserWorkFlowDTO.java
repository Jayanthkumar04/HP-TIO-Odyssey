package com.ascendion.agentflowmind.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class UserWorkFlowDTO {
    String workFLowName;
    long workFlowId;
    String runningInstanceId;
    String workflowStatus;
    String startDate;
    String endDate;
    List<ActivityDTO> activityList;
    public UserWorkFlowDTO(String workFLowName, long workFlowId, String runningInstanceId, String workflowStatus, String startDate, String endDate, List<ActivityDTO> activityList) {
        this.workFLowName = workFLowName;
        this.workFlowId = workFlowId;
        this.runningInstanceId = runningInstanceId;
        this.workflowStatus = workflowStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.activityList = activityList;

    }

}
