package com.trainee_management.trainee_management_service.service;

import com.trainee_management.trainee_management_service.model.Specialization;
import com.trainee_management.trainee_management_service.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepository;

    // Create a new specialization
    public Specialization createSpecialization(Specialization specialization) {
        return specializationRepository.save(specialization);
    }

    // Get a specialization by ID
    public Optional<Specialization> getSpecializationById(Long id) {
        return specializationRepository.findById(id);
    }

    // Get all specializations
    public List<Specialization> getAllSpecializations() {
        return specializationRepository.findAll();
    }

    // Update an existing specialization
    public Specialization updateSpecialization(Long id, Specialization specializationDetails) {
        Specialization specialization = specializationRepository.findById(id).orElseThrow(() -> new RuntimeException("Specialization not found"));

        specialization.setName(specializationDetails.getName());
        specialization.setDescription(specializationDetails.getDescription());

        return specializationRepository.save(specialization);
    }

    // Delete a specialization
    public void deleteSpecialization(Long id) {
        Specialization specialization = specializationRepository.findById(id).orElseThrow(() -> new RuntimeException("Specialization not found"));
        specializationRepository.delete(specialization);
    }
}
