package com.example.scheduler_service.scheduler;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class CallbackJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(CallbackJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String taskId = jobDataMap.getString("taskId");
        String callbackUrl = jobDataMap.getString("callbackUrl");
        System.out.println(callbackUrl); // Retrieve from JobDataMap
        String payload = jobDataMap.getString("payload");

        logger.info("Executing callback for task ID: {}", taskId);
        logger.info("Callback URL: {}", callbackUrl);
        logger.info("Payload: {}", payload);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> callbackData = new HashMap<>();
        callbackData.put("id", taskId);
        if (payload != null) {
            callbackData.put("payload", payload);
        }
        callbackData.put("triggerTime", context.getFireTime());
        callbackData.put("scheduledTime", context.getScheduledFireTime());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(callbackData, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(callbackUrl, HttpMethod.POST, request, String.class);
            logger.info("Callback successful for task ID: {}, Response: {}", taskId, response.getStatusCode());
        } catch (Exception e) {
            logger.error("Error during callback for task ID: {}, Error: {}", taskId, e.getMessage());
        }
    }
}