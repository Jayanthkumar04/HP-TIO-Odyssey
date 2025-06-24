package com.ascendion.agentflowmind.repository;


import com.ascendion.agentflowmind.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepo extends JpaRepository<Agent, Integer> {
}