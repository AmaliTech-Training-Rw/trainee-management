package com.assessment_management.assessment_management_service.repository;

import com.assessment_management.assessment_management_service.model.Assessment;
import com.assessment_management.assessment_management_service.model.AssessmentStatus;
import com.assessment_management.assessment_management_service.model.Grade;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GradeRepository extends MongoRepository<Grade, String> {

}
