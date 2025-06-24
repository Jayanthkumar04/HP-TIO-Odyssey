package com.ascendion.agentflowmind.util;

public class NativeQueries {

    //To Get WorkFLow List Mapped for User
    public static final String USER_WORKFLOW_LIST="SELECT new com.ascendion.agentflowmind.entity.WorkFlowMaster (wm.workflowId, wm.workflowName, wm.domain, wm.doc) from WorkFlowMaster wm INNER JOIN WorkflowUser wu on (wm.workflowId = wu.workflowId) " +
            "WHERE wu.teamId = :teamId group by wm.workflowId";
    //To Get Running Workflow mapped for user
    public static final String GET_USER_WORKFLOW =
            "SELECT new com.ascendion.agentflowmind.dto.WorkFlowMasterDTO(" +
                    "    wm.workflowId, " +
                    "    wm.workflowName, " +
                    "    wm.domain, " +
                    "    wm.doc, " +
                    "    wt.runningInstanceId, " +
                    "    wt.startDate, " +
                    "    wt.endDate, " +
                    "    wt.workflowStatus, " +
                    "    1 as stepno )" +
                    "FROM WorkFlowMaster wm " +
                    "LEFT JOIN WorkflowTransaction wt ON (wm.workflowId = wt.workflowId) " +
                    "INNER JOIN WorkflowUser ra ON (ra.workflowId = wt.workflowId) " +
                    "INNER JOIN User user ON (user.userId = ra.userId) " +
                    "WHERE ra.userId = :userID and wt.startDate>= :startDate and wt.startDate <= :endDate " +
                    "Order by wt.startDate DESC";
    //To get Activities Mapped for the User
    public static final String USER_WORKFLOW_ACTIVITIES="SELECT new com.ascendion.agentflowmind.dto.WorkFlowDTO(" +
            "    w.workflowId, " +
            "    w.workflowName, " +
            "    w.domain, " +
            "    a.activityName, " +
            "    a.stepNo, " +
            "    a.activityType," +
            "    wt.runningInstanceId," +
            "    wt.startDate," +
            "    wt.endDate," +
            "    wt.workflowStatus)" +
            "FROM ActivityMaster a " +
            "INNER JOIN WorkFlowMaster w ON (a.workflowId = w.workflowId) " +
            "INNER JOIN RoleAssignment r ON (a.activityId = r.activityId) " +
            "LEFT JOIN WorkflowTransaction wt ON (wt.workflowId = w.workflowId) " +
            " WHERE wt.workflowStatus not in ('COMPLETED','REJECTED') and r.userId = :userID and wt.startDate>= :startDate and wt.startDate <= :endDate " +
            "ORDER BY wt.startDate DESC";
    // To get team workflows and activity List
    public static final String WORKFLOW_ACTIVITIES_FOR_TEAM ="SELECT new com.ascendion.agentflowmind.dto.WorkFlowDTO(" +
            "    w.workflowId, " +
            "    w.workflowName, " +
            "    w.domain, " +
            "    a.activityName, " +
            "    a.stepNo, " +
            "    a.activityType," +
            "    wt.runningInstanceId," +
            "    wt.startDate," +
            "    wt.endDate," +
            "    wt.workflowStatus)" +
            "FROM ActivityMaster a " +
            "INNER JOIN WorkFlowMaster w ON (a.workflowId = w.workflowId) " +
            "INNER JOIN RoleAssignment r ON (a.activityId = r.activityId and a.workflowId = w.workflowId) " +
            "LEFT JOIN WorkflowTransaction wt ON (wt.workflowId = w.workflowId) " +
            " WHERE wt.workflowStatus NOT IN ('COMPLETED','REJECTED') and r.teamId = :teamId and wt.startDate>= :startDate and wt.startDate <= :endDate " +
            "ORDER BY wt.startDate DESC";
    //Workflow status based on the UID
    public static final String WORK_FLOW_STATUS="SELECT new com.ascendion.agentflowmind.dto.WorkFLowStatusList(" +
            "wm.workflowName, " +
            "act.activityStatus, " +
            "act.stepNo, " +
            "am.activityType, " +
            "case  "+
            "when ra.userId > 0 then 'USER' "+
            "when ra.roleId > 0 then 'ROLE' end as assingedType, "+
            "am.activityName, " +
            "am.executionMessage, " +
            "act.assignedTo, " +
            "act.url, " +
            "act.startTime," +
            "act.endTime) " +
            "FROM ActivityTransaction act " +
            "INNER JOIN ActivityMaster am ON (act.workflowId = am.workflowId and act.stepNo = am.stepNo) " +
            "INNER JOIN WorkFlowMaster wm ON (am.workflowId = wm.workflowId) " +
            "INNER JOIN RoleAssignment ra ON (act.stepNo = ra.activityId and act.workflowId = ra.workflowId) " +
            "WHERE act.workflowId = :workFlowId and act.runningInstanceId=:guId "+
            "ORDER BY act.startTime DESC";
    //get Workflow activities mapped for the Role
    public static final String WORK_FLOW_FOR_USER_ROLE_MODIFIED="SELECT new com.ascendion.agentflowmind.dto.WorkFlowDTO(" +
            "    w.workflowId, " +
            "    w.workflowName, " +
            "    w.domain, " +
            "    a.activityName, " +
            "    a.stepNo, " +
            "    a.activityType," +
            "    wt.runningInstanceId," +
            "    wt.startDate," +
            "    wt.endDate," +
            "    wt.workflowStatus)" +
            "FROM ActivityMaster a " +
            "INNER JOIN WorkFlowMaster w ON (a.workflowId = w.workflowId) " +
            "INNER JOIN RoleAssignment r ON (a.activityId = r.activityId) " +
            "LEFT JOIN WorkflowTransaction wt ON (wt.workflowId = w.workflowId) " +
            " WHERE wt.workflowStatus not in ('COMPLETED','REJECTED') and r.roleId = :userRole  and wt.startDate>= :startDate and wt.startDate <= :endDate " +
            "ORDER BY wt.startDate DESC";
    public static final String GET_ALL_WORKFLOW =
            "SELECT" +
                    "    wm.workflowId, " +
                    "    wm.workflowName, " +
                    "    wm.domain, " +
                    "    wm.doc, " +
                    "    wt.runningInstanceId, " +
                    "    wt.startDate, " +
                    "    wt.endDate, " +
                    "    wt.workflowStatus, " +
                    "    1 as stepno " +
                    "FROM WorkFlowMaster wm " +
                    "LEFT JOIN WorkflowTransaction wt ON (wm.workflowId = wt.workflowId)" +
                    "Order by wt.startDate DESC";
    public static final String FIND_LAST_ACTIVITY_FOR_WORKFLOW = "select  max(stepNo) from ActivityTransaction where workflowId= :workFlowId and runningInstanceId =:runningInstanceID";

}
