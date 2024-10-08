package com.trainee_management.trainee_management_service.cohort.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainees")
public class Test {
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
