package com.ascendion.agentflowmind.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleWorkflowDto {
    private String workflowName;
    private Long runAt;
    private String scheduleBy;
    private String userInputs;
}
