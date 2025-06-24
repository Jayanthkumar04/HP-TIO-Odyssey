package com.ascendion.agentflowmind.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "WorkflowUser")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowUser {
    @Id
    @Column(name="workflow_id")
    private  Integer workflowId;
    @Column(name="user_id")
    private Integer userId;
    @Column(name="team_id")
    private Integer teamId;
    @Column(name="role_id")
    private Integer roleId;
}
