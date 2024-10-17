package com.assessment_management.assessment_management_service.controller;

import com.assessment_management.assessment_management_service.model.Assessment;
import com.assessment_management.assessment_management_service.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    // Create a new assessment
    @PostMapping("/create")
    public ResponseEntity<?> createAssessment(@RequestBody Assessment assessment) {
        return assessmentService.createAssessment(assessment);
    }

    // Get a specific assessment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Assessment> getAssessment(@PathVariable String id) {
        return assessmentService.getAssessment(id);
    }

    // Get all assessments
    @GetMapping("/all")
    public ResponseEntity<List<Assessment>> getAllAssessments() {
        return assessmentService.getAllAssessments();
    }

    // Update an assessment by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssessment(@PathVariable String id, @RequestBody Assessment updatedAssessment) {
        return assessmentService.updateAssessment(id, updatedAssessment);
    }

    // Delete an assessment by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable String id) {
        return assessmentService.deleteAssessment(id);
    }
}