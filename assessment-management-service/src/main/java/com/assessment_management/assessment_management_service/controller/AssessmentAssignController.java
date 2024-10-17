package com.assessment_management.assessment_management_service.controller;

import com.assessment_management.assessment_management_service.model.AssessmentAssignment;
import com.assessment_management.assessment_management_service.model.CohortsIdsRequest;
import com.assessment_management.assessment_management_service.model.TraineeIdsRequest;
import com.assessment_management.assessment_management_service.service.AssessmentAssignService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessments/assign")
public class AssessmentAssignController {

    @Autowired
    private AssessmentAssignService assessmentAssignService;

    // Assign assessment to trainees
    @PutMapping("/{assessmentId}")
    public ResponseEntity<?> assignAssessment(
            @PathVariable String assessmentId,
            @RequestBody TraineeIdsRequest traineeIdsRequest,
            HttpServletRequest request) {
        return assessmentAssignService.assignAssessment(assessmentId, traineeIdsRequest.getTraineeIds(), request);
    }

    // Assign cohorts to an existing assignment
    @PutMapping("/cohorts/{assignmentId}")
    public ResponseEntity<?> addCohortsToAssignment(
            @PathVariable String assignmentId,
            @RequestBody CohortsIdsRequest cohortsIdsRequest) {
        return assessmentAssignService.addCohortsToAssignment(assignmentId, cohortsIdsRequest.getCohortsIds());
    }


}

