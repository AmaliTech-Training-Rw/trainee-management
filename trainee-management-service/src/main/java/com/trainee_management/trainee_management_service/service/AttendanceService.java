package com.trainee_management.trainee_management_service.service;

import com.trainee_management.trainee_management_service.dto.AttendanceDto;
import com.trainee_management.trainee_management_service.model.Attendance;
import com.trainee_management.trainee_management_service.model.AttendanceStatus;
import com.trainee_management.trainee_management_service.model.Trainee;
import com.trainee_management.trainee_management_service.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (attendances.isEmpty()) {
            return 0.0; // Return 0% if there are no attendance records
        }

        long presentCount = attendances.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT)
                .count();
        return (double) presentCount / attendances.size() * 100;
    }


    @Transactional(readOnly = true)
    public Map<String, List<AttendanceDto>> getAttendanceInDateRange(LocalDate startDate, LocalDate endDate) {
        // Retrieve all attendance records within the specified date range
        List<Attendance> attendances = attendanceRepository.findByDateBetween(startDate, endDate);
        Map<String, List<AttendanceDto>> traineeAttendanceMap = new HashMap<>();
        for (Attendance attendance : attendances) {
            Trainee trainee = attendance.getTrainee();
            String fullName = trainee.getFirstName() + " " + trainee.getLastName();
            AttendanceDto dto = new AttendanceDto(
                    trainee.getId(),
                    attendance.getDate(),
                    attendance.getStatus()
            );

            traineeAttendanceMap.computeIfAbsent(fullName, k -> new ArrayList<>()).add(dto);
        }

        return traineeAttendanceMap;
    }

}
