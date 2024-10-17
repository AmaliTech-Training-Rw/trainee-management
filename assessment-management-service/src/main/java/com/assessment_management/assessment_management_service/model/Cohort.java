package com.assessment_management.assessment_management_service.model;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cohort {

    private Long cohortId; // Assuming you want to include this for identification

    @NotNull
    private String name;

    @NotNull
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    private LocalDate endDate;
    

}
