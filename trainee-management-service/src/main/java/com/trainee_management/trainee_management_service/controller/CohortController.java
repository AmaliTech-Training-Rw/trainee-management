package com.trainee_management.trainee_management_service.controller;

import com.trainee_management.trainee_management_service.dto.SuccessResponse;
import com.trainee_management.trainee_management_service.model.Cohort;
import com.trainee_management.trainee_management_service.service.CohortService;
import com.trainee_management.trainee_management_service.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trainees/cohorts")
public class CohortController {

    @Autowired
    private CohortService cohortService;

    @PostMapping("/create")
    public ResponseEntity<SuccessResponse<Cohort>> createCohort(@RequestBody Cohort cohort) {
        Cohort createdCohort = cohortService.createCohort(cohort);
        SuccessResponse<Cohort> response = new SuccessResponse<>("Cohort created successfully", createdCohort);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Cohort>> getCohortById(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(cohortService.getCohortById(id));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Cohort>> getAllCohorts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(cohortService.getAllCohorts(page, size).getContent()); // Return the content of the Page
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<Cohort>> updateCohort(@PathVariable Long id, @RequestBody Cohort cohortDetails) {
        Cohort updatedCohort = cohortService.updateCohort(id, cohortDetails);
        SuccessResponse<Cohort> response = new SuccessResponse<>("Cohort updated successfully", updatedCohort);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCohort(@PathVariable Long id) {
        try {
            cohortService.deleteCohort(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred: " + e.getMessage()); // Return 500 for any other errors
        }
    }

}
