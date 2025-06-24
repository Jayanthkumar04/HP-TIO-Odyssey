package com.ascendion.agentflowmind.dto;

import lombok.Data;

import java.util.List;
@Data
public class RoleWorkFLowDTO {
    private long roleId;
    private String roleName;
    private String description;
    List<RoleAvtivityDTO> activityList;

    public RoleWorkFLowDTO(long roleId, String roleName, String description, List<RoleAvtivityDTO> activityList) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
        this.activityList = activityList;
    }
}
