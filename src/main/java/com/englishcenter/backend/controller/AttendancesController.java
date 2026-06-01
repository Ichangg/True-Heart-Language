package com.englishcenter.backend.controller;

import com.englishcenter.backend.entity.Attendances;
import com.englishcenter.backend.service.AttendancesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
public class AttendancesController {

    @Autowired
    private AttendancesService attendancesService;

    // Lấy danh sách điểm danh của 1 buổi học cụ thể
    // Giúp giáo viên xem lại hôm đó ai vắng, ai có mặt
    @GetMapping("/session/{sessionId}")
    public List<Attendances> getAttendancesBySession(@PathVariable Long sessionId) {
        return attendancesService.getAttendancesBySession(sessionId);
    }

    // Lưu bản ghi điểm danh cho một học sinh trong một buổi học
    @PostMapping
    public Attendances markAttendance(@RequestBody Attendances attendance) {
        return attendancesService.markAttendance(attendance);
    }
    
    // Điểm danh hàng loạt cho cả lớp
    @PostMapping("/batch")
    public List<Attendances> markBatchAttendance(@RequestBody List<Attendances> attendances) {
        return attendancesService.markBatchAttendance(attendances);
    }
}