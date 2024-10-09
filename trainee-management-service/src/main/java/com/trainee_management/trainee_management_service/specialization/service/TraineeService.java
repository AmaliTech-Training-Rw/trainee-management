package com.trainee_management.trainee_management_service.specialization.service;

import com.trainee_management.trainee_management_service.specialization.ResourceNotFoundException;
import com.trainee_management.trainee_management_service.specialization.model.Cohort;
import com.trainee_management.trainee_management_service.specialization.model.Trainee;
import com.trainee_management.trainee_management_service.specialization.model.Specialization;
import com.trainee_management.trainee_management_service.specialization.repository.CohortRepository;
import com.trainee_management.trainee_management_service.specialization.repository.TraineeRepository;
import com.trainee_management.trainee_management_service.specialization.repository.SpecializationRepository; // Ensure this repository exists
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Trainee createTrainee(Trainee trainee) {
        // Check if the trainee has an ongoing specialization
        if (trainee.getProgressionStatus() == Trainee.ProgressionStatus.IN_PROGRESS) {
            throw new RuntimeException("Trainee cannot enroll in another specialization while one is ongoing.");
        }

        // Check if Cohort is provided and exists
        if (trainee.getCohort() != null) {
            Cohort cohort = cohortRepository.findById(trainee.getCohort().getCohortId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cohort not found with id " + trainee.getCohort().getCohortId()));
            trainee.setCohort(cohort); // Ensure cohort is managed
        }

        // Check if Specialization is provided and exists
        if (trainee.getSpecialization() != null) {
            Specialization specialization = specializationRepository.findById(trainee.getSpecialization().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Specialization not found with id " + trainee.getSpecialization().getId()));
            trainee.setSpecialization(specialization); // Ensure specialization is managed
        }

        return traineeRepository.save(trainee);
    }


    // Get a Trainee by ID
    public Trainee getTraineeById(Long traineeId) {
        return traineeRepository.findById(traineeId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found with id " + traineeId));
    }

    // Get all Trainees
    public List<Trainee> getAllTrainees() {
        return traineeRepository.findAll();
    }

    // Update a Trainee
    @Transactional
    public Trainee updateTrainee(Long traineeId, Trainee traineeDetails) {
        Trainee trainee = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found with id " + traineeId));

        // Prevent changing specialization if the trainee has an ongoing one
        if (trainee.getProgressionStatus() == Trainee.ProgressionStatus.IN_PROGRESS) {
            throw new RuntimeException("Trainee cannot change specialization while one is ongoing.");
        }

        // Allow specialization change if the trainee has completed their previous specialization
        if (trainee.getProgressionStatus() == Trainee.ProgressionStatus.COMPLETED) {
            // Set the new specialization
            trainee.setSpecialization(traineeDetails.getSpecialization());

            // If cohort is provided in the update, retrieve it from the database
            if (traineeDetails.getCohort() != null) {
                Cohort cohort = cohortRepository.findById(traineeDetails.getCohort().getCohortId())
                        .orElseThrow(() -> new ResourceNotFoundException("Cohort not found with id " + traineeDetails.getCohort().getCohortId()));
                trainee.setCohort(cohort);
            }
        } else {
            // Prevent specialization change if the trainee has not completed the program
            throw new RuntimeException("Trainee can only change specialization if they have completed the previous one.");
        }

        // Update other fields
        trainee.setName(traineeDetails.getName() != null ? traineeDetails.getName() : trainee.getName());
        trainee.setEmail(traineeDetails.getEmail() != null ? traineeDetails.getEmail() : trainee.getEmail());

        // Handle progression status conversion
        if (traineeDetails.getProgressionStatus() != null) {
            trainee.setProgressionStatus(traineeDetails.getProgressionStatus());
        }

        return traineeRepository.save(trainee);
    }


    // Delete a Trainee
    @Transactional
    public void deleteTrainee(Long traineeId) {
        Trainee trainee = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found with id " + traineeId));

        traineeRepository.delete(trainee);
    }
}
