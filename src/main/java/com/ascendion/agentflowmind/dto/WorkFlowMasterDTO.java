package com.ascendion.agentflowmind.dto;

import lombok.Data;

@Data
public class WorkFlowMasterDTO
{
    private int workflowId;
    private String workflowName;
    private String domain;
    private String doc;
    private String runningInstanceId;
    private String startDate;
    private String endDate;
    private String status;
    private int stepNo;

    public WorkFlowMasterDTO(int workflowid, String workflowname, String domain, String doc, String runninginstanceid,  String startdate, String endDate, String w_status, int stepNo){
        this.workflowId = workflowid;
        this.workflowName = workflowname;
        this.domain = domain;
        this.doc = doc;
        this.runningInstanceId = runninginstanceid;
        this.startDate = startdate;
        this.endDate = endDate;
        this.status = w_status;
        this.stepNo = stepNo;
    }
}
