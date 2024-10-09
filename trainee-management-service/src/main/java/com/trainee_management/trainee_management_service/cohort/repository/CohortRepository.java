package com.trainee_management.trainee_management_service.cohort.repository;

import com.trainee_management.trainee_management_service.cohort.model.CohortModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CohortRepository extends JpaRepository<CohortModel, Long> {
}
