package com.trainee_management.trainee_management_service.repository;

import com.trainee_management.trainee_management_service.model.Specialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    Optional<Specialization> findByName(String name);
    @EntityGraph(attributePaths = "trainees")
    Page<Specialization> findAll(Pageable pageable);
}
