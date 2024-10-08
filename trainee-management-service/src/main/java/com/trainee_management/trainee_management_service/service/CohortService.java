
package com.trainee_management.trainee_management_service.service;


import com.trainee_management.trainee_management_service.model.CohortModel;
import com.trainee_management.trainee_management_service.repository.CohortRepository;
import com.trainee_management.trainee_management_service.util.TraineeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.Date;
import java.util.List;

@Service
public class CohortService {

    @Autowired
    private CohortRepository cohortRepository;

    // Create a new cohort with date validation
    public CohortModel createCohort(CohortModel cohort) {
        validateDateRange(cohort.getStartDate(), cohort.getEndDate());
        return cohortRepository.save(cohort);
    }

    // Update an existing cohort with date validation
    public CohortModel updateCohort(Long id, CohortModel updatedCohort) {
        validateDateRange(updatedCohort.getStartDate(), updatedCohort.getEndDate());
        CohortModel existingCohort = cohortRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cohort not found"));
        existingCohort.setCohortName(updatedCohort.getCohortName()); // Update cohort name
        existingCohort.setStartDate(updatedCohort.getStartDate());
        existingCohort.setEndDate(updatedCohort.getEndDate());
        existingCohort.setLocation(updatedCohort.getLocation());
        existingCohort.setSpecializations(updatedCohort.getSpecializations());
        return cohortRepository.save(existingCohort);
    }
    // Validate the start and end dates for the cohort
    private void validateDateRange(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }
    }

    // Get all cohorts
    public List<CohortModel> getAllCohorts() {
        return cohortRepository.findAll();
    }

    // Get a cohort by ID
    public CohortModel getCohortById(Long id) {
        return cohortRepository.findById(id).orElseThrow(() -> new RuntimeException("Cohort not found"));
    }

    // Delete a cohort by ID
    public void deleteCohort(Long id) {
        cohortRepository.deleteById(id);
    }
    // add a trainee in cohort
    public CohortModel assignTraineesToCohort(Long id, List<String> trainees) {
        CohortModel cohort = cohortRepository.findById(id).orElseThrow(() -> new RuntimeException("Cohort not found"));
        TraineeUtil.addTrainees(cohort.getEnrolledTrainees(), trainees);
        return cohortRepository.save(cohort);
    }

    public CohortModel removeTraineeFromCohort(Long cohortId, String traineeJson) {
        CohortModel cohort = cohortRepository.findById(cohortId)
                .orElseThrow(() -> new RuntimeException("Cohort not found"));
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
            throw new RuntimeException("Invalid JSON format", e);
        }

        // Trim the traineeName to remove any leading/trailing spaces
        String trimmedTraineeName = traineeName.trim();
        System.out.println("Trimmed Trainee to remove: '" + trimmedTraineeName + "'");

        if (cohort.getEnrolledTrainees().remove(trimmedTraineeName)) {
            System.out.println("Updated Enrolled Trainees: " + cohort.getEnrolledTrainees());
            return cohortRepository.save(cohort);
        } else {
            throw new RuntimeException("Trainee not found in the cohort");
        }
    }


}
