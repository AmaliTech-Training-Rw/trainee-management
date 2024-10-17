package com.assessment_management.assessment_management_service.repository;

import com.assessment_management.assessment_management_service.model.Grade;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends MongoRepository<Grade, String> {
    // Custom query methods can be added here if needed
}
