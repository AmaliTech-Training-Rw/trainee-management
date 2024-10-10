package com.user_management.user_management_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetEmailRequest {
    private String email;
    private String token;

    // Constructors, Getters, and Setters
}