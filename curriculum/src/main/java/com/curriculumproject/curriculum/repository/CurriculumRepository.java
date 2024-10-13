package com.curriculumproject.curriculum.repository;




import com.curriculumproject.curriculum.module.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {
}
