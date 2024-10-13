package com.curriculumproject.curriculum.controller;

import com.curriculumproject.curriculum.dto.CurriculumDto;
import com.curriculumproject.curriculum.module.Curriculum;
import com.curriculumproject.curriculum.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/curriculum")
public class CurriculumController {

    @Autowired
    private CurriculumService curriculumService;

    // Create a new Curriculum
    @PostMapping("/create")
    public ResponseEntity<Curriculum> createCurriculum(@ModelAttribute CurriculumDto dto) {
        Curriculum curriculum = curriculumService.createCurriculum(dto);
        return ResponseEntity.ok(curriculum);
    }

    // Get all Curriculums
    @GetMapping
    public ResponseEntity<List<Curriculum>> getAllCurriculums() {
        List<Curriculum> curriculums = curriculumService.getAllCurriculums();
        return ResponseEntity.ok(curriculums);
    }

    // Get Curriculum by ID
    @GetMapping("/{id}")
    public ResponseEntity<Curriculum> getCurriculumById(@PathVariable Long id) {
        Curriculum curriculum = curriculumService.getCurriculumById(id);
        return ResponseEntity.ok(curriculum);
    }

    // Update Curriculum
    @PutMapping("/{id}")
    public ResponseEntity<Curriculum> updateCurriculum(@PathVariable Long id, @ModelAttribute CurriculumDto dto) {
        Curriculum updatedCurriculum = curriculumService.updateCurriculum(id, dto);
        return ResponseEntity.ok(updatedCurriculum);
    }

    // Delete Curriculum
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurriculum(@PathVariable Long id) {
        curriculumService.deleteCurriculum(id);
        return ResponseEntity.noContent().build();
    }
}
