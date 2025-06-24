package com.ascendion.agentflowmind.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamWorkflowDTO {
    private long teamId;
    private String teamName;
    private String description;
    List<RoleAvtivityDTO> activityList;
}
