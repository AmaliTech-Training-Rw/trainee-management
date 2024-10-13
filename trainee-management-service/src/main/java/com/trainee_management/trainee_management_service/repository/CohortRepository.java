package com.trainee_management.trainee_management_service.repository;

import com.trainee_management.trainee_management_service.model.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CohortRepository extends JpaRepository<Cohort, Long> {
    Optional<Cohort> findByName(String name);
}
