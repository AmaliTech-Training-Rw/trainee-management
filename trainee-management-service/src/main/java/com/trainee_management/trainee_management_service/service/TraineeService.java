package com.trainee_management.trainee_management_service.service;

import com.trainee_management.trainee_management_service.dto.TraineeRequest;
import com.trainee_management.trainee_management_service.model.Cohort;
import com.trainee_management.trainee_management_service.model.Specialization;
import com.trainee_management.trainee_management_service.model.Trainee;
import com.trainee_management.trainee_management_service.repository.CohortRepository;
import com.trainee_management.trainee_management_service.repository.SpecializationRepository;
import com.trainee_management.trainee_management_service.repository.TraineeRepository;
import com.trainee_management.trainee_management_service.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TraineeService {

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private CohortRepository cohortRepository;

    @Autowired
    private SpecializationRepository specializationRepository;
// create trainee
@Transactional
public Trainee addTrainee(TraineeRequest traineeRequest) {
    Trainee trainee = traineeRequest.toTrainee();

    try {
        // Check if a trainee with the same email already exists
        Optional<Trainee> existingTraineeOpt = traineeRepository.findByEmail(traineeRequest.getEmail());
        if (existingTraineeOpt.isPresent()) {
            throw new RuntimeException("Trainee with email " + traineeRequest.getEmail() + " already exists.");
        }

        // Fetch Specialization by name
        Optional<Specialization> specializationOpt = specializationRepository.findByName(traineeRequest.getSpecializationName());
        if (specializationOpt.isPresent()) {
            trainee.setSpecialization(specializationOpt.get());
        } else {
            throw new RuntimeException("Specialization not found: " + traineeRequest.getSpecializationName());
        }

        // Fetch Cohort by name
        Optional<Cohort> cohortOpt = cohortRepository.findByName(traineeRequest.getCohortName());
        if (cohortOpt.isPresent()) {
            trainee.setCohort(cohortOpt.get());
        } else {
            throw new RuntimeException("Cohort not found: " + traineeRequest.getCohortName());
        }

        return traineeRepository.save(trainee);

    } catch (Exception e) {
        throw new RuntimeException("Failed to add trainee: " + e.getMessage(), e);
    }
}


    @Transactional(readOnly = true)
    public Trainee getTraineeById(Long id) {
        return traineeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found with id " + id));
    }

    @Transactional(readOnly = true)
    public List<Trainee> getAllTrainees() {
        return traineeRepository.findAll();
    }
// update trainee
@Transactional
public Trainee partialUpdateTrainee(Long id, Map<String, Object> updates) {
    // Find the existing Trainee
    Trainee trainee = traineeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Trainee not found with id " + id));
    updates.forEach((fieldName, fieldValue) -> {
        switch (fieldName) {
            case "firstName":
                trainee.setFirstName((String) fieldValue);
                break;
            case "lastName":
                trainee.setLastName((String) fieldValue);
                break;
            case "email":
                trainee.setEmail((String) fieldValue);
                break;
            case "country":
                trainee.setCountry((String) fieldValue);
                break;
            case "university":
                trainee.setUniversity((String) fieldValue);
                break;
            case "dateOfBirth":
                trainee.setDateOfBirth(LocalDate.parse((String) fieldValue));
                break;
            case "status":
                trainee.setIsActive((Boolean) fieldValue);
                break;
            case "specializationName":
                Specialization specialization = specializationRepository
                        .findByName((String) fieldValue)
                        .orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));
                trainee.setSpecialization(specialization);
                break;
            case "cohortName":
                Cohort cohort = cohortRepository
                        .findByName((String) fieldValue)
                        .orElseThrow(() -> new ResourceNotFoundException("Cohort not found"));
                trainee.setCohort(cohort);
                break;
            case "address":
                trainee.setAddress((String) fieldValue);
                break;
            case "enrollmentDate":
                trainee.setEnrollmentDate(LocalDate.parse((String) fieldValue));
                break;
            case "trainingId":
                trainee.setTrainingId((String) fieldValue);
                break;
            default:
                throw new IllegalArgumentException("Invalid field: " + fieldName);
        }
    });

    return traineeRepository.save(trainee);
}


    // delete trainee
    @Transactional
    public void deleteTrainee(Long id) {
        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found with id " + id));
        traineeRepository.delete(trainee);
    }

}
