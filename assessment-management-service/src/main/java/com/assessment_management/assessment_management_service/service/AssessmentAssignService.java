package com.assessment_management.assessment_management_service.service;

import com.assessment_management.assessment_management_service.exception.AssessmentNotFoundException;
import com.assessment_management.assessment_management_service.exception.BadRequestException;
import com.assessment_management.assessment_management_service.model.Assessment;
import com.assessment_management.assessment_management_service.model.AssessmentAssignment;
import com.assessment_management.assessment_management_service.model.Trainee; // Import your Trainee model class
import com.assessment_management.assessment_management_service.repository.AssessmentRepository;
import com.assessment_management.assessment_management_service.repository.AssessmentAssignmentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AssessmentAssignService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private AssessmentAssignmentRepository assignmentRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String TRAINEE_SERVICE_URL = "http://localhost:8093/trainees/all";

    public ResponseEntity<?> assignAssessment(String assessmentId, List<String> traineeIds, HttpServletRequest request) {
        validateId(assessmentId, "Assessment ID");

        String authorizationHeader = request.getHeader("Authorization");

        // Prepare HttpHeaders with Authorization token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        // Fetch trainee IDs from external service if none are provided
        if (traineeIds == null || traineeIds.isEmpty()) {
            traineeIds = fetchAllTrainees(headers);
        }

        validateIds(traineeIds, "Trainee IDs");

        // Validate provided trainee IDs against valid trainees from the external service
        validateTraineeIds(traineeIds, headers);

        // Ensure the assessment exists
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new AssessmentNotFoundException("Assessment with ID " + assessmentId + " not found"));

        // Get the existing assessment assignment
        List<AssessmentAssignment> assignments = assignmentRepository.findByAssessmentId(assessmentId);

        // Check if any assignments exist
        if (!assignments.isEmpty()) {
            AssessmentAssignment assignment = assignments.get(0); // Retrieve the first existing assignment
            List<String> existingTraineeIds = assignment.getTraineeIds();

            // Create a Set to keep track of unique trainee IDs
            Set<String> traineeSet = new HashSet<>(existingTraineeIds); // Start with existing IDs

            // Track whether any duplicates were found
            boolean duplicatesFound = false;

            // Add new trainee IDs while checking for duplicates
            for (String traineeId : traineeIds) {
                if (!traineeSet.add(traineeId)) {
                    // Duplicate trainee ID detected
                    System.out.println("Duplicate trainee ID detected: " + traineeId);
                    duplicatesFound = true; // Set the flag to indicate duplicates were found
                }
            }

            // If duplicates were found, respond accordingly
            if (duplicatesFound) {
                return new ResponseEntity<>("Some trainee IDs were duplicates and were not assigned.", HttpStatus.CONFLICT);
            }

            // Update the assignment with unique trainee IDs
            assignment.setTraineeIds(new ArrayList<>(traineeSet)); // Convert back to List

            // Save the updated assignment and return the response
            AssessmentAssignment updatedAssignment = assignmentRepository.save(assignment);
            return new ResponseEntity<>(updatedAssignment, HttpStatus.OK);
        } else {
            // Create a new assignment if no existing assignments are found
            AssessmentAssignment newAssignment = new AssessmentAssignment();
            newAssignment.setAssessmentId(assessmentId);
            newAssignment.setTraineeIds(traineeIds); // Assign trainee IDs

            // Save the new assignment
            AssessmentAssignment savedAssignment = assignmentRepository.save(newAssignment);
            return new ResponseEntity<>(savedAssignment, HttpStatus.CREATED);
        }
    }

    public ResponseEntity<AssessmentAssignment> addCohortsToAssignment(String assignmentId, List<String> cohortIds) {
        if (cohortIds == null || cohortIds.isEmpty()) {
            throw new BadRequestException("Cohort IDs cannot be null or empty.");
        }

        // Fetch existing assignment by ID
        AssessmentAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssessmentNotFoundException("Assignment with ID " + assignmentId + " not found"));

        // Add new cohort IDs to the existing list
        if (assignment.getCohortsIds() != null) {
            assignment.getCohortsIds().addAll(cohortIds);
        } else {
            assignment.setCohortsIds(cohortIds);
        }

        // Save and return the updated assignment
        AssessmentAssignment updatedAssignment = assignmentRepository.save(assignment);
        return new ResponseEntity<>(updatedAssignment, HttpStatus.OK);
    }

    private List<String> fetchAllTrainees(HttpHeaders headers) {
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<List<Trainee>> response;

        try {
            response = restTemplate.exchange(
                    TRAINEE_SERVICE_URL,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Trainee>>() {}
            );
        } catch (Exception e) {
            throw new BadRequestException("Error fetching trainees: " + e.getMessage());
        }

        // Log the response status and body
        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());

        List<Trainee> trainees = response.getBody();
        if (trainees == null || trainees.isEmpty()) {
            throw new BadRequestException("No available trainees found.");
        }

        // Extract trainee IDs
        return trainees.stream()
                .map(Trainee::getId) // Get the ID of each trainee
                .toList(); // Convert to list
    }

    private void validateTraineeIds(List<String> traineeIds, HttpHeaders headers) {
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<List<Trainee>> validTraineesResponse = restTemplate.exchange(
                TRAINEE_SERVICE_URL,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Trainee>>() {}
        );

        List<Trainee> validTrainees = validTraineesResponse.getBody();
        if (validTrainees == null || validTrainees.isEmpty()) {
            throw new BadRequestException("No valid trainees available.");
        }

        List<String> validTraineeIds = validTrainees.stream()
                .map(Trainee::getId)
                .toList();

        for (String traineeId : traineeIds) {
            if (!validTraineeIds.contains(traineeId)) {
                throw new BadRequestException("Invalid trainee ID: " + traineeId);
            }
        }
    }

    private void validateIds(List<String> ids, String type) {
        if (ids == null || ids.isEmpty()) {
            throw new BadRequestException(type + " cannot be null or empty.");
        }
        // Additional validation logic can be added here if needed
    }

    private void validateId(String id, String type) {
        if (id == null || id.trim().isEmpty()) {
            throw new BadRequestException(type + " cannot be null or empty.");
        }
        // Additional validation logic can be added here if needed
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
