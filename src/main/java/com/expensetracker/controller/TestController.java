package com.expensetracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/")
    public String start() {
        return "Welcome to the Expense Tracker API!";
    }

    @GetMapping("/api/test")
    public Map<String, String> hello() {
        return Map.of(
                "message", "Hello from Spring Boot!",
                "status", "API is working"
        );
    }

    @PostMapping("/api/test")
    public Map<String, String> postTest(@RequestBody Map<String, Object> payload) {
        return Map.of(
                "message", "Received from POST request: " + payload.getOrDefault("name", "unknown") +" And Age is : " + payload.getOrDefault("age", "unknown"),
                "status", "API is working"
        );
    }
}
