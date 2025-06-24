package com.ascendion.agentflowmind.repository;

import com.ascendion.agentflowmind.entity.WorkflowTransaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WorkFlowTransactionRepo extends JpaRepository<WorkflowTransaction, String> {

    @Transactional
    @Modifying
    @Query("DELETE FROM WorkflowTransaction wt WHERE wt.runningInstanceId = :runningInstanceId")
    void deleteByRunningInstanceId(@Param("runningInstanceId") String runningInstanceId);

}
