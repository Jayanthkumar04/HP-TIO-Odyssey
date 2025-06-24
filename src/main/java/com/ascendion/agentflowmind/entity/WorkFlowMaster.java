package com.ascendion.agentflowmind.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workflow_master")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkFlowMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workflow_id")
    private int workflowId;

    @Column(name = "workflow_name", nullable = false, length = 100)
    private String workflowName;

    @Column(name = "domain", length = 100)
    private String domain;

    @Column(name = "doc", nullable = false)
    private String doc;
}
