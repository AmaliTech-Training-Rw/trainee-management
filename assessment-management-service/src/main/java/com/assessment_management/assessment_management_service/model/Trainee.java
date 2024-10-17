package com.assessment_management.assessment_management_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainee {
    private String id; // or Integer if appropriate
    private String firstName;
    private String lastName;
    private String email;
    private String dateOfBirth;
    private String gender;
    private String country;
    private String address;
    private String phoneNumber;
    private String university;
    private String photoUrl;
    private boolean isActive;
    private String enrollmentDate;
    private String trainingId;
}
