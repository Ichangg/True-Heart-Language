package com.englishcenter.backend.controller;

import com.englishcenter.backend.entity.Sessions;
import com.englishcenter.backend.service.SessionsService; // Corrected import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionsController {

    @Autowired
    private SessionsService sessionsService;

    // Lấy toàn bộ danh sách các buổi học của một lớp cụ thể
    // Dùng cho màn hình Lịch học của Lớp
    @GetMapping("/class/{classId}")
    public List<Sessions> getSessionsByClass(@PathVariable Long classId) {
        return sessionsService.getSessionsByClass(classId);
    }

    // Giáo viên xem các buổi mình dạy
    @GetMapping("/teacher/{teacherId}")
    public List<Sessions> getSessionsByTeacher(@PathVariable Long teacherId) {
        return sessionsService.getSessionsByTeacher(teacherId);
    }

    // Tạo mới một buổi học (hoặc tạo tự động từ thời khóa biểu)
    @PostMapping
    public Sessions createSession(@RequestBody Sessions session) {
        return sessionsService.createSession(session);
    }
}