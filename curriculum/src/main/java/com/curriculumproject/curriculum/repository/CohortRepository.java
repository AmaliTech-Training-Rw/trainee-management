package com.curriculumproject.curriculum.repository;


import com.curriculumproject.curriculum.module.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CohortRepository extends JpaRepository<Cohort, Long> {
}
