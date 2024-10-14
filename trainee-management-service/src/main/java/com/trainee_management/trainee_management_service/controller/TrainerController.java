package com.trainee_management.trainee_management_service.controller;

import com.trainee_management.trainee_management_service.dto.TrainerRequest;
import com.trainee_management.trainee_management_service.model.Specialization;
import com.trainee_management.trainee_management_service.model.Trainer;
import com.trainee_management.trainee_management_service.service.KafkaProducerService;
import com.trainee_management.trainee_management_service.service.SpecializationService;
import com.trainee_management.trainee_management_service.service.TrainerService;
import com.trainee_management.trainee_management_service.utils.ResourceNotFoundException;
import com.trainee_management.trainee_management_service.utils.ResponseHandler;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/trainers")
public class TrainerController {

    private final TrainerService trainerService;
    private final SpecializationService specializationService;
    private final KafkaProducerService kafkaProducerService;
    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);

    @Autowired
    public TrainerController(TrainerService trainerService, SpecializationService specializationService, KafkaProducerService kafkaProducerService) {
        this.trainerService = trainerService;
        this.specializationService = specializationService;
        this.kafkaProducerService = kafkaProducerService;
    }

    // Create a new Trainer
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createTrainer(
            @Valid @RequestPart TrainerRequest trainerData,
            @RequestPart("file") MultipartFile file
    ) {
        try {
            Trainer trainer = trainerData.toTrainer();


            // Upload image using Kafka
            CompletableFuture<String> futureImageUrl = kafkaProducerService.sendImageMessage(file);
            String imageUrl = futureImageUrl.get(30, TimeUnit.SECONDS);
            trainer.setPhotoUrl(imageUrl);

            Trainer createdTrainer = trainerService.createTrainer(trainer);
            logger.info("Trainer created: {}", createdTrainer.getEmail());
            return ResponseHandler.responseBuilder("Trainer created successfully", HttpStatus.CREATED, createdTrainer);
        } catch (Exception e) {
            logger.error("Error adding trainer: {}", e.getMessage());
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    // Get a Trainer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getTrainerById(@PathVariable("id") Long trainerId) {
        try {
            Trainer trainer = trainerService.getTrainerById(trainerId);
            logger.info("Trainer fetched successfully: {}", trainer.getEmail());
            return ResponseHandler.responseBuilder("Trainer fetched successfully", HttpStatus.OK, trainer);
        } catch (Exception e) {
            logger.error("Error getting trainer: {}", e.getMessage());
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    // Get all Trainers
    @GetMapping
    public ResponseEntity<Object> getAllTrainers() {
        try {
            List<Trainer> trainers = trainerService.getAllTrainers();
            logger.info("Trainers fetched successfully: {}", trainers.size());
            return ResponseHandler.responseBuilder("Trainers fetched successfully", HttpStatus.OK, trainers);
        } catch (Exception e) {
            logger.error("Error getting trainers: {}", e.getMessage());
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    // Update a Trainer
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateTrainer(@PathVariable("id") Long trainerId,
                                                @Valid @RequestPart TrainerRequest trainerDetails,
                                                @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            Trainer trainer = trainerDetails.toTrainer();

            if (file != null && !file.isEmpty()) {
                CompletableFuture<String> futureImageUrl = kafkaProducerService.sendImageMessage(file);
                String imageUrl = futureImageUrl.get(30, TimeUnit.SECONDS);
                trainer.setPhotoUrl(imageUrl);
            }

            Trainer updatedTrainer = trainerService.updateTrainer(trainerId, trainer);
            logger.info("Trainer updated successfully: {}", updatedTrainer.getEmail());
            return ResponseHandler.responseBuilder("Trainer updated successfully", HttpStatus.OK, updatedTrainer);
        } catch (Exception e) {
            logger.error("Error updating trainer: {}", e.getMessage());
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    // Delete a Trainer
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTrainer(@PathVariable("id") Long trainerId) {
        try {
            trainerService.deleteTrainer(trainerId);
            logger.info("Trainer deleted successfully: {}", trainerId);
            return ResponseHandler.responseBuilder("Trainer deleted successfully", HttpStatus.NO_CONTENT, null);
        } catch (Exception e) {
            logger.error("Error deleting trainer: {}", e.getMessage());
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}