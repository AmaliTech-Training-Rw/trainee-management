package com.assessment_management.assessment_management_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "trainee_progress")
public class TraineeProgress {

    @Id
    private String id;  // MongoDB uses String type for IDs by default

    private String traineeName;

    private CurrentPhase currentPhase;

    private String progressIndicator;  // Phase1, Phase2, Phase3

    private LocalDate completionDate;

    // Constructors
    public TraineeProgress() {}

    public TraineeProgress(String traineeName,String traineeEmail, CurrentPhase currentPhase, String progressIndicator, LocalDate completionDate) {
        this.traineeName = traineeName;
        this.currentPhase = currentPhase;
        this.progressIndicator = progressIndicator;
        this.completionDate = completionDate;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTraineeName() {
        return traineeName;
    }

    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }


    public CurrentPhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(CurrentPhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public String getProgressIndicator() {
        return progressIndicator;
    }

    public void setProgressIndicator(String progressIndicator) {
        this.progressIndicator = progressIndicator;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }
}
