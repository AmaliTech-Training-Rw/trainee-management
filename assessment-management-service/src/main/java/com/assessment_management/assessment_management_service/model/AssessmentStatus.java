package com.assessment_management.assessment_management_service.model;

public enum AssessmentStatus {
    UNGRADED, GRADED, SUBMITTED , EMPTY;

    public boolean isEmpty() {

        return this == EMPTY;
    }
}
