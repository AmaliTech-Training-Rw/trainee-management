package com.assessment_management.assessment_management_service.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthMessage {
    private String userId;
    private String token;
    private String name;

}

