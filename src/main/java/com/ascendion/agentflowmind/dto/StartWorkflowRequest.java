package com.ascendion.agentflowmind.dto;

import lombok.Data;

import java.util.Map;

@Data
public class StartWorkflowRequest {
    private String workflowName;
    private String userInputs;
    private String runningInstanceId;
    private String userEmail;
}
