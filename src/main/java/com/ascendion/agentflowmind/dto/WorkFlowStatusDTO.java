package com.ascendion.agentflowmind.dto;

import java.util.List;

public record WorkFlowStatusDTO(String workFLowName, int workFlowId, List<ActivityStatusDTO> activityStatusDTOList) {

}
