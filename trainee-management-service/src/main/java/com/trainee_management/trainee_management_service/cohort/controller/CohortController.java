package com.trainee_management.trainee_management_service.cohort.controller;

import com.trainee_management.trainee_management_service.cohort.dto.AssignTraineesRequest;
import com.trainee_management.trainee_management_service.cohort.model.CohortModel;
import com.trainee_management.trainee_management_service.cohort.service.CohortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainees/cohorts")
public class CohortController {

    @Autowired
    private CohortService cohortService;

    @PostMapping
    public ResponseEntity<CohortModel> createCohort(@RequestBody CohortModel cohort) {
        return cohortService.createCohort(cohort);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CohortModel> updateCohort(@PathVariable Long id, @RequestBody CohortModel updatedCohort) {
        return cohortService.updateCohort(id, updatedCohort);
    }

    @GetMapping
    public ResponseEntity<List<CohortModel>> getAllCohorts() {
        return cohortService.getAllCohorts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CohortModel> getCohortById(@PathVariable Long id) {
        return cohortService.getCohortById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCohort(@PathVariable Long id) {
        return cohortService.deleteCohort(id);
    }

    @PostMapping("/{id}/assign-trainees")
    public ResponseEntity<CohortModel> assignTraineesToCohort(@PathVariable Long id, @RequestBody AssignTraineesRequest request) {
        return cohortService.assignTraineesToCohort(id, request.getTrainees());
    }


    @DeleteMapping("/{id}/remove-trainees")
    public ResponseEntity<CohortModel> removeTraineeFromCohort(@PathVariable Long id, @RequestBody String traineeId) {
        return cohortService.removeTraineeFromCohort(id, traineeId);
    }


}
