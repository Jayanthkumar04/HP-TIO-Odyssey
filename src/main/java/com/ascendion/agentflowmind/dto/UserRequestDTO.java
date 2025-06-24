package com.ascendion.agentflowmind.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private long userId;
    private long teamId;
    private long roleId;
    private String startDate;
    private String endDate;

}
