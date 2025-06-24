package com.ascendion.agentflowmind.dto;

import lombok.Data;

@Data
public class ActivityStatusDTO {

    private String activityStatus;
    private int stepNo;
    //private String assignedType;
    private String activityType;
    private String assignedType;
    private String activityName;
    private String executionMessage;
    private String assignedTo;
    private String url;
    private String startDate;
    private String endDate;

    public ActivityStatusDTO(String activityStatus, int StepNo, String activitytype, String assignedType, String activityname, String executionMessage, String assignedTo, String url, String startDate, String endDate)
    {
        this.activityStatus = activityStatus;
        this.stepNo = StepNo;
        this.activityType = activitytype;
        this.assignedType = assignedType;
        this.activityName = activityname;
        this.executionMessage = executionMessage;
        this.assignedTo = assignedTo;
        this.url = url;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
