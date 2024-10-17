package com.trainee_management.trainee_management_service.controller;

import com.trainee_management.trainee_management_service.dto.AttendanceDto;
import com.trainee_management.trainee_management_service.model.Attendance;
import com.trainee_management.trainee_management_service.model.AttendanceStatus;
import com.trainee_management.trainee_management_service.model.Trainee;
import com.trainee_management.trainee_management_service.service.AttendanceService;
import com.trainee_management.trainee_management_service.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trainees/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private TraineeService traineeService;

    @PostMapping("/mark")
    public ResponseEntity<Attendance> markAttendance(@RequestBody AttendanceDto attendanceDto) {
        Trainee trainee = traineeService.getTraineeById(attendanceDto.getTraineeId());
        Attendance attendance = attendanceService.markAttendance(trainee, attendanceDto.getDate(), attendanceDto.getStatus());
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/{traineeId}")
    public ResponseEntity<List<Attendance>> getAttendanceForTrainee(@PathVariable Long traineeId) {
        Trainee trainee = traineeService.getTraineeById(traineeId);
        List<Attendance> attendanceRecords = attendanceService.getAttendanceForTrainee(trainee);
        return ResponseEntity.ok(attendanceRecords);
    }

    @PutMapping("/{attendanceId}")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Long attendanceId, @RequestParam AttendanceStatus status) {
        attendanceService.updateAttendance(attendanceId, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{traineeId}/percentage")
    public ResponseEntity<Double> getAttendancePercentage(@PathVariable Long traineeId) {
        Trainee trainee = traineeService.getTraineeById(traineeId);
        double percentage = attendanceService.calculateAttendancePercentage(trainee);
        return ResponseEntity.ok(percentage);
    }

    @GetMapping("/report")
    public ResponseEntity<Map<String, List<AttendanceDto>>> getAttendanceReport(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        // Fetch attendance data for all trainees in the given date range
        Map<String, List<AttendanceDto>> report = attendanceService.getAttendanceInDateRange(startDate, endDate);

        return ResponseEntity.ok(report);
    }

}
