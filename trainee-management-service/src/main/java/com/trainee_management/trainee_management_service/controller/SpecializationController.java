package com.trainee_management.trainee_management_service.controller;

import com.trainee_management.trainee_management_service.dto.SpecializationDTO;
import com.trainee_management.trainee_management_service.dto.SuccessResponse;
import com.trainee_management.trainee_management_service.service.SpecializationService;
import com.trainee_management.trainee_management_service.model.Specialization;
import com.trainee_management.trainee_management_service.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trainees/specialization")
public class SpecializationController {

    @Autowired
    private SpecializationService specializationService;

    // Create a new specialization
    @PostMapping("/create")
    public Specialization createSpecialization(@RequestBody Specialization specialization) {
        return specializationService.createSpecialization(specialization);
    }

    // Get a specialization by ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getSpecializationById(@PathVariable Long id) {
        try {
            Optional<Specialization> specialization = specializationService.getSpecializationById(id);
            return ResponseEntity.ok(specialization);
        }catch (Exception e){
           return ResponseEntity.notFound().build();
        }
    }

    // Get all specializations
    @GetMapping("/all")
    public ResponseEntity<Page<Specialization>> getAllSpecializations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Specialization> specializations = specializationService.getSpecializationsWithPagination(pageable);

        return new ResponseEntity<>(specializations, HttpStatus.OK);
    }

    // Update an existing specialization
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<SpecializationDTO>> updateSpecialization(
            @PathVariable Long id, @RequestBody Specialization specializationDetails) {
        try {
            SpecializationDTO updatedSpecialization = specializationService.updateSpecialization(id, specializationDetails);
            SuccessResponse<SpecializationDTO> response = new SuccessResponse<>(
                    "Specialization updated successfully", updatedSpecialization);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    // Delete a specialization
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSpecialization(@PathVariable Long id) {
        try {
            String message = specializationService.deleteSpecialization(id);
            return ResponseEntity.ok(message); // Return success message
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Return not found message
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Return bad request message
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred: " + e.getMessage());
        }
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
