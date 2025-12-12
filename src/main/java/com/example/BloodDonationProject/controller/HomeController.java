package com.example.BloodDonationProject.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BloodDonationProject.dto.ApiResponse;

@RestController
public class HomeController {

    @GetMapping("/")
    public ApiResponse<Map<String, Object>> home() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("version", "1.0");
        metadata.put("status", "running");

        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("donors", "/api/donors");
        endpoints.put("bloodRequests", "/api/requests");
        endpoints.put("donationInterests", "/api/interests");

        metadata.put("endpoints", endpoints);
        return ApiResponse.success("Blood Donation API", metadata);
    }
}
