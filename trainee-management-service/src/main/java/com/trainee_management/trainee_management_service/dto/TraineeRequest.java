package com.trainee_management.trainee_management_service.dto;

import com.trainee_management.trainee_management_service.model.Trainee;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TraineeRequest {
    @NotNull(message = "First name is required")
    private String firstName;

    @NotNull(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private String gender;

    @NotNull(message = "Country is required")
    private String country;

    @NotNull(message = "Address is required")
    private String address;

    @NotNull(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "University is required")
    private String university;

    @NotNull(message = "Status is required")
    private Boolean status;

    @NotNull(message = "Enrollment date is required")
    private LocalDate enrollmentDate;

    @NotNull(message = "Specialization name is required")
    private String specializationName;

    @NotNull(message = "Cohort name is required")
    private String cohortName;

    private String photoUrl;

    @NotNull(message = "Training ID is required")
    private String trainingId;

    /**
     * Maps the fields of this DTO to a Trainee entity.
     * The Specialization and Cohort will be set separately in the service layer.
     */
    public Trainee toTrainee() {
        Trainee trainee = new Trainee();
        trainee.setFirstName(this.firstName);
        trainee.setLastName(this.lastName);
        trainee.setEmail(this.email);
        trainee.setDateOfBirth(this.dateOfBirth);
        trainee.setGender(Trainee.Gender.valueOf(this.gender.toUpperCase()));
        trainee.setCountry(this.country);
        trainee.setAddress(this.address);
        trainee.setPhoneNumber(this.phoneNumber);
        trainee.setUniversity(this.university);
        trainee.setIsActive(this.status);
        trainee.setEnrollmentDate(this.enrollmentDate);
        trainee.setTrainingId(this.trainingId);
        // Specialization and Cohort will be set later in the service
        return trainee;
    }
}
