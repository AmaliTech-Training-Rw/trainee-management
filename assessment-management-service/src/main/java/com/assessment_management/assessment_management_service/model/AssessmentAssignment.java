package com.assessment_management.assessment_management_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "assessment_assignments")
public class AssessmentAssignment {

    @Id
    private String id;
    private String assessmentId; // Added assessmentId field
    private List<String> traineeIds;
    private String assignedDate;
    private List<String> cohortsIds; // Add cohortsIds
    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssessmentId() {
        return assessmentId; // Getter for assessmentId
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId; // Setter for assessmentId
    }

    public List<String> getTraineeIds() {
        return traineeIds;
    }

    public void setTraineeIds(List<String> traineeIds) {
        this.traineeIds = traineeIds;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
    }

    public List<String> getCohortsIds() {
        return cohortsIds;
    }

    public void setCohortsIds(List<String> cohortsIds) {
        this.cohortsIds = cohortsIds;
    }
    // No other changes needed
}
