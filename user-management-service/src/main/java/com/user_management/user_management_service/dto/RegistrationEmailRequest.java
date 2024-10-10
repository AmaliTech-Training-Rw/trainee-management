package com.user_management.user_management_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationEmailRequest {
    private String email;
    private String userName;
    private String resetToken;


}

