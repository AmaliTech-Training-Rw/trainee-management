package com.assessment_management.assessment_management_service.repository;

import com.assessment_management.assessment_management_service.model.AssessmentAssignment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentAssignmentRepository extends MongoRepository<AssessmentAssignment, String> {

    // Find all assignments by a specific assessment ID
    List<AssessmentAssignment> findByAssessmentId(String assessmentId);

    // Check if an assignment exists for a given assessment ID
    boolean existsByAssessmentId(String assessmentId);

    // Find all assignments for a specific trainee ID
    List<AssessmentAssignment> findByTraineeIdsContains(String traineeId);
}
