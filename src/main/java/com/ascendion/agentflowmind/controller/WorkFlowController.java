package com.ascendion.agentflowmind.controller;

import com.ascendion.agentflowmind.dto.*;
import com.ascendion.agentflowmind.entity.ScheduleWorkflow;
import com.ascendion.agentflowmind.entity.WorkFlowMaster;
import com.ascendion.agentflowmind.service.BatchService;
import com.ascendion.agentflowmind.service.WorkFlowService;
import com.ascendion.agentflowmind.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/workflow/monitoring")
/**
 * WorkFlowController - TO handle All request related to Workflow, WorkflowActivities
 * @author Nithyananda CV
 */
public class WorkFlowController {

    @Autowired WorkFlowService workFlowService;
    @Autowired BatchService batchService;

    /**
     * Get WorkFLow List Mapped for User(Workflow Master Data)
     * @param userId
     * @return
     */
    @GetMapping(path="/getUserWorkFlowList/{userId}")
    public  ResponseEntity<ApiResponse<List<WorkFlowMaster>>> getUserWorkFlowList(@PathVariable int userId) {
        List<WorkFlowMaster> result = null;
        ApiResponse<List<WorkFlowMaster>> response = null;
        try{
            result = workFlowService.getWorkflowList(userId);
            if(!result.isEmpty()){
                response = ResponseUtil.success(HttpStatus.OK.value(), "Success",result);
            }
            else{
                response = ResponseUtil.error(HttpStatus.NO_CONTENT.value(), "Not Found",result);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(),null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * To get Running WorkFlows for User
     * @param requestObj
     * @return WorkFlowMasterDTO
     */
    @PostMapping(path="/getUserWorkflows")
            public  ResponseEntity<ApiResponse<Page<WorkFlowMasterDTO>>> getUserWorkflow(@RequestBody WorkflowRequestDTO requestObj) {
        Page<WorkFlowMasterDTO> result = null;
        ApiResponse<Page<WorkFlowMasterDTO>> response = null;
        try{
            result = workFlowService.getUserWorkflows(requestObj);
            if(result!=null && !result.isEmpty()){
                response = ResponseUtil.success(HttpStatus.OK.value(), "Success",result);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            else{
                response = ResponseUtil.error(HttpStatus.NO_CONTENT.value(), "Not Found",result);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(),null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * to get User Activities
     * @param userReq (RoleId, StartDate, EndDate)
     * @return UserWorkFlowDTO List - Workflow-> [Activity List]
     */
    @PostMapping("/getUserActivities")
    public ResponseEntity<ApiResponse<List<UserWorkFlowDTO>>> getUserActivities(@RequestBody UserRequestDTO userReq) {
        ApiResponse<List<UserWorkFlowDTO> > response = null;
        try{
            List<UserWorkFlowDTO> result = workFlowService.getUserWorkflowActivities(userReq);
            if(result!=null && !result.isEmpty()){
                response = ResponseUtil.success(HttpStatus.OK.value(), "Success",result);
            }
            else{
                response = ResponseUtil.error(HttpStatus.NO_CONTENT.value(), "WorkFlow Not Found for User Id :"+userReq.toString(),result);
            }
        }catch (Exception e){
            return new ResponseEntity<>(ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(),null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * to get the Team Workflows and Activities mapped to team
     * @param userReq (RoleId, StartDate, EndDate)
     * @return
     */
    @PostMapping("/getTeamActivities")
    public ResponseEntity<ApiResponse<TeamWorkflowDTO>> getTeamWorkflowActivities(@RequestBody UserRequestDTO userReq) {
        ApiResponse<TeamWorkflowDTO> response = null;
        try{
            TeamWorkflowDTO result = workFlowService.getTeamActivities(userReq);
            if(result!=null){
                response = ResponseUtil.success(HttpStatus.OK.value(), "Success",result);
            }
            else{
                response = ResponseUtil.error(HttpStatus.NO_CONTENT.value(), "WorkFlow Not Found for User Role :"+userReq.toString(),result);
            }
        }catch (Exception e){
            return new ResponseEntity<>(ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(),null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * to get the Team Workflows and Activities mapped to Role
     * @param userReq (RoleId, StartDate, EndDate)
     * @return
     */
    @PostMapping("/getRoleActivities")
    public ResponseEntity<ApiResponse<RoleWorkFLowDTO>> getWorkFlowsforRole(@RequestBody UserRequestDTO userReq) {
        ApiResponse<RoleWorkFLowDTO> response = null;
        try{
            RoleWorkFLowDTO result = workFlowService.getWorkFlowsforRole(userReq);

            if(result!=null){
                response = ResponseUtil.success(HttpStatus.OK.value(), "Success",result);
            }
            else{
                response = ResponseUtil.error(HttpStatus.NO_CONTENT.value(), "WorkFlow Not Found for User Role :"+userReq.toString(),result);
            }
        }catch (Exception e){
            return new ResponseEntity<>(ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(),null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * Return workflow Deffination -> All Activities mapped to the workflow including child info
     * @param workFlowId
     * @return
     */
    @GetMapping("getWorkflowDefinition")
    public ResponseEntity<ApiResponse<List<ActivityMasterResponseDTO>>> getWorkflowDefinition(@RequestParam Integer workFlowId) {
        ApiResponse<List<ActivityMasterResponseDTO>> response = null;
        try{
            Map<String, List<ActivityMasterResponseDTO>> resMap = workFlowService.getWorkFlowDefinition(workFlowId);
            String workflowName = null;

            List<ActivityMasterResponseDTO> activityList = null;
            if (!resMap.isEmpty() && resMap != null) {
                Optional<String> key = resMap.entrySet().stream().map(Map.Entry::getKey).findFirst();
                if (key.isPresent())
                    workflowName = key.get();
                activityList = workflowName != null ? resMap.get(workflowName) : new ArrayList<>();
            }
            if(!activityList.isEmpty()){
                response = ResponseUtil.customReponse(HttpStatus.OK.value(), "Success", workflowName, activityList);
            } else {
                response = ResponseUtil.error(HttpStatus.NO_CONTENT.value(), "Not Found",activityList);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(),null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("getWorkflowStatus")
    public ResponseEntity<ApiResponse<WorkFlowStatusDTO>> getWorkflowStatusUsingGuiID(@RequestParam Long workFlowID, @RequestParam(required = false, defaultValue = "") String UUID) {
        try{
            WorkFlowStatusDTO result = workFlowService.getWorkflowStatusUsingGuiID(workFlowID, UUID);
            if(result == null){
                return new ResponseEntity<>(ResponseUtil.error(HttpStatus.NO_CONTENT.value(), "Workflow Not Found",result), HttpStatus.OK);
            }
            return new ResponseEntity<>(ResponseUtil.success(HttpStatus.OK.value(), "Success",result), HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(),null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/start")
    public ResponseEntity<ApiResponse<String>> startWorkflow(@RequestBody StartWorkflowRequest request) {
        ApiResponse<String> response;
        String instanceId = workFlowService.startWorkflow(
                request.getWorkflowName(),
                request.getUserInputs(),
                request.getRunningInstanceId(),
                request.getUserEmail()
        );
        if (instanceId != null) {
            response = ResponseUtil.success(HttpStatus.OK.value(), "Workflow started successfully", instanceId);
        } else {
            response = ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to start workflow", null);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/progress")
    public ResponseEntity<ApiResponse<String>> progressWorkflow(@RequestBody ProgressWorkflowRequest progressWorkflowRequest) {
        ApiResponse<String> response = null;
        String result = workFlowService.progressWorkflow(progressWorkflowRequest);
        if(result != null){
            response = ResponseUtil.success(HttpStatus.OK.value(), "Success",result);
        }else{
            response = ResponseUtil.error(HttpStatus.NO_CONTENT.value(), "Not Found",result);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping(path="/batchRunner")
    public  ResponseEntity<ApiResponse<String>> batchRunner() {
        ApiResponse<String> response = null;
    try {
		//Path Created for Testing .
        String filePath = "C:\\HP\\MyHPAutomation\\Runner.bat";
        int exitCode = batchService.batchRunner(filePath);

        if(exitCode == 0){
            response = ResponseUtil.success(HttpStatus.OK.value(), "Success","Batch file executed successfully.");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        else{
            response = ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "","Batch file execution failed with code: " + exitCode);
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    catch(Exception e){
    }
        response = ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "","Batch file execution failed!!");
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/reject")
    public ResponseEntity<ApiResponse<String>> rejectWorkflow(@RequestParam String instanceId) {
        ApiResponse<String> response = null;
        String result = workFlowService.rejectWorkflow(instanceId);
        if(result != null){
            response = ResponseUtil.success(HttpStatus.OK.value(), "Success",result);
        }
        else{
            response = ResponseUtil.error(HttpStatus.NO_CONTENT.value(), "Not Found",result);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @PostMapping("/schedule")
    public ResponseEntity<String> scheduleWorkflow(@RequestBody ScheduleWorkflowDto request) {
        try {
            workFlowService.scheduleWorkflow(request);
            return ResponseEntity.ok("Workflow scheduled at epoch: " + request.getRunAt());
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body("Scheduling failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/cancelScheduledWorkflow")
    public ResponseEntity<ApiResponse<String>> cancelScheduledWorkflow(@RequestParam String instanceId) {
        boolean cancelled = workFlowService.cancelScheduledWorkflow(instanceId);
        if (!cancelled) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.error(HttpStatus.NOT_FOUND.value(), "No scheduled workflow found or cancellation failed", null));
        }
        return ResponseEntity.ok(
                ResponseUtil.success(HttpStatus.OK.value(), "Scheduled workflow successfully cancelled", instanceId)
        );
    }
}
