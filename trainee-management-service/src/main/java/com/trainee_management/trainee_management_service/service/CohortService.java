package com.trainee_management.trainee_management_service.service;

import com.trainee_management.trainee_management_service.model.Cohort;
import com.trainee_management.trainee_management_service.model.Trainee;
import com.trainee_management.trainee_management_service.repository.CohortRepository;
import com.trainee_management.trainee_management_service.utils.CohortValidator;
import com.trainee_management.trainee_management_service.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CohortService {

    @Autowired
    private CohortRepository cohortRepository;

    // Create a new Cohort with date validation
    @Transactional
    public Cohort createCohort(Cohort cohort) {
        // Validate dates before saving
        CohortValidator.validateCohortDates(cohort);
        return cohortRepository.save(cohort);
    }
    // Get a Cohort by ID including trainees
    @Transactional(readOnly = true)
    public Optional<Cohort> getCohortById(Long cohortId) {
        Optional<Cohort> cohortOptional = cohortRepository.findById(cohortId);
        if (cohortOptional.isPresent()) {
            Cohort cohort = cohortOptional.get();
            List<Trainee> trainees = cohort.getTrainees(); // Access to ensure they are loaded
            return Optional.of(cohort);
        } else {
            throw new ResourceNotFoundException("Cohort not found with id " + cohortId);
        }
    }

    // Get all Cohorts with pagination and ensure trainees are loaded
    @Transactional(readOnly = true)
    public Page<Cohort> getAllCohorts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cohort> cohortsPage = cohortRepository.findAll(pageable);

        cohortsPage.forEach(cohort -> {
            List<Trainee> trainees = cohort.getTrainees(); // Load trainees
        });

        return cohortsPage;
    }

    // Update a Cohort with date validation
    @Transactional
    public Cohort updateCohort(Long cohortId, Cohort cohortDetails) {
        Cohort cohort = cohortRepository.findById(cohortId)
                .orElseThrow(() -> new ResourceNotFoundException("Cohort not found with id " + cohortId));

        // Update only the fields that are non-null in cohortDetails
        if (cohortDetails.getName() != null) {
            cohort.setName(cohortDetails.getName());
        }
        if (cohortDetails.getStartDate() != null) {
            cohort.setStartDate(cohortDetails.getStartDate());
        }
        if (cohortDetails.getEndDate() != null) {
            cohort.setEndDate(cohortDetails.getEndDate());
        }

        // Save and return the updated cohort
        return cohortRepository.save(cohort);
    }


    // Delete a Cohort (cannot delete if there are trainees)
    @Transactional
    public void deleteCohort(Long cohortId) {
        Cohort cohort = cohortRepository.findById(cohortId)
                .orElseThrow(() -> new ResourceNotFoundException("Cohort not found with id " + cohortId));

        // Check if the cohort has trainees
        if (cohort.getTrainees() != null && !cohort.getTrainees().isEmpty()) {
            throw new IllegalStateException("Cannot delete cohort with id " + cohortId + " because it has associated trainees.");
        }

        cohortRepository.delete(cohort);
    }
}
