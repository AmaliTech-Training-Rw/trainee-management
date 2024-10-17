package com.assessment_management.assessment_management_service.service;

import com.assessment_management.assessment_management_service.exception.AssessmentNotFoundException;
import com.assessment_management.assessment_management_service.exception.BadRequestException;
import com.assessment_management.assessment_management_service.model.Assessment;
import com.assessment_management.assessment_management_service.model.AssessmentStatus;
import com.assessment_management.assessment_management_service.model.Grade;
import com.assessment_management.assessment_management_service.repository.AssessmentRepository;
import com.assessment_management.assessment_management_service.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    public ResponseEntity<Grade> createGrade(String assessmentId, Grade grade) {
        // Validate the grade object
        validateGrade(grade);

        // Find the assessment by ID
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new AssessmentNotFoundException("Assessment with ID " + assessmentId + " not found."));

        // Associate the grade with the assessment
        grade.setAssessment(assessment);
        grade.setDate(LocalDateTime.now()); // Set the current date

        // Change status of assessment to graded
        assessment.setStatus(AssessmentStatus.GRADED);
        assessmentRepository.save(assessment); // Save updated assessment

        Grade savedGrade = gradeRepository.save(grade); // Save the grade
        return new ResponseEntity<>(savedGrade, HttpStatus.CREATED); // Return created status
    }

    private void validateGrade(Grade grade) {
        // Check for null grade
        if (grade == null) {
            throw new BadRequestException("Grade cannot be null.");
        }

        // Validate required fields
        if (grade.getScore() == null) {
            throw new BadRequestException("Score is required.");
        }
        if (grade.getGrade() == null || grade.getGrade().trim().isEmpty()) {
            throw new BadRequestException("Grade value is required.");
        }
        if (grade.getTraineeId() == null || grade.getTraineeId().trim().isEmpty()) {
            throw new BadRequestException("Trainee ID is required.");
        }
    }


}
