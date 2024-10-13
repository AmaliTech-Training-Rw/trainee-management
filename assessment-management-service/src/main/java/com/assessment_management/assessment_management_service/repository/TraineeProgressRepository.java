package com.assessment_management.assessment_management_service.repository;

import com.assessment_management.assessment_management_service.model.TraineeProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TraineeProgressRepository extends MongoRepository<TraineeProgress, String> {
    List<TraineeProgress> findByTraineeName(String traineeName);
}
