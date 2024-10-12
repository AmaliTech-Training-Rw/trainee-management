package com.trainee_management.trainee_management_service.controller;

import com.trainee_management.trainee_management_service.dto.TraineeRequest;
import com.trainee_management.trainee_management_service.model.Cohort;
import com.trainee_management.trainee_management_service.model.Specialization;
import com.trainee_management.trainee_management_service.service.CloudinaryService;
import com.trainee_management.trainee_management_service.service.CohortService;
import com.trainee_management.trainee_management_service.service.SpecializationService;
import com.trainee_management.trainee_management_service.utils.ResourceNotFoundException;
import com.trainee_management.trainee_management_service.model.Trainee;
import com.trainee_management.trainee_management_service.service.TraineeService;
import com.trainee_management.trainee_management_service.utils.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {

    private final TraineeService traineeService;
    private final CohortService cohortService;
    private final SpecializationService specializationService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public TraineeController(TraineeService traineeService, CohortService cohortService, SpecializationService specializationService, CloudinaryService cloudinaryService) {
        this.traineeService = traineeService;
        this.cohortService = cohortService;
        this.specializationService = specializationService;
        this.cloudinaryService = cloudinaryService;
    }

    // Create a new Trainee
    @PostMapping
    public ResponseEntity<Object> createTrainee(
            @Valid @RequestBody TraineeRequest traineeData
//            @RequestPart("file") MultipartFile file
            )
    {
        try {
            System.out.println("hhhhhhhhhh");
            Trainee trainee = traineeData.toTrainee();

            // Check cohort by id and assign it to the trainee
            Cohort cohort = cohortService.getCohortById(traineeData.getCohortId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cohort not found"));
            trainee.setCohort(cohort);

            // Check specialization by id and assign it to the trainee
            Specialization specialization = specializationService.getSpecializationById(traineeData.getSpecializationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));
            trainee.setSpecialization(specialization);

            // Upload image to Cloudinary
//            String imageUrl = cloudinaryService.uploadFile(file);
//            trainee.setPhotoUrl(imageUrl);

            Trainee createdTrainee = traineeService.createTrainee(trainee);
            return ResponseHandler.responseBuilder("Trainee created successfully", HttpStatus.CREATED, createdTrainee);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    // Get a Trainee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getTraineeById(@PathVariable("id") Long traineeId) {
        try {
            Trainee trainee = traineeService.getTraineeById(traineeId);
            return ResponseHandler.responseBuilder("Trainee fetched successfully", HttpStatus.OK, trainee);
        }catch(Exception e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }

    }

    // Get all Trainees
    @GetMapping
    public ResponseEntity<Object> getAllTrainees() {
        try {
            List<Trainee> trainees = traineeService.getAllTrainees();
            return ResponseHandler.responseBuilder("Trainees fetched successfully", HttpStatus.OK, trainees);
        }catch (Exception e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }

    }

    // Update a Trainee
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTrainee(@PathVariable("id") Long traineeId,@Valid @RequestBody TraineeRequest traineeDetails) {
       try {
           Trainee updatedTrainee = traineeService.updateTrainee(traineeId, traineeDetails.toTrainee());
           return ResponseHandler.responseBuilder("Trainee updated successfully", HttpStatus.OK, updatedTrainee);
       }catch (Exception e) {
           return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
       }
    }

    // Delete a Trainee
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTrainee(@PathVariable("id") Long traineeId) {
        try{
            traineeService.deleteTrainee(traineeId);
            return ResponseHandler.responseBuilder("Trainee deleted successfully", HttpStatus.NO_CONTENT, null);
        }catch (Exception e){
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
