package com.carrental.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class DebugController {

    @GetMapping("/api/debug")
    public Map<String, Object> debug() {

        return Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now(),
                "service", "Car Rental System"
        );
    }
}