package com.trainee_management.trainee_management_service.repository;

import com.trainee_management.trainee_management_service.model.Attendance;
import com.trainee_management.trainee_management_service.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByTrainee(Trainee trainee);
    List<Attendance> findByTraineeAndDateBetween(Trainee trainee, LocalDate startDate, LocalDate endDate);
}
