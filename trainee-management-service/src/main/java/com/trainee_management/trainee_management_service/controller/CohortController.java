package com.trainee_management.trainee_management_service.controller;


import com.trainee_management.trainee_management_service.dto.AssignTraineesRequest;
import com.trainee_management.trainee_management_service.model.CohortModel;
import com.trainee_management.trainee_management_service.service.CohortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainees/cohorts")
public class CohortController {

    @Autowired
    private CohortService cohortService;

    // Create a new cohort
    @PostMapping
    public CohortModel createCohort(@RequestBody CohortModel cohort) {
        return cohortService.createCohort(cohort);
    }

    // Get all cohorts
    @GetMapping
    public List<CohortModel> getAllCohorts() {
        return cohortService.getAllCohorts();
    }

    // Get cohort by ID
    @GetMapping("/{id}")
    public CohortModel getCohortById(@PathVariable Long id) {
        return cohortService.getCohortById(id);
    }

    // Update a cohort
    @PutMapping("/{id}")
    public CohortModel updateCohort(@PathVariable Long id, @RequestBody CohortModel updatedCohort) {
        return cohortService.updateCohort(id, updatedCohort);
    }

    // Delete a cohort
    @DeleteMapping("/{id}")
    public void deleteCohort(@PathVariable Long id) {
        cohortService.deleteCohort(id);
    }

    // Assign trainees to a cohort
    @PostMapping("/{id}/assign-trainees")
    public CohortModel assignTraineesToCohort(@PathVariable Long id, @RequestBody AssignTraineesRequest request) {
        return cohortService.assignTraineesToCohort(id, request.getTrainees());
    }

    // Delete a trainee from a cohort
    @DeleteMapping("/{id}/remove-trainees")
    public CohortModel removeTraineeFromCohort(@PathVariable Long id, @RequestBody String traineeId) {
        return cohortService.removeTraineeFromCohort(id, traineeId);
    }
}
