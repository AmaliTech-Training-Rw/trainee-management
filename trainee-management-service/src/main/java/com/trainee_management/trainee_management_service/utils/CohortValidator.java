package com.trainee_management.trainee_management_service.utils;

import com.trainee_management.trainee_management_service.model.Cohort;

import java.time.LocalDate;

public class CohortValidator {

    // Validate cohort dates
    public static void validateCohortDates(Cohort cohort) {
        LocalDate startDate = cohort.getStartDate();
        LocalDate endDate = cohort.getEndDate();

        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }

        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before the start date");
        }
    }
}
