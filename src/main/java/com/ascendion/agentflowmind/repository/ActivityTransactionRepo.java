package com.ascendion.agentflowmind.repository;

import com.ascendion.agentflowmind.entity.ActivityTransaction;
import com.ascendion.agentflowmind.dto.WorkFLowStatusList;
import com.ascendion.agentflowmind.dto.WorkFlowDTO;

import com.ascendion.agentflowmind.util.NativeQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActivityTransactionRepo extends JpaRepository<ActivityTransaction, Long> {

    @Query(NativeQueries.USER_WORKFLOW_ACTIVITIES)
    List<WorkFlowDTO> getUserWorkflowActivities(long userID, String startDate, String endDate);

    @Query(NativeQueries.WORK_FLOW_FOR_USER_ROLE_MODIFIED)
    List<WorkFlowDTO> getWorkFlowsforRole(long userRole, String startDate, String endDate);

    @Query(NativeQueries.WORKFLOW_ACTIVITIES_FOR_TEAM)
    List<WorkFlowDTO> getTeamActivities(long teamId, String startDate, String endDate);

    @Query(NativeQueries.WORK_FLOW_STATUS)
    List<WorkFLowStatusList> getActivityUserStausbyUid(long workFlowId,String guId);

    @Query(NativeQueries.FIND_LAST_ACTIVITY_FOR_WORKFLOW)
    Object[] getStepNumber(long workFlowId,String runningInstanceID);

    Optional<ActivityTransaction> findFirstByRunningInstanceIdAndActivityStatusOrderByStepNoAsc(String instanceId, String activityStatus);
    List<ActivityTransaction> findByRunningInstanceId(String instanceId);

    Optional<ActivityTransaction> findByWorkflowIdAndRunningInstanceIdAndStepNo(Long workflowId, String runningInstanceId, Integer stepNo);

    Optional<ActivityTransaction> findFirstByRunningInstanceIdAndActivityStatusOrderByExecutionIdDesc(String runningInstanceId, String activityStatus);

    @Query("SELECT MAX(at.executionId) FROM ActivityTransaction at WHERE at.runningInstanceId = :instanceId")
    Integer findMaxExecutionIdByRunningInstanceId(@Param("instanceId") String instanceId);

    Optional<ActivityTransaction> findTopByRunningInstanceIdAndExecutionIdLessThanOrderByExecutionIdDesc(String instanceId, int executionId);
}
