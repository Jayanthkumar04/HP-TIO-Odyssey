package com.ascendion.agentflowmind.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table (name="activity_transaction")
public class ActivityTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "workflow_id")
    private int workflowId;

    @Column(name = "running_instance_id", columnDefinition = "CHAR(36)")
    private String runningInstanceId;

    @Column(name = "execution_id")
    private int executionId;

    @Column(name = "step_no")
    private int stepNo;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "activity_status")
    private String activityStatus;

    @Column(name = "url")
    private String url;

    @Column(name = "activity_input")
    private String activity_input;

    @Column(name = "activity_output")
    private String activity_output;

    @Column(name = "performed_by")
    private String performedBy;

    @Column(name = "comment")
    private String comment;
}
