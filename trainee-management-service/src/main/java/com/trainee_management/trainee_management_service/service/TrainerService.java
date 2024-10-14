package com.trainee_management.trainee_management_service.service;

import com.trainee_management.trainee_management_service.utils.ResourceNotFoundException;
import com.trainee_management.trainee_management_service.model.Trainer;
import com.trainee_management.trainee_management_service.repository.TrainerRepository;
import com.trainee_management.trainee_management_service.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private SpecializationRepository specializationRepository;

    // Create a new Trainer
    public Trainer createTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    // Get a Trainer by ID
    public Trainer getTrainerById(Long id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id " + id));
    }

    // Get all Trainers
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    // Update a Trainer
    public Trainer updateTrainer(Long id, Trainer trainerDetails) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id " + id));

        trainer.setFirstName(trainerDetails.getFirstName());
        trainer.setLastName(trainerDetails.getLastName());
        trainer.setEmail(trainerDetails.getEmail());
        trainer.setGender(trainerDetails.getGender());
        trainer.setCountry(trainerDetails.getCountry());
        trainer.setPhoneNumber(trainerDetails.getPhoneNumber());
        trainer.setSpecialization(trainerDetails.getSpecialization());

        return trainerRepository.save(trainer);
    }

    // Delete a Trainer
    public void deleteTrainer(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id " + id));

        trainerRepository.delete(trainer);
    }
}