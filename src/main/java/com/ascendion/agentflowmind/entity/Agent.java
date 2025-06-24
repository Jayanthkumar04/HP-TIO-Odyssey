package com.ascendion.agentflowmind.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "agent")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agent_id")
    private Integer agentId;

    @Column(name = "agent_name", nullable = false, length = 100)
    private String agentName;

    @Column(name = "agent_url", nullable = false, unique = true, length = 150)
    private String agentUrl;

    @Column(name = "pipeline_id", nullable = false)
    private Integer pipelineId;

    @Column(name = "header", nullable = false, columnDefinition = "json")
    private String header;
}