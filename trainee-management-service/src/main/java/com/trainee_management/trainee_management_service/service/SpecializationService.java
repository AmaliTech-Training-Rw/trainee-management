package com.trainee_management.trainee_management_service.service;

import com.trainee_management.trainee_management_service.dto.SpecializationDTO;
import com.trainee_management.trainee_management_service.model.Specialization;
import com.trainee_management.trainee_management_service.model.Trainee;
import com.trainee_management.trainee_management_service.repository.SpecializationRepository;
import com.trainee_management.trainee_management_service.repository.TraineeRepository;
import com.trainee_management.trainee_management_service.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepository;
    @Autowired
    private TraineeRepository traineeRepository;

    // Create a new specialization
    @Transactional
    public Specialization createSpecialization(Specialization specialization) {
        return specializationRepository.save(specialization);
    }

    // Get a specialization by ID
    @Transactional(readOnly = true)
    public Optional<Specialization> getSpecializationById(Long id) {
        return specializationRepository.findById(id);
    }

    // Get all specializations
    @Transactional(readOnly = true)
    public Page<Specialization> getSpecializationsWithPagination(Pageable pageable) {
        return specializationRepository.findAll(pageable);
    }
    // Update an existing specialization
    @Transactional
    public SpecializationDTO updateSpecialization(Long id, Specialization specializationDetails) {
        Specialization specialization = specializationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Specialization not found"));

        // Update fields only if they are not null in specializationDetails
        if (specializationDetails.getName() != null) {
            specialization.setName(specializationDetails.getName());
        }

        if (specializationDetails.getDescription() != null) {
            specialization.setDescription(specializationDetails.getDescription());
        }

        Specialization updatedSpecialization = specializationRepository.save(specialization);

        // Return the DTO, excluding the lazy-loaded trainees collection
        return new SpecializationDTO(
                updatedSpecialization.getId(),
                updatedSpecialization.getName(),
                updatedSpecialization.getDescription()
        );
    }


    // Delete a specialization (Only if no trainees are assigned)
    @Transactional
    public String deleteSpecialization(Long id) {
        Specialization specialization = specializationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found with id " + id));

        // Check if the specialization can be deleted
        if (specialization.getTrainees() != null && !specialization.getTrainees().isEmpty()) {
            throw new IllegalStateException("Cannot delete specialization with id " + id + " because it has associated trainees.");
        }

        specializationRepository.delete(specialization);
        return "Specialization with id " + id + " deleted successfully.";
    }

    @Transactional
    public void removeTraineeFromSpecialization(Long specializationId, Long traineeId) {
        Specialization specialization = specializationRepository.findById(specializationId)
                .orElseThrow(() -> new RuntimeException("Specialization not found"));

        Trainee trainee = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new RuntimeException("Trainee not found"));

        // Remove trainee from specialization
        specialization.getTrainees().remove(trainee);
        trainee.setSpecialization(null);
        traineeRepository.save(trainee);

        specializationRepository.save(specialization);
    }
}
