package com.assessment_management.assessment_management_service.controller;

import com.assessment_management.assessment_management_service.model.AnswerSubmission;
import com.assessment_management.assessment_management_service.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @PostMapping("/{assessmentId}")
    public ResponseEntity<String> submitAnswers(
            @PathVariable String assessmentId,
            @RequestBody AnswerSubmission submission) {
        String result = answerService.calculateScore(assessmentId, submission);
        return ResponseEntity.ok(result);
    }
}
