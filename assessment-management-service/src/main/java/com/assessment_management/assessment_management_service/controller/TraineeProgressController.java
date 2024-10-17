package com.assessment_management.assessment_management_service.controller;

import com.assessment_management.assessment_management_service.model.IdListRequest;
import com.assessment_management.assessment_management_service.model.TraineeProgress;
import com.assessment_management.assessment_management_service.service.TraineeProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainee-progress")
public class TraineeProgressController {

    @Autowired
    private TraineeProgressService traineeProgressService;

    // Get all trainee progress
    @GetMapping
    public ResponseEntity<List<TraineeProgress>> getAllProgress() {
        return traineeProgressService.getAllProgress();
    }

    // Get trainee progress by ID
    @GetMapping("/{id}")
    public ResponseEntity<TraineeProgress> getProgressById(@PathVariable String id) {
        return traineeProgressService.getProgressById(id);
    }


    // Create new trainee progress
    @PostMapping
    public ResponseEntity<TraineeProgress> saveProgress(@RequestBody TraineeProgress traineeProgress) {
        return traineeProgressService.saveProgress(traineeProgress);
    }

    // Update trainee progress by ID
    @PutMapping("/{id}")
    public ResponseEntity<TraineeProgress> updateProgress(@PathVariable String id, @RequestBody TraineeProgress updatedProgress) {
        return traineeProgressService.updateProgress(id, updatedProgress);
    }

    // Delete trainee progress by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgress(@PathVariable String id) {
        return traineeProgressService.deleteProgress(id);
    }

    // New route to handle multiple ID lookups
    @PostMapping("/get-only-selected")
    public ResponseEntity<List<TraineeProgress>> getMultipleProgressByIds(@RequestBody IdListRequest idListRequest) {
        return traineeProgressService.getMultipleProgressByIds(idListRequest.getIds());
    }
}
