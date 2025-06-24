package com.ascendion.agentflowmind.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTransaction {

    @Id
    @Column(name = "running_instance_id", columnDefinition = "CHAR(36)")
    private String runningInstanceId;

    @Column(name = "workflow_id")
    private Integer workflowId;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "workflow_status")
    private String workflowStatus;


}