package com.trainee_management.trainee_management_service.service;

import com.trainee_management.trainee_management_service.model.Attendance;
import com.trainee_management.trainee_management_service.model.AttendanceStatus;
import com.trainee_management.trainee_management_service.model.Trainee;
import com.trainee_management.trainee_management_service.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;
@Transactional
    public Attendance markAttendance(Trainee trainee, LocalDate date, AttendanceStatus status) {
        Attendance attendance = new Attendance();
        attendance.setTrainee(trainee);
        attendance.setDate(date);
        attendance.setStatus(status);
        return attendanceRepository.save(attendance);
    }
@Transactional(readOnly = true)
    public List<Attendance> getAttendanceForTrainee(Trainee trainee) {
        return attendanceRepository.findByTrainee(trainee);
    }
@Transactional
    public void updateAttendance(Long attendanceId, AttendanceStatus status) {
        Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow(() -> new RuntimeException("Attendance not found"));
        attendance.setStatus(status);
        attendanceRepository.save(attendance);
    }
@Transactional
    public double calculateAttendancePercentage(Trainee trainee) {
        List<Attendance> attendances = getAttendanceForTrainee(trainee);
        long presentCount = attendances.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        return (double) presentCount / attendances.size() * 100;
    }
@Transactional(readOnly = true)
    public List<Attendance> getAttendanceInDateRange(Trainee trainee, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByTraineeAndDateBetween(trainee, startDate, endDate);
    }
}
