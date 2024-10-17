package com.assessment_management.assessment_management_service.model;

import java.util.List;

// Request class to handle cohorts IDs
public class CohortsIdsRequest {
    private List<String> cohortsIds;

    // Getters and Setters
    public List<String> getCohortsIds() {
        return cohortsIds;
    }

    public void setCohortsIds(List<String> cohortsIds) {
        this.cohortsIds = cohortsIds;
    }
}