package com.assessment_management.assessment_management_service.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "assessments")
public class Assessment {

    @Id
    private String id;
    private String title;
    @Enumerated(EnumType.STRING)
    private AssessmentType type;
    @Enumerated(EnumType.STRING)
    private AssessmentStatus status;
    private String trainerId;
    private LocalDateTime created;
    private List<Question> questions;
    private String image;          // URL or path to the assessment's image
    private String description;    // Description of the assessment
    private String focusArea;      // Focus area of the assessment (e.g., specific subject or topic)
    private int duration;          // Duration in minutes for the quiz


}
