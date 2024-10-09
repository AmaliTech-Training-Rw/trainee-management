package com.user_management.user_management_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class RoleRequest {
    @NotBlank(message = "Role name is required")
    private String Name;
    @NotBlank(message = "Role description is required")
    private String Description;
}
