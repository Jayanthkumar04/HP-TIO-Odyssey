package com.ascendion.agentflowmind.repository;

import com.ascendion.agentflowmind.entity.RoleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleAssignmentRepo extends JpaRepository<RoleAssignment, Integer> {
    Optional<RoleAssignment> findByActivityIdAndWorkflowId(Integer activityId,Integer workFlowId);
}
