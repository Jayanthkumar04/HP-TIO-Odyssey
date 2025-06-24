package com.ascendion.agentflowmind.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowRequestDTO {
    private int userId;
    private String startDate;
    private String endDate;
    private int page;
    private int pageSize;
}
