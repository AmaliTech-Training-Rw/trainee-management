package com.curriculumproject.curriculum.repository;

import com.curriculumproject.curriculum.module.ModuleCreate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleCreateRepository extends JpaRepository<ModuleCreate, Long> {
}
