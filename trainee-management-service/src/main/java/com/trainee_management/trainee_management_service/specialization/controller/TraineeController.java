package com.trainee_management.trainee_management_service.specialization.controller;

import com.trainee_management.trainee_management_service.specialization.ResourceNotFoundException;
import com.trainee_management.trainee_management_service.specialization.model.Trainee;
import com.trainee_management.trainee_management_service.specialization.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {

    @Autowired
    private TraineeService traineeService;

    // Create a new Trainee
    @PostMapping
    public ResponseEntity<Trainee> createTrainee(@RequestBody Trainee trainee) {
        Trainee createdTrainee = traineeService.createTrainee(trainee);
        return new ResponseEntity<>(createdTrainee, HttpStatus.CREATED);
    }

    // Get a Trainee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Trainee> getTraineeById(@PathVariable("id") Long traineeId) {
        Trainee trainee = traineeService.getTraineeById(traineeId);
        return new ResponseEntity<>(trainee, HttpStatus.OK);
    }

    // Get all Trainees
    @GetMapping
    public ResponseEntity<List<Trainee>> getAllTrainees() {
        List<Trainee> trainees = traineeService.getAllTrainees();
        return new ResponseEntity<>(trainees, HttpStatus.OK);
    }

    // Update a Trainee
    @PutMapping("/{id}")
    public ResponseEntity<Trainee> updateTrainee(@PathVariable("id") Long traineeId, @RequestBody Trainee traineeDetails) {
        Trainee updatedTrainee = traineeService.updateTrainee(traineeId, traineeDetails);
        return new ResponseEntity<>(updatedTrainee, HttpStatus.OK);
    }

    // Delete a Trainee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable("id") Long traineeId) {
        traineeService.deleteTrainee(traineeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Handle ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Handle other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
