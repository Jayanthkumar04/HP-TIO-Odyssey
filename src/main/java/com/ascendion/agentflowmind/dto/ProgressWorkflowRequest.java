package com.ascendion.agentflowmind.dto;

import com.ascendion.agentflowmind.entity.User;
import lombok.Data;

@Data
public class ProgressWorkflowRequest {
    private String instanceId;
    private User user;
    private String comment;
}
