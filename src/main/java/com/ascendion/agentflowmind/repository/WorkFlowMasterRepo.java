package com.ascendion.agentflowmind.repository;

import com.ascendion.agentflowmind.entity.WorkFlowMaster;
import com.ascendion.agentflowmind.dto.WorkFlowMasterDTO;
import com.ascendion.agentflowmind.util.NativeQueries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkFlowMasterRepo extends JpaRepository<WorkFlowMaster, Integer> {
    @Query(NativeQueries.GET_ALL_WORKFLOW)
    List<Object[]> getAllWorkFlow();
    Optional<WorkFlowMaster> findByWorkflowName(String workflowName);

    @Query(NativeQueries.USER_WORKFLOW_LIST)
    List<WorkFlowMaster> getUserWorkFlowList(int teamId);

    @Query(NativeQueries.GET_USER_WORKFLOW)
    Page<WorkFlowMasterDTO> getUserWorkFlow(int userID, String startDate, String endDate, Pageable pageable);


}



