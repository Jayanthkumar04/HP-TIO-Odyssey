package com.ascendion.agentflowmind.repository;

import com.ascendion.agentflowmind.entity.ScheduleWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduledWorkflowRepository extends JpaRepository<ScheduleWorkflow, Long> {

    Optional<ScheduleWorkflow> findByRunningInstanceId(String runningInstanceId);

    List<ScheduleWorkflow> findByRunningInstanceIdAndStatus(String runningInstanceId, String workflowStatus);
}
