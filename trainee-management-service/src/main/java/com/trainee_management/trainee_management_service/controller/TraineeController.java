package com.trainee_management.trainee_management_service.controller;

import com.trainee_management.trainee_management_service.dto.SuccessResponse;
import com.trainee_management.trainee_management_service.dto.TraineeRequest;
import com.trainee_management.trainee_management_service.model.Cohort;
import com.trainee_management.trainee_management_service.model.Specialization;
import com.trainee_management.trainee_management_service.service.*;
import com.trainee_management.trainee_management_service.utils.ResourceNotFoundException;
import com.trainee_management.trainee_management_service.model.Trainee;
import com.trainee_management.trainee_management_service.utils.ResponseHandler;
import jakarta.validation.Valid;
import org.cloudinary.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/trainees")
public class TraineeController {

    @Autowired
    private SpecializationService specializationService;
    private final TraineeService traineeService;
    private final CohortService cohortService;
    private final KafkaProducerService kafkaProducerService;
    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);


    @Autowired
    public TraineeController(
            TraineeService traineeService,
            CohortService cohortService,
            SpecializationService specializationService,
            CloudinaryService cloudinaryService,
            KafkaProducerService kafkaProducerService) {
        this.traineeService = traineeService;
        this.cohortService = cohortService;
        this.specializationService = specializationService;
        this.kafkaProducerService = kafkaProducerService;
    }

    // Create a new Trainee

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createTrainee(
            @Valid @RequestPart TraineeRequest traineeData,
            @RequestPart("file") MultipartFile file
    )
    {
        try {

            CompletableFuture<String> futureImageUrl = kafkaProducerService.sendImageMessage(file);

            // Wait for the image URL (with a timeout)
            String imageUrl = futureImageUrl.get(30, TimeUnit.SECONDS);
            traineeData.setPhotoUrl(imageUrl);

            Trainee createdTrainee = traineeService.addTrainee(traineeData);

            logger.info("Trainee created: {}", createdTrainee.getEmail());

            SuccessResponse<Trainee> response = new SuccessResponse<>("Trainee created successfully", createdTrainee);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error adding trainee: {}", e.getMessage());
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

    // Get all Trainees
    @GetMapping("/all")
    public ResponseEntity<List<Trainee>> getAllTrainees() {
        List<Trainee> trainees = traineeService.getAllTrainees();
        return new ResponseEntity<>(trainees, HttpStatus.OK);
    }

    // Update a trainee
    @PutMapping("/{id}")
    public ResponseEntity<?> partialUpdateTrainee(
            @PathVariable Long id,
            @RequestPart Map<String, Object> updates,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {

            if (file != null && !file.isEmpty()) {
                // Upload the file and get the image URL
                CompletableFuture<String> futureImageUrl = kafkaProducerService.sendImageMessage(file);
                String imageUrl = futureImageUrl.get(30, TimeUnit.SECONDS);

                // Add the imageUrl to the updates map
                updates.put("photoUrl", imageUrl);
            }
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
