package com.assessment_management.assessment_management_service.repository;

import com.assessment_management.assessment_management_service.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionRepository extends MongoRepository<Question, String> {
    List<Question> findAllById(Iterable<String> ids);
}
