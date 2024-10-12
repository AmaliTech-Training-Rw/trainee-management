package com.trainee_management.trainee_management_service.controller;

import com.trainee_management.trainee_management_service.dto.TrainerRequest;
import com.trainee_management.trainee_management_service.model.Specialization;
import com.trainee_management.trainee_management_service.model.Trainer;
import com.trainee_management.trainee_management_service.service.CloudinaryService;
import com.trainee_management.trainee_management_service.service.SpecializationService;
import com.trainee_management.trainee_management_service.service.TrainerService;
import com.trainee_management.trainee_management_service.utils.ResourceNotFoundException;
import com.trainee_management.trainee_management_service.utils.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    private final TrainerService trainerService;
    private final SpecializationService specializationService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public TrainerController(TrainerService trainerService, SpecializationService specializationService, CloudinaryService cloudinaryService) {
        this.trainerService = trainerService;
        this.specializationService = specializationService;
        this.cloudinaryService = cloudinaryService;
    }

    // Create a new Trainer
    @PostMapping
    public ResponseEntity<Object> createTrainer(
            @Valid @RequestBody TrainerRequest trainerData
//            @RequestPart("file") MultipartFile file
    )
    {
        try {
            Trainer trainer = trainerData.toTrainer();

            // Check specialization by id and assign it to the trainer
            Specialization specialization = specializationService.getSpecializationById(trainerData.getSpecializationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));
            trainer.setSpecialization(specialization);

            // Upload image to Cloudinary
//            String imageUrl = cloudinaryService.uploadFile(file);
//            trainer.setPhotoUrl(imageUrl);

            Trainer createdTrainer = trainerService.createTrainer(trainer);
            return ResponseHandler.responseBuilder("Trainer created successfully", HttpStatus.CREATED, createdTrainer);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    // Get a Trainer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getTrainerById(@PathVariable("id") Long trainerId) {
        try {
            Trainer trainer = trainerService.getTrainerById(trainerId);
            return ResponseHandler.responseBuilder("Trainer fetched successfully", HttpStatus.OK, trainer);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    // Get all Trainers
    @GetMapping
    public ResponseEntity<Object> getAllTrainers() {
        try {
            List<Trainer> trainers = trainerService.getAllTrainers();
            return ResponseHandler.responseBuilder("Trainers fetched successfully", HttpStatus.OK, trainers);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    // Update a Trainer
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTrainer(@PathVariable("id") Long trainerId, @Valid @RequestBody TrainerRequest trainerDetails) {
        try {
            Trainer updatedTrainer = trainerService.updateTrainer(trainerId, trainerDetails.toTrainer());
            return ResponseHandler.responseBuilder("Trainer updated successfully", HttpStatus.OK, updatedTrainer);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    // Delete a Trainer
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTrainer(@PathVariable("id") Long trainerId) {
        try {
            trainerService.deleteTrainer(trainerId);
            return ResponseHandler.responseBuilder("Trainer deleted successfully", HttpStatus.NO_CONTENT, null);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
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