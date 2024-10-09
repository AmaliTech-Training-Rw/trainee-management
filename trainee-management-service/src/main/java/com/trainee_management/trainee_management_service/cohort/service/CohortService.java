
package com.trainee_management.trainee_management_service.cohort.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainee_management.trainee_management_service.cohort.model.CohortModel;
import com.trainee_management.trainee_management_service.cohort.repository.CohortRepository;
import com.trainee_management.trainee_management_service.cohort.util.TraineeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CohortService {

    @Autowired
    private CohortRepository cohortRepository;

    // Create a new cohort with date validation
    public ResponseEntity<CohortModel> createCohort(CohortModel cohort) {
        validateDateRange(cohort.getStartDate(), cohort.getEndDate());
        CohortModel savedCohort = cohortRepository.save(cohort);
        return new ResponseEntity<>(savedCohort, HttpStatus.CREATED);
    }

    // Update an existing cohort with date validation
    public ResponseEntity<CohortModel> updateCohort(Long id, CohortModel updatedCohort) {
        validateDateRange(updatedCohort.getStartDate(), updatedCohort.getEndDate());
        CohortModel existingCohort = cohortRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cohort not found"));
        existingCohort.setCohortName(updatedCohort.getCohortName()); // Update cohort name
        existingCohort.setStartDate(updatedCohort.getStartDate());
        existingCohort.setEndDate(updatedCohort.getEndDate());
        existingCohort.setLocation(updatedCohort.getLocation());
        existingCohort.setDescription(updatedCohort.getDescription());
        existingCohort.setSpecializations(updatedCohort.getSpecializations());
        CohortModel savedCohort = cohortRepository.save(existingCohort);
        return new ResponseEntity<>(savedCohort, HttpStatus.OK);
    }

    // Validate the start and end dates for the cohort
    private void validateDateRange(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new NoSuchElementException("Start date cannot be after end date.");
        }
    }

    // Get all cohorts
    public ResponseEntity<List<CohortModel>> getAllCohorts() {
        List<CohortModel> cohorts = cohortRepository.findAll();
        return new ResponseEntity<>(cohorts, HttpStatus.OK);
    }

    // Get a cohort by ID
    public ResponseEntity<CohortModel> getCohortById(Long id) {
        CohortModel cohort = cohortRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cohort not found"));
        return new ResponseEntity<>(cohort, HttpStatus.OK);
    }

    // Delete a cohort by ID
    public ResponseEntity<Void> deleteCohort(Long id) {
        cohortRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Add a trainee in cohort
    public ResponseEntity<CohortModel> assignTraineesToCohort(Long id, List<String> trainees) {
        CohortModel cohort = cohortRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cohort not found"));
        TraineeUtil.addTrainees(cohort.getEnrolledTrainees(), trainees);
        CohortModel savedCohort = cohortRepository.save(cohort);
        return new ResponseEntity<>(savedCohort, HttpStatus.OK);
    }

    // Remove a trainee from cohort
    public ResponseEntity<CohortModel> removeTraineeFromCohort(Long cohortId, String traineeJson) {
        CohortModel cohort = cohortRepository.findById(cohortId)
                .orElseThrow(() -> new NoSuchElementException("Cohort not found"));
        System.out.println("Cohort ID: " + cohortId);
        System.out.println("Enrolled Trainees: " + cohort.getEnrolledTrainees());
        System.out.println("Trainee JSON to remove: '" + traineeJson + "'");

        // Parse the JSON string to extract the trainee name
        ObjectMapper objectMapper = new ObjectMapper();
        String traineeName;
        try {
            JsonNode jsonNode = objectMapper.readTree(traineeJson);
            traineeName = jsonNode.get("traineeName").asText();
        } catch (Exception e) {
            throw new NoSuchElementException("Invalid JSON format", e);
        }

        // Trim the traineeName to remove any leading/trailing spaces
        String trimmedTraineeName = traineeName.trim();
        System.out.println("Trimmed Trainee to remove: '" + trimmedTraineeName + "'");

        if (cohort.getEnrolledTrainees().remove(trimmedTraineeName)) {
            System.out.println("Updated Enrolled Trainees: " + cohort.getEnrolledTrainees());
            CohortModel savedCohort = cohortRepository.save(cohort);
            return new ResponseEntity<>(savedCohort, HttpStatus.OK);
        } else {
            throw new NoSuchElementException("Trainee not found in the cohort");
        }
    }
}
