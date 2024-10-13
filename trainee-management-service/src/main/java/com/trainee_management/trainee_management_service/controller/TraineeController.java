package com.trainee_management.trainee_management_service.controller;

import com.trainee_management.trainee_management_service.dto.SuccessResponse;
import com.trainee_management.trainee_management_service.dto.TraineeRequest;
import com.trainee_management.trainee_management_service.model.Trainee;
import com.trainee_management.trainee_management_service.service.SpecializationService;
import com.trainee_management.trainee_management_service.service.TraineeService;
import com.trainee_management.trainee_management_service.utils.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trainees")
public class TraineeController {

    @Autowired
    private SpecializationService specializationService;
    private final TraineeService traineeService;

    @Autowired
    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    // Create a new trainee
    @PostMapping("/create")
    public ResponseEntity<SuccessResponse<?>> createTrainee(@RequestBody TraineeRequest traineeRequest) {
        try {
            Trainee createdTrainee = traineeService.addTrainee(traineeRequest);
            SuccessResponse<Trainee> response = new SuccessResponse<>("Trainee created successfully", createdTrainee);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            SuccessResponse<String> errorResponse = new SuccessResponse<>("Failed to create trainee: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // Get a trainee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Trainee> getTraineeById(@PathVariable Long id) {
        Trainee trainee = traineeService.getTraineeById(id);
        return new ResponseEntity<>(trainee, HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity<List<Trainee>> getAllTrainees() {
        List<Trainee> trainees = traineeService.getAllTrainees();
        return new ResponseEntity<>(trainees, HttpStatus.OK);
    }

    // Update a trainee
    @PutMapping("/{id}")
    public ResponseEntity<?> partialUpdateTrainee(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            // Call service to partially update the trainee with the provided map of updates
            Trainee updatedTrainee = traineeService.partialUpdateTrainee(id, updates);

            // Create a success response message
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Trainee updated successfully");
            response.put("updatedTrainee", updatedTrainee);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {

            return new ResponseEntity<>("Unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    // Delete a trainee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable Long id) {
        traineeService.deleteTrainee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/{specializationId}/trainees/{traineeId}")
    public ResponseEntity<String> removeTraineeFromSpecialization(
            @PathVariable Long specializationId,
            @PathVariable Long traineeId) {
        try {
            specializationService.removeTraineeFromSpecialization(specializationId, traineeId);
            return new ResponseEntity<>("Trainee removed from specialization successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
