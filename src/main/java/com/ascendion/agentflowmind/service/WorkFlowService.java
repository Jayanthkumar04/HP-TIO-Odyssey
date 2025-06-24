package com.ascendion.agentflowmind.service;

import com.ascendion.agentflowmind.dto.*;
import com.ascendion.agentflowmind.entity.*;
import com.ascendion.agentflowmind.repository.*;
import com.ascendion.agentflowmind.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WorkFlowService - TO process All request related to Workflow, WorkflowActivities
 * @author Nithyananda CV
 */
@Service
public class WorkFlowService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired private WorkFlowMasterRepo workFlowRepo;
    @Autowired private ActivityTransactionRepo activityTransactionRepo;
    @Autowired ResponseMapper responseMapper;
    @Autowired RoleMasterRepo roleMasterRepo;
    @Autowired private WorkFlowTransactionRepo workflowTransactionRepo;
    @Autowired private RoleAssignmentRepo roleAssignmentRepo;
    @Autowired private AgentRepo agentRepo;
    @Autowired private ActivityMasterRepo activityMasterRepo;
    @Autowired private RestTemplate restTemplate;
    @Autowired TeamRepo teamRepo;
    @Autowired ScheduledWorkflowRepository scheduledWorkflowRepository;
    @Autowired Scheduler scheduler;
    @Autowired WorkFlowMasterRepo workFlowMasterRepo;
    @Autowired UserRepository userRepo;
    @Autowired UserTeamRepository userTeam;

    private static final Logger logger = LoggerFactory.getLogger(WorkFlowService.class);

    public List<WorkFlowMaster> getWorkflowList(int userID){
        try{
            logger.info("WorkFlowService.getWorkflowList...");
            int teamId = userTeam.findTeamIdByUserId(userID);
            return  workFlowRepo.getUserWorkFlowList(teamId);
        } catch (Exception e) {
            logger.error("WorkFlowService.getWorkflowList..."+e.getMessage());
        }
        return null;
    }

    public Page<WorkFlowMasterDTO> getUserWorkflows(WorkflowRequestDTO requestObj) {
        logger.info("WorkFlowService.getAllWorkFlows started");
        if(requestObj==null || requestObj.getUserId()<=0)
            return null;
        Page<WorkFlowMasterDTO> responseObj = null;
        try{
            responseObj = getUserWorkflow(requestObj);
        }catch (Exception e){
            logger.error("Error :: WorkFlowService.getAllWorkFlows"+e.getMessage());
        }
        return responseObj;
    }

    public WorkFlowStatusDTO getWorkflowStatusUsingGuiID(long workflowId, String uuId) {
        logger.info("getWorkflowStatus Service Call Started");
        List<WorkFLowStatusList>  response = null;
        if(!uuId.isEmpty())
            response = getActivityUserStausbyUid(workflowId, uuId);

        return responseMapper.toWorkFlowStatusDTO(response);
    }

    public RoleWorkFLowDTO getWorkFlowsforRole(UserRequestDTO userReq) {
        logger.info("GetWorkFlowsforRole Service Call Started");
        List<WorkFlowDTO> workFlowDTOList = null;
        RoleWorkFLowDTO responseObj = null;
        try{
            workFlowDTOList = activityTransactionRepo.getWorkFlowsforRole(userReq.getTeamId(),userReq.getStartDate(),userReq.getEndDate());
            Role res = null;
            if(workFlowDTOList!=null && !workFlowDTOList.isEmpty()) {
                res = roleMasterRepo.findById(userReq.getRoleId()).orElse(new Role());
            }
            List<RoleAvtivityDTO>  roleActivityList = responseMapper.roleWorkFlowMapper(workFlowDTOList,res);
            responseObj =  new RoleWorkFLowDTO(res.getRoleId(),res.getRoleName(),res.getDescription(),roleActivityList);

        } catch (Exception e) {
            logger.error("Error In getWorkFlowsforRole ::" +e.getMessage() );
        }
        return responseObj;
    }
    public TeamWorkflowDTO getTeamActivities(UserRequestDTO userReq) {
        logger.info("getTeamActivities Service Call Started");
        List<WorkFlowDTO> workFlowDTOList = null;
        TeamWorkflowDTO responseObj = null;
        try{
            workFlowDTOList = activityTransactionRepo.getTeamActivities(userReq.getTeamId(),userReq.getStartDate(),userReq.getEndDate());
            Team res = null;
            if(workFlowDTOList!=null && !workFlowDTOList.isEmpty()) {
                res = teamRepo.findById(userReq.getTeamId()).orElse(new Team());
            }
            List<RoleAvtivityDTO>  roleActivityList = responseMapper.roleWorkFlowMapper(workFlowDTOList,null);
            responseObj = new TeamWorkflowDTO(res.getTeamId(),res.getTeamName(),res.getDomain(),roleActivityList);

        } catch (Exception e) {
            logger.info("Error In getTeamActivities ::" + e.getMessage());
        }
        return responseObj;

    }


    public List<WorkFLowStatusList> getActivityUserStausbyUid(long workflowId, String gUid)
    {
        return activityTransactionRepo.getActivityUserStausbyUid(workflowId, gUid);
    }

    private Object[] findStepNumber(long workFlowId,String runId)
    {
        return activityTransactionRepo.getStepNumber(workFlowId,runId);
    }
    public List<UserWorkFlowDTO> getUserWorkflowActivities(UserRequestDTO userReq) {
        List<WorkFlowDTO> workFlowDTOList =null;
        List<UserWorkFlowDTO> userWorkFlowDTOList = null;
        logger.info("getUserWorkflowActivities Service Call Started");
        try{
            workFlowDTOList = activityTransactionRepo.getUserWorkflowActivities(userReq.getUserId(),userReq.getStartDate(),userReq.getEndDate());
            userWorkFlowDTOList = responseMapper.userWorkFlowMapper(workFlowDTOList);
        } catch (Exception e) {
            logger.error("Error in getUserWorkflowActivities "+ e.getMessage());
        }
        return userWorkFlowDTOList;
    }

    public Map<String, List<ActivityMasterResponseDTO>> getWorkFlowDefinition(int workflowId){
        List<ActivityMasterResponseDTO> response = null;
        Map<String, List<ActivityMasterResponseDTO>> resMap = new HashMap<>();
        try{
            logger.info("Inside WorkFlowService.getWorkFlowDefination ");
            response = activityMasterRepo.findByWorkflowIdOrderByStepNoAsc(workflowId).stream().map(activityRes->
                    new ActivityMasterResponseDTO(activityRes.getActivityId(),
                            activityRes.getStepNo(),
                            activityRes.getActivityName(),
                            activityRes.getActivityType(),
                            activityRes.getDecisionKey(),
                            activityRes.getRightChild(),
                            activityRes.getLeftChild(),
                            activityRes.getParentId())).toList();

            String workflowName = workFlowRepo.findById(workflowId).get().getWorkflowName();

            resMap.put(workflowName,response);
            logger.debug("WorkFlowService.getWorkFlowDefination responce:: "+response.toString());
        } catch (Exception e) {
            logger.error("WorkFlowService.getWorkFlowDefination :: " + e.getMessage());

        }
        return  resMap;
    }

    public String startWorkflow(String workflowName, String userInputText,String runningInstanceId,String userEmail) {
        WorkFlowMaster wf = workFlowRepo.findByWorkflowName(workflowName)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));

        String instanceId;
        if(runningInstanceId == null || runningInstanceId.isBlank()) {
            instanceId = UUID.randomUUID().toString();
        }else{
            instanceId = runningInstanceId;
        }
        Map<String, Object> userInputs = new HashMap<>();
        if (userInputText != null && !userInputText.isBlank()) {
            userInputs.put("inputText", userInputText);
        }
        String activeUser = extractEmailFromUserInputs(userInputs) ;
        if(activeUser == null || activeUser.isBlank()) {
            activeUser = userEmail;
        }

        WorkflowTransaction wfTxn = new WorkflowTransaction();
        wfTxn.setRunningInstanceId(instanceId);
        wfTxn.setWorkflowId(wf.getWorkflowId());
        wfTxn.setStartDate(LocalDateTime.now().toString());
        wfTxn.setEndDate(null);
        wfTxn.setWorkflowStatus(Constants.WORKFLOW_RUNNING_STATUS);
        workflowTransactionRepo.save(wfTxn);

        scheduledWorkflowRepository.findByRunningInstanceId(instanceId)
                .ifPresent(scheduledWorkflow -> {
                    scheduledWorkflow.setStatus(Constants.WORKFLOW_TRIGGERED_STATUS);
                    scheduledWorkflowRepository.save(scheduledWorkflow);
                });


        ActivityMaster currentStep = activityMasterRepo.findFirstByWorkflowIdOrderByStepNoAsc(wf.getWorkflowId())
                .orElseThrow(() -> new RuntimeException("No activities defined for workflow"));

        while (currentStep != null) {
            RoleAssignment ra = roleAssignmentRepo.findByActivityIdAndWorkflowId(currentStep.getActivityId(), currentStep.getWorkflowId())
                    .orElseThrow(() -> new RuntimeException("Role assignment not found"));
            if (ra.getAgentId() != null) {
                String agentOutput = runAgent(currentStep, instanceId, ra.getAgentId(), userInputs);
                userInputs = null;
                String decisionKey = currentStep.getDecisionKey();
                Integer nextStepNo = null;
                String endMessage = null;
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(agentOutput);
                    JsonNode pipelineNode = null;
                    if(agentOutput.contains("pipeline")){
                        pipelineNode = root.path("pipeline");
                    }else{
                        pipelineNode = root.path("decision_result");
                    }
                    if (decisionKey != null && !decisionKey.isBlank()) {
                        String decisionValue = pipelineNode.has("Decision") ? pipelineNode.get("Decision").asText() : null;
                        if (decisionValue != null && decisionValue.equalsIgnoreCase(decisionKey)) {
                            nextStepNo = currentStep.getRightChild();
                        } else {
                            nextStepNo = currentStep.getLeftChild();
                        }
                    } else {
                        nextStepNo = currentStep.getRightChild();
                    }
                    if (nextStepNo == null) {
                        endMessage = pipelineNode.has("message") ? pipelineNode.get("message").asText() : "Workflow completed.";
                        System.out.println("Workflow end message: " + endMessage);

                        WorkflowTransaction txn = workflowTransactionRepo.findById(instanceId).orElseThrow();
                        txn.setWorkflowStatus(Constants.WORKFLOW_COMPLETED_STATUS);
                        txn.setEndDate(LocalDateTime.now().toString());
                        workflowTransactionRepo.save(txn);
                        break;
                    }
                    currentStep = activityMasterRepo.findByWorkflowIdAndStepNo(wf.getWorkflowId(), nextStepNo)
                            .orElse(null);

                } catch (Exception e) {
                    throw new RuntimeException("Failed to process agent output for step: " + currentStep.getStepNo(), e);
                }
            } else {
                createManualTransaction(currentStep, instanceId,activeUser);
                break;
            }
        }
        return instanceId;
    }

    public String progressWorkflow(ProgressWorkflowRequest request) {
        String instanceId = request.getInstanceId();
        ActivityTransaction current = activityTransactionRepo
                .findFirstByRunningInstanceIdAndActivityStatusOrderByExecutionIdDesc(
                        instanceId, Constants.ACTIVITY_TRANSACTION_ASSIGNED)
                .orElseThrow(() -> new RuntimeException("No active manual step found"));

        current.setActivityStatus(Constants.ACTIVITY_COMPLETED_STATUS);
        current.setPerformedBy(request.getUser().getUserEmail());
        current.setComment(request.getComment());
        current.setEndTime(LocalDateTime.now().toString());
        activityTransactionRepo.save(current);

        ActivityMaster currentStep = activityMasterRepo.findByWorkflowIdAndStepNo(
                current.getWorkflowId(), current.getStepNo()).orElseThrow();

        Integer nextStepNo = currentStep.getRightChild();
        while (nextStepNo != null) {
            ActivityMaster nextStep = activityMasterRepo.findByWorkflowIdAndStepNo(
                    current.getWorkflowId(),nextStepNo).orElse(null);
            if (nextStep == null) break;
            RoleAssignment ra = roleAssignmentRepo.findByActivityIdAndWorkflowId(nextStep.getActivityId(), nextStep.getWorkflowId())
                    .orElseThrow(() -> new RuntimeException("Role assignment not found"));

            Integer lastExecId = activityTransactionRepo
                    .findMaxExecutionIdByRunningInstanceId(instanceId);

            int newExecId = lastExecId + 1;
            Map<String, Object> userInputs = new HashMap<>();
            userInputs.put("userInputs", request.getComment());

            if (ra.getAgentId() != null) {
                String agentResponse = runAgent(nextStep, instanceId, ra.getAgentId(),userInputs);
                if (agentResponse != null) {
                    try {
                        JsonNode root = new ObjectMapper().readTree(agentResponse);
                        String decisionValue = null;
                        if(agentResponse.contains("pipeline")) {
                            JsonNode pipelineNode = root.path("pipeline");
                            decisionValue = pipelineNode.has("Decision")
                                    ? pipelineNode.get("Decision").asText() : null;
                        }else{
                            JsonNode pipelineNode = root.path("decision_result");
                            decisionValue = pipelineNode.has("Decision")
                                    ? pipelineNode.get("Decision").asText() : null;
                        }
                        if (nextStep.getDecisionKey() != null) {
                            if (nextStep.getDecisionKey().equalsIgnoreCase(decisionValue)) {
                                nextStepNo = nextStep.getRightChild();
                            } else {
                                nextStepNo = nextStep.getLeftChild();
                            }
                        } else {
                            nextStepNo = nextStep.getRightChild();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse agent decisionKey", e);
                    }
                } else {
                    nextStepNo = null;
                }
            } else {
                createManualTransaction(nextStep, instanceId, current.getPerformedBy());
                return "Waiting for manual step.";
            }
        }
        WorkflowTransaction txn = workflowTransactionRepo.findById(instanceId).orElseThrow();
        txn.setWorkflowStatus(Constants.WORKFLOW_COMPLETED_STATUS);
        txn.setEndDate(LocalDateTime.now().toString());
        workflowTransactionRepo.save(txn);
        return "Workflow completed.";
    }

    private String runAgent(ActivityMaster step, String instanceId, Integer agentId, Map<String, Object> userInputs) {
        Agent agent = agentRepo.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        Integer maxExecutionId = activityTransactionRepo.findMaxExecutionIdByRunningInstanceId(instanceId);
        int newExecutionId = (maxExecutionId == null) ? 1 : maxExecutionId + 1;

        ActivityTransaction txn = new ActivityTransaction();
        txn.setWorkflowId(step.getWorkflowId());
        txn.setRunningInstanceId(instanceId);
        txn.setStepNo(step.getStepNo());
        txn.setExecutionId(newExecutionId);
        txn.setActivityType(step.getActivityType());
        txn.setAssignedTo(agent.getAgentName());
        txn.setStartTime(LocalDateTime.now().toString());
        txn.setActivityStatus(Constants.ACTIVITY_START_STATUS);
        activityTransactionRepo.save(txn);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> payload = new HashMap<>();
            ObjectMapper mapper = new ObjectMapper();
            Integer pipelineId = 0;

            if (agent.getHeader() != null && !agent.getHeader().equalsIgnoreCase("null")) {
                Map<String, Object> headerMap = mapper.readValue(agent.getHeader(), Map.class);

                String token = headerMap.get("token").toString();
                String user = headerMap.get("user").toString();
                pipelineId = Integer.parseInt(headerMap.get("pipeLineId").toString());

                headers.set("access-key", token);
                payload.put("pipeLineId", pipelineId);
                payload.put("executionId", instanceId);
                payload.put("user", user);
            }
            payload.put("userInputs", userInputs != null ? userInputs : new HashMap<>());

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
            logger.info("Agent Call Started for pipelineId: " + pipelineId + " at " + Instant.now());

            ResponseEntity<String> response = restTemplate.exchange(
                    agent.getAgentUrl(), HttpMethod.POST, request, String.class);

            logger.info("Raw agent response: " + response.getBody());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            String output = null;
            if(response.getBody().contains("pipeline")) {
                output = root.path("pipeline").path("output").asText(null);
            }
            else{
                output = root.path("decision_result").path("Reason").asText(null);
            }
            txn.setActivity_output(output);
            txn.setEndTime(LocalDateTime.now().toString());
            txn.setActivityStatus(Constants.ACTIVITY_COMPLETED_STATUS);
            activityTransactionRepo.save(txn);
            return response.getBody();
        } catch (ResourceAccessException ex) {
            logger.error("Agent timeout (Socket) at step {}: {}", step.getStepNo(), ex.getMessage());
            txn.setActivity_output("Socket exception occurred");
        } catch (Exception ex) {
            logger.error("Agent call failed at step {}: {}", step.getStepNo(), ex.getMessage());
            throw new RuntimeException("Agent call failed", ex);
        }
        txn.setEndTime(LocalDateTime.now().toString());
        txn.setActivityStatus(Constants.ACTIVITY_FAILED_STATUS);
        activityTransactionRepo.save(txn);
        return null;
    }

    private void createManualTransaction(ActivityMaster step, String instanceId,String activeUser) {
        ActivityTransaction txn = new ActivityTransaction();
        txn.setWorkflowId(step.getWorkflowId());
        txn.setRunningInstanceId(instanceId);
        txn.setStepNo(step.getStepNo());
        txn.setActivityType(Constants.ACTIVITY_TRANSACTION_TYPE_MANUAL);
        txn.setAssignedTo(step.getActivityName());
        txn.setStartTime(LocalDateTime.now().toString());
        txn.setActivityStatus(Constants.ACTIVITY_TRANSACTION_ASSIGNED);

        Integer currentMaxExecutionId = activityTransactionRepo.findMaxExecutionIdByRunningInstanceId(instanceId);
        txn.setExecutionId((currentMaxExecutionId != null ? currentMaxExecutionId : 0) + 1);

        Optional<ActivityTransaction> previousTxnOpt = activityTransactionRepo
                .findTopByRunningInstanceIdAndExecutionIdLessThanOrderByExecutionIdDesc(instanceId, txn.getExecutionId());

        if (previousTxnOpt.isPresent()) {
            String activityOutput = previousTxnOpt.get().getActivity_output();
            int previousStep = previousTxnOpt.get().getStepNo();
            String extractedUrl = null;

            try {
                if (activityOutput != null && activityOutput.contains("Socket exception occurred")) {
                    extractedUrl = getUrl(previousStep);
                    txn.setUrl(extractedUrl);
                    activityTransactionRepo.save(txn);
                    return;
                }

                if (activityOutput != null) {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(activityOutput);
                        if (root.has("outputUrl")) {
                            extractedUrl = root.get("outputUrl").asText();
                        }
                    } catch (Exception e) {
                        extractedUrl = getUrl(previousStep);
                    }
                    if (extractedUrl == null || extractedUrl.isBlank()) {
                        try {
                            Pattern pattern = Pattern.compile("https?://[^\"]+");
                            Matcher matcher = pattern.matcher(activityOutput);
                            if (matcher.find()) {
                                extractedUrl = matcher.group(0);
                            }
                        } catch (Exception e) {
                            extractedUrl = getUrl(previousStep);
                        }
                    }
                }
                txn.setUrl(extractedUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to extract URL from previous step output", e);
            }
        }
        User user = userRepo.findByUserEmail(activeUser)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + activeUser));
        Integer activeUserId = user.getUserId();

        RoleAssignment roleAssignment = roleAssignmentRepo
                .findByActivityIdAndWorkflowId(step.getActivityId(), step.getWorkflowId())
                .orElseThrow(() -> new RuntimeException("Role assignment not found"));

        if (roleAssignment.getUserId() != null
                && !roleAssignment.getUserId().equals(activeUserId)
                && roleAssignment.getAgentId() == null) {
            roleAssignment.setUserId(activeUserId);
            roleAssignmentRepo.save(roleAssignment);
        }

        activityTransactionRepo.save(txn);
    }

    private String getUrl(int stepNo){
        String extractedUrl= null;
        switch (stepNo) {
            case 1:
                extractedUrl = Constants.JIRA_URL;
                break;
            case 3:
                extractedUrl = Constants.TEST_RAIL_URL;
                break;
            case 5:
                extractedUrl = Constants.GITHUB_URL;
                break;
            default:
                extractedUrl = "";
        }
        return extractedUrl;
    }

    public Page<WorkFlowMasterDTO>  getUserWorkflow(WorkflowRequestDTO requestObj){
        Pageable pageable = PageRequest.of(requestObj.getPage(),requestObj.getPageSize());
        Page<WorkFlowMasterDTO> pageObj = workFlowRepo.getUserWorkFlow(requestObj.getUserId(),
                requestObj.getStartDate(),requestObj.getEndDate(),pageable);
        if(pageObj.getTotalElements()==0){
            return null;
        }
        pageObj.forEach(obj -> {String status = obj.getStatus()!=null?obj.getStatus():null;
            int stepNum = Integer.parseInt(String.valueOf(obj.getStepNo()));
            int workFlowId = obj.getWorkflowId();
            String runId = obj.getRunningInstanceId()!=null?obj.getRunningInstanceId():"";
            if(status!=null && (status.equalsIgnoreCase("Running")|| !runId.isEmpty()))
            {
                Object[] resSet = findStepNumber(workFlowId,runId);
                try{
                    stepNum = resSet[0]!=null?Integer.parseInt(resSet[0].toString()):null;
                }catch(Exception e){
                    stepNum =1;
                }
            }
            obj.setStepNo(stepNum);});
        return pageObj;
    }

    public String rejectWorkflow(String instanceId) {
        List<ActivityTransaction> transactions = activityTransactionRepo.findByRunningInstanceId(instanceId);
        if (transactions == null || transactions.isEmpty()) {
            return null;
        }
        transactions.sort(Comparator.comparing(ActivityTransaction::getExecutionId).reversed());
        ActivityTransaction prevManualStep = null;
        Integer prevManualExecutionId = null;
        boolean foundCurrent = false;
        for (ActivityTransaction txn : transactions) {
            if (!foundCurrent) {
                foundCurrent = true;
                continue;
            }
            if ("manual".equalsIgnoreCase(txn.getActivityType())) {
                prevManualStep = txn;
                prevManualExecutionId = txn.getExecutionId();
                break;
            }
        }
        if (prevManualStep != null) {
            prevManualStep.setActivityStatus(Constants.ACTIVITY_TRANSACTION_ASSIGNED);
            prevManualStep.setEndTime(null);
            activityTransactionRepo.save(prevManualStep);
            for (ActivityTransaction txn : transactions) {
                if (txn.getExecutionId() > prevManualExecutionId) {
                    activityTransactionRepo.delete(txn);
                }
            }
        } else {
            for (ActivityTransaction txn : transactions) {
                activityTransactionRepo.delete(txn);
            }
            Optional<WorkflowTransaction> workflowTxnOpt = workflowTransactionRepo.findById(instanceId);
            if (workflowTxnOpt.isPresent()) {
                WorkflowTransaction workflowTxn = workflowTxnOpt.get();
                workflowTxn.setWorkflowStatus(Constants.WORKFLOW_REJECTED_STATUS);
                workflowTxn.setEndDate(LocalDateTime.now().toString());
                workflowTransactionRepo.save(workflowTxn);
            }
        }
        return instanceId;
    }

    private String extractEmailFromUserInputs(Map<String, Object> userInputs) {
        if (userInputs == null || userInputs.isEmpty()) return null;

        Pattern emailPattern = Pattern.compile("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+");

        for (Object value : userInputs.values()) {
            if (value instanceof String) {
                Matcher matcher = emailPattern.matcher((String) value);
                if (matcher.find()) {
                    return matcher.group();
                }
            }
        }
        return null;
    }


    public void scheduleWorkflow(ScheduleWorkflowDto dto) throws SchedulerException {
        long currentEpoch = System.currentTimeMillis();
        if (dto.getRunAt() <= currentEpoch) {
            throw new IllegalArgumentException("Scheduled time is in the past. Please select a future UTC time.");
        }
        String instanceId = UUID.randomUUID().toString();

        WorkFlowMaster wf = workFlowMasterRepo.findByWorkflowName(dto.getWorkflowName())
                .orElseThrow(() -> new RuntimeException("Workflow not found"));

        WorkflowTransaction wfTxn = new WorkflowTransaction();
        wfTxn.setRunningInstanceId(instanceId);
        wfTxn.setWorkflowId(wf.getWorkflowId());
        long epoch = dto.getRunAt();
        LocalDateTime startDate = Instant.ofEpochMilli(epoch).atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
        wfTxn.setStartDate(startDate.toString());
        wfTxn.setEndDate(null);
        wfTxn.setWorkflowStatus(Constants.WORKFLOW_SCHEDULED_STATUS);
        workflowTransactionRepo.save(wfTxn);

        ScheduleWorkflow scheduledWorkflow = new ScheduleWorkflow();
        scheduledWorkflow.setWorkflowName(dto.getWorkflowName());
        scheduledWorkflow.setRunAt(dto.getRunAt());
        scheduledWorkflow.setCreatedAt(Instant.now().toEpochMilli());
        scheduledWorkflow.setScheduleBy(dto.getScheduleBy());
        scheduledWorkflow.setUserInputs(dto.getUserInputs());
        scheduledWorkflow.setStatus(Constants.WORKFLOW_SCHEDULED_STATUS);
        scheduledWorkflow.setRunningInstanceId(instanceId);


        scheduledWorkflowRepository.save(scheduledWorkflow);
        try {
            JobDataMap jobData = new JobDataMap();
            jobData.put("workflowName", dto.getWorkflowName());
            jobData.put("userInputs", dto.getUserInputs());
            jobData.put("runningInstanceId", instanceId);
            jobData.put("userEmail", dto.getScheduleBy());
            jobData.put("scheduleId", scheduledWorkflow.getId());

            JobDetail jobDetail = JobBuilder.newJob(JobScheduler.class)
                    .withIdentity(UUID.randomUUID().toString(), "workflow-jobs")
                    .setJobData(jobData)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(UUID.randomUUID().toString(), "workflow-triggers")
                    .startAt(new Date(dto.getRunAt()))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception ex) {
            scheduledWorkflow.setStatus(Constants.WORKFLOW_SCHEDULED_FAILED_STATUS);
            scheduledWorkflow.setErrorMessage(ex.getMessage());
            scheduledWorkflowRepository.save(scheduledWorkflow);
            throw new RuntimeException("Failed scheduled workflow: " + ex);
        }

    }

    public boolean cancelScheduledWorkflow(String instanceId) {
        List<ScheduleWorkflow> scheduledList = scheduledWorkflowRepository.findByRunningInstanceIdAndStatus(instanceId, Constants.WORKFLOW_SCHEDULED_STATUS);
        if (scheduledList == null || scheduledList.isEmpty()) {
            return false;
        }
        try {
            scheduledList.forEach(sw -> scheduledWorkflowRepository.deleteById(sw.getId()));
            workflowTransactionRepo.deleteByRunningInstanceId(instanceId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
