package com.ascendion.agentflowmind.repository;


import com.ascendion.agentflowmind.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepo extends JpaRepository<Team, Long> {
}
