package com.assessment_management.assessment_management_service.controller;

import com.assessment_management.assessment_management_service.model.Assessment;
import com.assessment_management.assessment_management_service.model.Grade;
import com.assessment_management.assessment_management_service.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessments/grade")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    // Endpoint to create a grade for a specific assessment
    @PostMapping("/{assessmentId}")
    public ResponseEntity<Grade> createGrade(
            @PathVariable String assessmentId,
            @RequestBody Grade grade) {
        return gradeService.createGrade(assessmentId, grade);
    }

}
