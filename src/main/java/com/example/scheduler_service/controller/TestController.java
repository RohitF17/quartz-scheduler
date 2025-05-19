package com.example.scheduler_service.controller;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/")
    public String getMethodName() {
        return new String("hello world");
    }
    

    @PostMapping("/")
    public ResponseEntity<Optional<Object>> TestingPost (@RequestBody Optional<Object> body) {
        System.out.println(body);
        return ResponseEntity.ok(body);
    }
    
    
}
