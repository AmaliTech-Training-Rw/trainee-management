
package com.trainee_management.trainee_management_service.service;

import com.trainee_management.trainee_management_service.model.CohortModel;
import com.trainee_management.trainee_management_service.repository.CohortRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CohortService {

    @Autowired
    private CohortRepository cohortRepository;

    // Create a new cohort
    public CohortModel createCohort(CohortModel cohort) {
        return cohortRepository.save(cohort);
    }

    // Get all cohorts
    public List<CohortModel> getAllCohorts() {
        return cohortRepository.findAll();
    }

    // Get cohort by ID
    public CohortModel getCohortById(Long id) {
        Optional<CohortModel> cohort = cohortRepository.findById(id);
        return cohort.orElse(null); // Return null if cohort not found, could also throw an exception
    }

    // Update a cohort
    public CohortModel updateCohort(Long id, CohortModel updatedCohort) {
        Optional<CohortModel> cohortOptional = cohortRepository.findById(id);
        if (cohortOptional.isPresent()) {
            CohortModel existingCohort = cohortOptional.get();
            existingCohort.setStartDate(updatedCohort.getStartDate());
            existingCohort.setEndDate(updatedCohort.getEndDate());
            existingCohort.setLocation(updatedCohort.getLocation());
            existingCohort.setSpecializations(updatedCohort.getSpecializations());
            existingCohort.setEnrolledTrainees(updatedCohort.getEnrolledTrainees());
            return cohortRepository.save(existingCohort);
        }
        return null; // Return null or throw an exception if cohort not found
    }

    // Delete a cohort
    public void deleteCohort(Long id) {
        cohortRepository.deleteById(id);
    }
}
