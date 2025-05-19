package com.example.scheduler_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${callback.base.url}")
    private String callbackUrl;

    public String getCallback(){
        return callbackUrl;
    }
    
}
