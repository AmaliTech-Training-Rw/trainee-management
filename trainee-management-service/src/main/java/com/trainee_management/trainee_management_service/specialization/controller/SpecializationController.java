package com.trainee_management.trainee_management_service.specialization.controller;

import com.trainee_management.trainee_management_service.specialization.service.SpecializationService;
import com.trainee_management.trainee_management_service.specialization.model.Specialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationController {

    @Autowired
    private SpecializationService specializationService;

    // Create a new specialization
    @PostMapping
    public Specialization createSpecialization(@RequestBody Specialization specialization) {
        return specializationService.createSpecialization(specialization);
    }

    // Get a specialization by ID
    @GetMapping("/{id}")
    public ResponseEntity<Specialization> getSpecializationById(@PathVariable Long id) {
        Optional<Specialization> specialization = specializationService.getSpecializationById(id);
        return specialization.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get all specializations
    @GetMapping
    public List<Specialization> getAllSpecializations() {
        return specializationService.getAllSpecializations();
    }

    // Update an existing specialization
    @PutMapping("/{id}")
    public ResponseEntity<Specialization> updateSpecialization(@PathVariable Long id, @RequestBody Specialization specializationDetails) {
        try {
            Specialization updatedSpecialization = specializationService.updateSpecialization(id, specializationDetails);
            return ResponseEntity.ok(updatedSpecialization);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a specialization
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialization(@PathVariable Long id) {
        try {
            specializationService.deleteSpecialization(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
