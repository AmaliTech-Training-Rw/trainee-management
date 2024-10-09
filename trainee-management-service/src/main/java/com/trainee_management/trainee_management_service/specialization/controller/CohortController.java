package com.trainee_management.trainee_management_service.specialization.controller;

import com.trainee_management.trainee_management_service.specialization.model.Cohort;
import com.trainee_management.trainee_management_service.specialization.service.CohortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cohorts")
public class CohortController {

    @Autowired
    private CohortService cohortService;

    @PostMapping
    public ResponseEntity<Cohort> createCohort(@RequestBody Cohort cohort) {
        return ResponseEntity.ok(cohortService.createCohort(cohort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cohort> getCohortById(@PathVariable Long id) {
        return ResponseEntity.ok(cohortService.getCohortById(id).orElse(null));
    }

    @GetMapping
    public ResponseEntity<List<Cohort>> getAllCohorts() {
        return ResponseEntity.ok(cohortService.getAllCohorts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cohort> updateCohort(@PathVariable Long id, @RequestBody Cohort cohortDetails) {
        return ResponseEntity.ok(cohortService.updateCohort(id, cohortDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCohort(@PathVariable Long id) {
        cohortService.deleteCohort(id);
        return ResponseEntity.noContent().build();
    }
}
