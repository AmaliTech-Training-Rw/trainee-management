package com.assessment_management.assessment_management_service.repository;

import com.assessment_management.assessment_management_service.model.Assessment;
import com.assessment_management.assessment_management_service.model.AssessmentStatus;
import com.assessment_management.assessment_management_service.model.AssessmentType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends MongoRepository<Assessment, String> {
    // You can define custom queries here if necessary
    List<Assessment> findByType(AssessmentType type);
    // New method to find by status
    List<Assessment> findByStatus(AssessmentStatus status);
}
