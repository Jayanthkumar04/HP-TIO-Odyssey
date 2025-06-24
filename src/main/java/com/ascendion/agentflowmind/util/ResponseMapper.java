package com.ascendion.agentflowmind.util;

import com.ascendion.agentflowmind.dto.*;
import com.ascendion.agentflowmind.entity.ActivityTransaction;
import com.ascendion.agentflowmind.entity.Role;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResponseMapper {

    /*public WorkFlowStatusDTO toWorkFlowStatusDTO(ActivityTransaction activityTransaction) {
        if (activityTransaction == null)
            return null;
        return new WorkFlowStatusDTO(activityTransaction.getStepNo(), activityTransaction.getStatus(),
                activityTransaction.getAssignedTo());
    }*/
    public WorkFlowDTO toWorkFlowDTO(ActivityTransaction activityTransaction) {
        if (activityTransaction == null)
            return null;
        return null;

    }

    public WorkFlowStatusDTO toWorkFlowStatusDTO(List<WorkFLowStatusList> workFLowStatusList) {
        if (workFLowStatusList == null || workFLowStatusList.isEmpty())
            return null;
        String workFlowName = workFLowStatusList.get(0).getWorkflowName();
        List<ActivityStatusDTO> activityList = new ArrayList<ActivityStatusDTO>();
        for(WorkFLowStatusList workFLowStatus : workFLowStatusList){
            ActivityStatusDTO asObj = new ActivityStatusDTO(workFLowStatus.getStatus(),
                    workFLowStatus.getStepNo(),
                    //workFLowStatus.getTransActivityType(),
                    workFLowStatus.getActivityType(),
					workFLowStatus.getAssingedType(),
                    workFLowStatus.getActivityName(),
                    workFLowStatus.getExecutionMessage(),
                    workFLowStatus.getAssignedTo(),
                    workFLowStatus.getUrl(),
                    workFLowStatus.getStartDate(),
                    workFLowStatus.getEndDate());
            activityList.add(asObj);
        }

        return new WorkFlowStatusDTO(workFlowName,1,
                activityList);
    }

    public List<RoleAvtivityDTO>  roleWorkFlowMapper(List<WorkFlowDTO> workFlowDTOList, Role roleObj){
        if (workFlowDTOList == null || workFlowDTOList.isEmpty())
            return null;
        List<RoleAvtivityDTO> response = new ArrayList<>();
        List<ActivityDTO> activityDTOList = new ArrayList<>();
        String workFlowName = workFlowDTOList.get(0).getWorkflowName();
        int workFlowId = workFlowDTOList.get(0).getWorkflowId();
        String runningInstanceId = workFlowDTOList.get(0).getRunningInstanceId();
        String startDate = workFlowDTOList.get(0).getStartDate();
        String endDate = workFlowDTOList.get(0).getEndDate();
        String workflowStatus = workFlowDTOList.get(0).getWorkflowStatus();
        for(WorkFlowDTO obj : workFlowDTOList)
        {
            if(workFlowName.equalsIgnoreCase(obj.getWorkflowName()) && runningInstanceId.equals(obj.getRunningInstanceId())){
                activityDTOList.add(new ActivityDTO(
                        obj.getActivityName(),
                        obj.getActivityType(),
                        obj.getStepNo(),
                        obj.getDomain()
                ));
            }
            else
            {
                response.add(new RoleAvtivityDTO(workFlowName, workFlowId, runningInstanceId,workflowStatus,startDate, endDate, activityDTOList));
                activityDTOList = new ArrayList<>();
                workFlowName = obj.getWorkflowName();
                workFlowId = obj.getWorkflowId();
                runningInstanceId = obj.getRunningInstanceId();
                startDate = obj.getStartDate();
                endDate = obj.getEndDate();
                workflowStatus = obj.getWorkflowStatus();
            }
        }
        response.add(new RoleAvtivityDTO(workFlowName,workFlowId,runningInstanceId,workflowStatus,startDate,endDate, activityDTOList));
        return response;
    }
    public List<UserWorkFlowDTO> userWorkFlowMapper(List<WorkFlowDTO> workFlowDTOList) {
        if (workFlowDTOList == null || workFlowDTOList.isEmpty())
            return null;
        List<UserWorkFlowDTO> response = new ArrayList<>();
        List<ActivityDTO> activityDTOList = new ArrayList<>();
        String workFlowName = workFlowDTOList.get(0).getWorkflowName();
        int workFlowId = workFlowDTOList.get(0).getWorkflowId();
        String runningInstanceId = workFlowDTOList.get(0).getRunningInstanceId();
        String startDate = workFlowDTOList.get(0).getStartDate();
        String endDate = workFlowDTOList.get(0).getEndDate();
        String workflowStatus = workFlowDTOList.get(0).getWorkflowStatus();
        for(WorkFlowDTO obj : workFlowDTOList)
        {
            if(workFlowName.equalsIgnoreCase(obj.getWorkflowName())
            && runningInstanceId.equals(obj.getRunningInstanceId()))
            {
                activityDTOList.add(new ActivityDTO(
                        obj.getActivityName(),
                        obj.getActivityType(),
                        obj.getStepNo(),
                        obj.getDomain()
                ));
            }
            else
            {
                response.add(new UserWorkFlowDTO(workFlowName,workFlowId,runningInstanceId,workflowStatus,startDate, endDate, activityDTOList));
                activityDTOList = new ArrayList<>();
                workFlowName = obj.getWorkflowName();
                runningInstanceId = obj.getRunningInstanceId();
                workFlowId = obj.getWorkflowId();
                startDate = obj.getStartDate();
                endDate = obj.getEndDate();
                workflowStatus = obj.getWorkflowStatus();

            }
        }
        response.add(new UserWorkFlowDTO(workFlowName,workFlowId,runningInstanceId,workflowStatus,startDate, endDate, activityDTOList));
        return response;
    }
}
