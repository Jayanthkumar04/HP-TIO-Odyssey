package com.ascendion.agentflowmind.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_assignment")
public class RoleAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Integer assignmentId;

    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "agent_id")
    private Integer agentId;

    @Column(name = "workflow_id")
    private Integer workflowId;

    @Column (name = "team_id")
    private Integer teamId;
}
