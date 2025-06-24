package com.ascendion.agentflowmind.dto;

import lombok.Data;

import java.util.List;
@Data

public class RoleAvtivityDTO {

    String workFlowName;
    long workFlowId;
    String runningInstanceId;
    String workflowStatus;
    String startDate;
    String endDate;
    List<ActivityDTO> activityList;
    public RoleAvtivityDTO(String workFlowName, long workFlowId, String runningInstanceId, String workflowStatus,String startDate, String endDate, List<ActivityDTO> activityList) {
        this.workFlowName = workFlowName;
        this.workFlowId = workFlowId;
        this.runningInstanceId = runningInstanceId;
        this.workflowStatus = workflowStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.activityList = activityList;

    }

}
