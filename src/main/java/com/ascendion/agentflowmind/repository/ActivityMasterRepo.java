package com.ascendion.agentflowmind.repository;


import com.ascendion.agentflowmind.entity.ActivityMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ActivityMasterRepo extends JpaRepository<ActivityMaster, Integer> {
    List<ActivityMaster> findByWorkflowIdOrderByStepNoAsc(Integer workflowId);
    Optional<ActivityMaster> findFirstByWorkflowIdAndStepNo(Integer workflowId, Integer stepNo);
    Optional<ActivityMaster> findFirstByWorkflowIdAndStepNoGreaterThanOrderByStepNoAsc(Integer workflowId, Integer stepNo);
    Optional<ActivityMaster> findFirstByWorkflowIdOrderByStepNoAsc(Integer workflowId);
    Optional<ActivityMaster> findByWorkflowIdAndStepNo(Integer workflowId, Integer stepNo);
}