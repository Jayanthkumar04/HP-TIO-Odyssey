package com.ascendion.agentflowmind.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "activity_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "workflow_id")
    private Integer workflowId;

    @Column(name = "step_no")
    private Integer stepNo;

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "decision_key")
    private String decisionKey;

    @Column(name = "right_child")
    private Integer rightChild;

    @Column(name = "left_child")
    private Integer leftChild;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "execution_message")
    private String executionMessage;

}
