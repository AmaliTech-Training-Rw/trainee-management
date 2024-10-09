package com.trainee_management.trainee_management_service.specialization.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Trainee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long traineeId;

    @NotNull
    private String name;

    @Email
    @NotNull
    private String email;

    @ManyToOne
    @JoinColumn(name = "specialization_id")  // Ensure this matches your database
    private Specialization specialization;

    @ManyToOne(cascade = CascadeType.PERSIST) // Cascade persist operation
    @JoinColumn(name = "cohort_id")  // Ensure this matches your database
    private Cohort cohort;

    @NotNull
    @Enumerated(EnumType.STRING) // Store the enum as a string in the database
    private ProgressionStatus progressionStatus;

    public enum ProgressionStatus {
        IN_PROGRESS,
        COMPLETED,
        DISCONTINUED
    }
}
