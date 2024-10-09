package com.assessment_management.assessment_management_service.repository;

import com.assessment_management.assessment_management_service.model.Assessment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentRepository extends MongoRepository<Assessment, String> {
    // You can define custom queries here if necessary
}
