package com.example.scheduler_service.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduledTaskRegistrationRequest {
    private String id;
    private LocalDateTime triggerTime;
    private String payload; // Optional payload
}