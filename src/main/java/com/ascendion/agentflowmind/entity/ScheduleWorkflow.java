package com.ascendion.agentflowmind.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "scheduled_workflows")
public class ScheduleWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String workflowName;

    @Column(name = "created_at")
    private Long createdAt;

    private String runningInstanceId;

    private Long runAt;

    private String scheduleBy;

    private String userInputs;

    private String status;

    private String errorMessage;

}
