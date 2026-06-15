package com.englishcenter.controller;

import com.englishcenter.dto.ApiResponse;
import com.englishcenter.entity.Attendance;
import com.englishcenter.entity.Session;
import com.englishcenter.entity.User;
import com.englishcenter.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/sessions/{classId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<Session>> createSession(@PathVariable Long classId, @RequestBody Map<String, Object> body) {
        LocalDate sessionDate = LocalDate.parse((String) body.get("session_date"));
        String topic = (String) body.get("topic");
        String note = (String) body.get("note");
        Session session = attendanceService.createSession(classId, sessionDate, topic, note);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(session, "Tạo buổi học thành công."));
    }

    @GetMapping("/sessions/{classId}")
    public ResponseEntity<ApiResponse<List<Session>>> getSessionsByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getSessionsByClass(classId)));
    }

    @PostMapping("/mark/{sessionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<ApiResponse<List<Attendance>>> markAttendance(
            @PathVariable Long sessionId,
            @RequestBody Map<String, Object> body,
            @AuthenticationPrincipal User user) {
        List<Map<String, Object>> attendanceData = (List<Map<String, Object>>) body.get("attendanceData");
        List<Attendance> results = attendanceService.markAttendance(sessionId, attendanceData, user.getId());
        return ResponseEntity.ok(ApiResponse.success(results, "Điểm danh thành công."));
    }

    @GetMapping("/class/{classId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<Session>>> getByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAttendanceByClass(classId)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAttendanceByStudent(studentId)));
    }

    @GetMapping("/my-attendance")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMyAttendance(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAttendanceByStudent(user.getId())));
    }
}
