package com.ascendion.agentflowmind.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkFLowStatusList
{
    private String workflowName;
    private String status;
    private int stepNo;
    private String activityType;
    private String assingedType;
    private String activityName;
    private String assignedTo;
    private String url;
    private String startDate;
    private String endDate;
    private String executionMessage;

    WorkFLowStatusList(String workflowName, String status, int stepNo, String activityType, String assingedType, String activityName, String executionMessage, String assignedTo, String url, String startDate, String endDate)
    {
        this.workflowName = workflowName;
        this.status = status;
        this.stepNo = stepNo;
        this.activityType = activityType;
        this.assingedType = assingedType;
        this.activityName = activityName;
        this.executionMessage = executionMessage;
        this.assignedTo = assignedTo;
        this.url = url;
        this.startDate = startDate;
        this.endDate = endDate;

    }
}
