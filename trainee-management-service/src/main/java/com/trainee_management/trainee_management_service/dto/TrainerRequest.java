package com.trainee_management.trainee_management_service.dto;

import com.trainee_management.trainee_management_service.model.Trainer;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerRequest {
    @NotNull(message = "First name is required")
    private String firstName;

    @NotNull(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Gender is required")
    private String gender;

    @NotNull(message = "Country is required")
    private String country;

    @NotNull(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Specialization is required")
    private Long specializationId;

    public Trainer toTrainer() {
        Trainer trainer = new Trainer();
        trainer.setFirstName(this.firstName);
        trainer.setLastName(this.lastName);
        trainer.setEmail(this.email);
        trainer.setGender(Trainer.Gender.valueOf(this.gender.toUpperCase()));
        trainer.setCountry(this.country);
        trainer.setPhoneNumber(this.phoneNumber);
        // Specialization will be set separately
        return trainer;
    }
}