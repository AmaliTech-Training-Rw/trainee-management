package com.trainee_management.trainee_management_service.service;

import com.trainee_management.trainee_management_service.utils.ResourceNotFoundException;
import com.trainee_management.trainee_management_service.model.Cohort;
import com.trainee_management.trainee_management_service.repository.CohortRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CohortService {

    @Autowired
    private CohortRepository cohortRepository;

    // Create a new Cohort
    public Cohort createCohort(Cohort cohort) {
        return cohortRepository.save(cohort);
    }

    // Get a Cohort by ID
    public Optional<Cohort> getCohortById(Long cohortId) {
        return cohortRepository.findById(cohortId);
    }

    // Get all Cohorts
    public List<Cohort> getAllCohorts() {
        return cohortRepository.findAll();
    }

    // Update a Cohort
    public Cohort updateCohort(Long cohortId, Cohort cohortDetails) {
        Cohort cohort = cohortRepository.findById(cohortId)
                .orElseThrow(() -> new ResourceNotFoundException("Cohort not found with id " + cohortId));

        cohort.setName(cohortDetails.getName());
        cohort.setStartDate(cohortDetails.getStartDate());
        cohort.setEndDate(cohortDetails.getEndDate());

        return cohortRepository.save(cohort);
    }

    // Delete a Cohort
    public void deleteCohort(Long cohortId) {
        Cohort cohort = cohortRepository.findById(cohortId)
                .orElseThrow(() -> new ResourceNotFoundException("Cohort not found with id " + cohortId));

        cohortRepository.delete(cohort);
    }
}
