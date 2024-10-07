package com.user_management.user_management_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/users")
public class UserController {

    private final RestTemplate restTemplate;

    public UserController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/hello-from-trainee")
    public String helloFromTrainee() {
        try {
            // Call the Trainee Management Service via the API Gateway
            String response = restTemplate.getForObject("http://localhost:8093/trainees/hello", String.class);
            return "Response from Trainee Management Service: " + response;
        } catch (Exception e) {
            return "Error calling Trainee Management Service: " + e.getMessage();
        }
    }
}
