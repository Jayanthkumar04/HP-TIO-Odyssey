package com.ascendion.agentflowmind.dto;

import lombok.Data;

@Data
public class WorkFlowDTO
{
    private int workflowId;
    private String workflowName;
    private String domain;
    private String activityName;
    private int stepNo;
    private String activityType;
    private String runningInstanceId;
    private String startDate;
    private String endDate;
    private String workflowStatus;

    WorkFlowDTO(int workflowId, String workflowName, String domain, String activityName, int stepNo, String activityType, String runningInstanceId, String startDate, String endDate, String workflowStatus){
        this.workflowId = workflowId;
        this.workflowName = workflowName;
        this.domain = domain;
        this.activityName = activityName;
        this.stepNo = stepNo;
        this.activityType = activityType;
        this.runningInstanceId = runningInstanceId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.workflowStatus = workflowStatus;
    }

}
