package com.example.scheduler_service.controller;

import com.example.scheduler_service.model.ScheduledTaskRegistrationRequest;
import com.example.scheduler_service.services.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SchedulerController {
    private final SchedulerService schedulerService;

    @Autowired
    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerScheduledTask(@RequestBody ScheduledTaskRegistrationRequest registrationRequest) {
        try {
            schedulerService.scheduleTask(registrationRequest.getId(), registrationRequest.getTriggerTime(), registrationRequest.getPayload());
            return ResponseEntity.ok("Task registered successfully with ID: " + registrationRequest.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to register task: " + e.getMessage());
        }
    }
}
