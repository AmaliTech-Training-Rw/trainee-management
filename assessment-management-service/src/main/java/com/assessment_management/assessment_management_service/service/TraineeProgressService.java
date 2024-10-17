package com.assessment_management.assessment_management_service.service;

import com.assessment_management.assessment_management_service.model.CurrentPhase;
import com.assessment_management.assessment_management_service.model.TraineeProgress;
import com.assessment_management.assessment_management_service.repository.TraineeProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TraineeProgressService {

    @Autowired
    private TraineeProgressRepository traineeProgressRepository;


    public ResponseEntity<List<TraineeProgress>> getAllProgress() {
        List<TraineeProgress> progressList = traineeProgressRepository.findAll();
        return new ResponseEntity<>(progressList, HttpStatus.OK);  // Return OK status
    }

    public ResponseEntity<TraineeProgress> getProgressById(String id) {
        Optional<TraineeProgress> traineeProgress = traineeProgressRepository.findById(id);
        if (traineeProgress.isPresent()) {
            return new ResponseEntity<>(traineeProgress.get(), HttpStatus.OK);  // Return OK status
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 Not Found if the progress doesn't exist
        }
    }

//    public ResponseEntity<List<TraineeProgress>> getProgressByTraineeName(String traineeName) {
//        List<TraineeProgress> progressList = traineeProgressRepository.findByTraineeName(traineeName);
//        return new ResponseEntity<>(progressList, HttpStatus.OK);  // Return OK status
//    }

    public ResponseEntity<TraineeProgress> saveProgress(TraineeProgress traineeProgress) {
        // Automatically set the progressIndicator based on the currentPhase
        traineeProgress.setProgressIndicator(getProgressIndicatorByPhase(traineeProgress.getCurrentPhase()));
        TraineeProgress savedProgress = traineeProgressRepository.save(traineeProgress);
        return new ResponseEntity<>(savedProgress, HttpStatus.CREATED);  // Return 201 Created status
    }

    public ResponseEntity<TraineeProgress> updateProgress(String id, TraineeProgress updatedProgress) {
        Optional<TraineeProgress> existingProgressOpt = traineeProgressRepository.findById(id);
        if (existingProgressOpt.isPresent()) {
            TraineeProgress existingProgress = existingProgressOpt.get();

            // Update only the currentPhase if provided
            if (updatedProgress.getCurrentPhase() != null) {
                existingProgress.setCurrentPhase(updatedProgress.getCurrentPhase());
                // Automatically update the progressIndicator based on the new currentPhase
                existingProgress.setProgressIndicator(getProgressIndicatorByPhase(updatedProgress.getCurrentPhase()));


            }

            TraineeProgress updated = traineeProgressRepository.save(existingProgress);
            return new ResponseEntity<>(updated, HttpStatus.OK);  // Return 200 OK status
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if the progress with the given id doesn't exist
        }
    }



    public ResponseEntity<Void> deleteProgress(String id) {
        if (traineeProgressRepository.existsById(id)) {
            traineeProgressRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Return 204 No Content status on successful deletion
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if the progress with the given id doesn't exist
        }
    }

    // New method to retrieve multiple progress records by IDs
    public ResponseEntity<List<TraineeProgress>> getMultipleProgressByIds(List<String> ids) {
        List<TraineeProgress> progressList = new ArrayList<>();

        for (String id : ids) {
            Optional<TraineeProgress> traineeProgress = traineeProgressRepository.findById(id);
            traineeProgress.ifPresent(progressList::add);
        }

        return new ResponseEntity<>(progressList, HttpStatus.OK);
    }
    // Helper method to set progressIndicator based on the currentPhase
    private String getProgressIndicatorByPhase(CurrentPhase phase) {
        switch (phase) {
            case Foundation:
                return "Phase 1";
            case Advanced:
                return "Phase 2";
            case Capstone:
                return "Phase 3";
            default:
                return "Unknown";  // Fallback in case the phase is not recognized
        }
    }
}
