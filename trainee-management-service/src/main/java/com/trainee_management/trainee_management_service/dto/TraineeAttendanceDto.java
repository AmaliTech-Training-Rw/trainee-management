package com.trainee_management.trainee_management_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeAttendanceDto {
    private Long traineeId;
    private String firstName;
    private String lastName;
    private List<AttendanceDto> attendances; // List of individual attendance records
}

