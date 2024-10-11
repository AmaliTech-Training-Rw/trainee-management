package com.trainee_management.trainee_management_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Trainee {

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String country;
    private String address;
    private String phoneNumber;
    private String university;
    private String photoUrl;
    private Boolean isActive;
    private LocalDate enrollmentDate;
    private String trainingId;


    @ManyToOne
    @JoinColumn(name = "specialization_id")  // Ensure this matches your database
    private Specialization specialization;

    @ManyToOne
    @JoinColumn(name = "cohort_id")  // Ensure this matches your database
    private Cohort cohort;


}
