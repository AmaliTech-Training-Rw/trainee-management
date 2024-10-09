package com.trainee_management.trainee_management_service.specialization.repository;

import com.trainee_management.trainee_management_service.specialization.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
}