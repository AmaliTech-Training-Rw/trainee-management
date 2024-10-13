package com.trainee_management.trainee_management_service.repository;

import com.trainee_management.trainee_management_service.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Optional<Trainee> findByEmail(String email);
}