package com.trainee_management.trainee_management_service.service;

import com.trainee_management.trainee_management_service.utils.ResourceNotFoundException;
import com.trainee_management.trainee_management_service.model.Trainee;
import com.trainee_management.trainee_management_service.repository.CohortRepository;
import com.trainee_management.trainee_management_service.repository.TraineeRepository;
import com.trainee_management.trainee_management_service.repository.SpecializationRepository; // Ensure this repository exists
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeService {

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private CohortRepository cohortRepository;

    @Autowired
    private SpecializationRepository specializationRepository; // Add this line

    // Create a new Trainee
    public Trainee createTrainee(Trainee trainee) {
        return traineeRepository.save(trainee);
    }

    // Get a Trainee by ID
    public Trainee getTraineeById(Long id) {
        return traineeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found with id " + id));
    }

    // Get all Trainees
    public List<Trainee> getAllTrainees() {
        return traineeRepository.findAll();
    }

    // Update a Trainee
    public Trainee updateTrainee(Long id, Trainee traineeDetails) {
        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found with id " + id));

        trainee.setFirstName(traineeDetails.getFirstName());
        trainee.setLastName(traineeDetails.getLastName());
        trainee.setEmail(traineeDetails.getEmail());
        trainee.setDateOfBirth(traineeDetails.getDateOfBirth());
        trainee.setGender(traineeDetails.getGender());
        trainee.setCountry(traineeDetails.getCountry());
        trainee.setAddress(traineeDetails.getAddress());
        trainee.setPhoneNumber(traineeDetails.getPhoneNumber());
        trainee.setUniversity(traineeDetails.getUniversity());
        trainee.setPhotoUrl(traineeDetails.getPhotoUrl());
        trainee.setIsActive(traineeDetails.getIsActive());
        trainee.setEnrollmentDate(traineeDetails.getEnrollmentDate());
        trainee.setTrainingId(traineeDetails.getTrainingId());
        trainee.setSpecialization(traineeDetails.getSpecialization());
        trainee.setCohort(traineeDetails.getCohort());

        return traineeRepository.save(trainee);
    }

    // Delete a Trainee
    public void deleteTrainee(Long id) {
        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found with id " + id));

        traineeRepository.delete(trainee);
    }

}
