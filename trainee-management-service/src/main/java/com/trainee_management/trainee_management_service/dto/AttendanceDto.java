package com.trainee_management.trainee_management_service.dto;

import com.trainee_management.trainee_management_service.model.AttendanceStatus;
import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDto {
    private Long traineeId;
    private LocalDate date;
    private AttendanceStatus status;


}
