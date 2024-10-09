package com.assessment_management.assessment_management_service.service;

import com.assessment_management.assessment_management_service.exception.AssessmentNotFoundException;
import com.assessment_management.assessment_management_service.exception.BadRequestException;
import com.assessment_management.assessment_management_service.model.Assessment;
import com.assessment_management.assessment_management_service.model.AssessmentStatus;
import com.assessment_management.assessment_management_service.model.Question;
import com.assessment_management.assessment_management_service.repository.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    // Create a new assessment, including the embedded questions
    public ResponseEntity<?> createAssessment(Assessment assessment) {
        // Check for null assessment
        if (assessment == null) {
            throw new BadRequestException("Assessment cannot be null.");
        }

        // Validate required fields
        if (assessment.getTitle() == null || assessment.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Title is required.");
        }
        if (assessment.getType() == null) {
            throw new BadRequestException("Type is required.");
        }
        if (assessment.getQuestions() == null || assessment.getQuestions().isEmpty()) {
            throw new BadRequestException("At least one question is required.");
        }

        assessment.setStatus(AssessmentStatus.EMPTY); // Set status to an empty string
        assessment.setCreated(LocalDateTime.now());

        // Ensure each question has a unique ID
        assignIdsToQuestions(assessment.getQuestions());

        Assessment savedAssessment = assessmentRepository.save(assessment);
        return new ResponseEntity<>(savedAssessment, HttpStatus.CREATED); // Return created status
    }

    // Get an assessment by its ID
    public ResponseEntity<Assessment> getAssessment(String id) {
        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new AssessmentNotFoundException("Assessment with ID " + id + " not found"));
        return new ResponseEntity<>(assessment, HttpStatus.OK); // Return OK status
    }

    // Get all assessments
    public ResponseEntity<List<Assessment>> getAllAssessments() {
        List<Assessment> assessments = assessmentRepository.findAll();
        return new ResponseEntity<>(assessments, HttpStatus.OK); // Return OK status
    }

    // Update an existing assessment and its questions
    public ResponseEntity<?> updateAssessment(String id, Assessment updatedAssessment) {
        // Check for null updatedAssessment
        if (updatedAssessment == null) {
            throw new AssessmentNotFoundException("Updated assessment cannot be null.");
        }

        // Validate required fields
        if (updatedAssessment.getTitle() == null || updatedAssessment.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Title is required.");
        }
        if (updatedAssessment.getType() == null) {
            throw new BadRequestException("Type is required.");
        }
        if (updatedAssessment.getQuestions() == null || updatedAssessment.getQuestions().isEmpty()) {
            throw new BadRequestException("At least one question is required.");
        }

        Assessment existingAssessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new AssessmentNotFoundException("Assessment with ID " + id + " not found"));

        existingAssessment.setTitle(updatedAssessment.getTitle());
        existingAssessment.setType(updatedAssessment.getType());
        existingAssessment.setQuestions(updatedAssessment.getQuestions());  // Update questions
        existingAssessment.setStatus(updatedAssessment.getStatus());

        // Ensure each question has a unique ID
        assignIdsToQuestions(existingAssessment.getQuestions());

        Assessment savedAssessment = assessmentRepository.save(existingAssessment);
        return new ResponseEntity<>(savedAssessment, HttpStatus.OK); // Return OK status
    }

    // Delete an assessment by ID
    public ResponseEntity<Void> deleteAssessment(String id) {
        if (!assessmentRepository.existsById(id)) {
            throw new AssessmentNotFoundException("Assessment with ID " + id + " not found"); // Throw exception if ID is not valid
        }
        assessmentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return no content status
    }

    // Helper method to assign IDs to each question
    private void assignIdsToQuestions(List<Question> questions) {
        if (questions != null) {
            for (Question question : questions) {
                if (question.getId() == null) {
                    question.setId(UUID.randomUUID().toString());  // Generate unique ID for each question
                }
            }
        }
    }
}
