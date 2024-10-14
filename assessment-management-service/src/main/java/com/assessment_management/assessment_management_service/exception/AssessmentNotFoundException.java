package com.assessment_management.assessment_management_service.exception;

// Custom exception for Assessment not found
public class AssessmentNotFoundException extends RuntimeException {
    public AssessmentNotFoundException(String message) {
        super(message); // Pass the message to the superclass
    }
}
