package com.trainee_management.trainee_management_service.cohort.dto;



import java.util.List;

public class AssignTraineesRequest {
    private List<String> trainees;

    // Getter and Setter
    public List<String> getTrainees() {
        return trainees;
    }

    public void setTrainees(List<String> trainees) {
        this.trainees = trainees;
    }
}
