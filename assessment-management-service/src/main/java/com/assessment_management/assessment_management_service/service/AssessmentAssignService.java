package com.assessment_management.assessment_management_service.service;

import com.assessment_management.assessment_management_service.exception.AssessmentNotFoundException;
import com.assessment_management.assessment_management_service.exception.BadRequestException;
import com.assessment_management.assessment_management_service.model.Assessment;
import com.assessment_management.assessment_management_service.model.AssessmentAssignment;
import com.assessment_management.assessment_management_service.repository.AssessmentRepository;
import com.assessment_management.assessment_management_service.repository.AssessmentAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AssessmentAssignService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private AssessmentAssignmentRepository assignmentRepository;

    public ResponseEntity<?> assignAssessment(String assessmentId, List<String> traineeIds) {
        // Check for null or empty traineeIds
        if (traineeIds == null || traineeIds.isEmpty()) {
            throw new BadRequestException("Trainee IDs cannot be null or empty.");
        }

        // Validate assessment existence
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new AssessmentNotFoundException("Assessment with ID " + assessmentId + " not found"));

        // Fetch the existing assignment for the assessmentId
        List<AssessmentAssignment> assignments = assignmentRepository.findByAssessmentId(assessmentId);

        // Check if there is an existing assignment
        AssessmentAssignment existingAssignment;
        if (!assignments.isEmpty()) {
            // Get the existing assignment (assuming you want to update the first one in the list)
            existingAssignment = assignments.get(0);

            // Merge the existing traineeIds with the new ones
            List<String> existingTraineeIds = existingAssignment.getTraineeIds();
            if (existingTraineeIds != null) {
                // Avoid adding duplicates
                for (String traineeId : traineeIds) {
                    if (!existingTraineeIds.contains(traineeId)) {
                        existingTraineeIds.add(traineeId);
                    }
                }
            } else {
                existingAssignment.setTraineeIds(traineeIds); // If no trainees exist, set the new list
            }
        } else {
            // Create a new assignment if none exists
            existingAssignment = new AssessmentAssignment();
            existingAssignment.setAssessmentId(assessment.getId()); // Set the assessmentId from the fetched assessment
            existingAssignment.setTraineeIds(traineeIds);
            existingAssignment.setAssignedDate(LocalDateTime.now().toString()); // Store the assigned date as a String
        }

        // Save the updated or new assignment
        AssessmentAssignment savedAssignment = assignmentRepository.save(existingAssignment);
        return new ResponseEntity<>(savedAssignment, HttpStatus.CREATED); // Return created status
    }



    // Add cohorts to an existing assessment assignment
    public ResponseEntity<AssessmentAssignment> addCohortsToAssignment(String assignmentId, List<String> cohortsIds) {
        // Validate cohortsIds
        if (cohortsIds == null || cohortsIds.isEmpty()) {
            throw new BadRequestException("Cohorts IDs cannot be null or empty.");
        }

        // Fetch the existing assignment
        System.out.println("Assignment with ID " + assignmentId);
        List<AssessmentAssignment> assignments = assignmentRepository.findByAssessmentId(assignmentId);

        // Check if the assignment exists
        if (assignments.isEmpty()) {
            throw new AssessmentNotFoundException("Assignment with ID " + assignmentId + " not found");
        }

        // Assuming you want to work with the first assignment in the list
        AssessmentAssignment existingAssignment = assignments.get(0);

        // Add the new cohorts IDs to the existing list
        List<String> existingCohortsIds = existingAssignment.getCohortsIds();
        if (existingCohortsIds != null) {
            existingCohortsIds.addAll(cohortsIds);
        } else {
            existingAssignment.setCohortsIds(cohortsIds);
        }

        // Save the updated assignment
        AssessmentAssignment updatedAssignment = assignmentRepository.save(existingAssignment);

        return new ResponseEntity<>(updatedAssignment, HttpStatus.OK);
    }

}
