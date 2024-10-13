package com.trainee_management.trainee_management_service.model;



import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    @JsonBackReference
    private Trainee trainee;

    private LocalDate date;

    @Enumerated(EnumType.STRING) // Store the enum as a string in the database
    private AttendanceStatus status;

}