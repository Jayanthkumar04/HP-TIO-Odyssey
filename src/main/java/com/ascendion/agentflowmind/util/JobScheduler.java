package com.ascendion.agentflowmind.util;

import com.ascendion.agentflowmind.dto.ScheduleWorkflowDto;
import com.ascendion.agentflowmind.repository.ScheduledWorkflowRepository;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class JobScheduler implements Job {

    private final WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();

    @Autowired
    ScheduledWorkflowRepository scheduledWorkflowRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();
        String workflowName = dataMap.getString("workflowName");
        String userInputs = dataMap.getString("userInputs");
        String runningInstanceId = dataMap.getString("runningInstanceId");
        String userEmail = dataMap.getString("userEmail");
        Long scheduleId = dataMap.getLong("scheduleId");

        webClient.post()
                .uri("/workflow/monitoring/start")
                .bodyValue(Map.of(
                        "workflowName", workflowName,
                        "userInputs", userInputs,
                        "runningInstanceId", runningInstanceId,
                        "userEmail", userEmail
                ))
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> {
                    System.out.println("Workflow triggered: " + workflowName);
                })
                .doOnError(error -> {
                    System.err.println("Error triggering workflow: " + error.getMessage());
                    updateFailedStatus(scheduleId, error.getMessage());
                })
                .subscribe();
    }

    private void updateFailedStatus(Long id, String errorMessage) {
        scheduledWorkflowRepository.findById(id).ifPresent(workflow -> {
            workflow.setStatus(Constants.WORKFLOW_SCHEDULED_FAILED_STATUS);
            workflow.setErrorMessage(errorMessage);
            scheduledWorkflowRepository.save(workflow);
        });
    }
}
