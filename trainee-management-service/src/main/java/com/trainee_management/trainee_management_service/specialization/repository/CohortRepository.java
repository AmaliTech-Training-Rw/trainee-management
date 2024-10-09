package com.trainee_management.trainee_management_service.specialization.repository;

import com.trainee_management.trainee_management_service.specialization.model.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CohortRepository extends JpaRepository<Cohort, Long> {
}
