package com.englishcenter.backend.controller;

import com.englishcenter.backend.entity.TeacherSalaries;
import com.englishcenter.backend.service.TeacherSalariesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher-salaries")
public class TeacherSalariesController {

    @Autowired
    private TeacherSalariesService teacherSalariesService;

    // Lấy lịch sử nhận lương của một giáo viên cụ thể
    @GetMapping("/teacher/{teacherId}")
    public List<TeacherSalaries> getSalariesByTeacher(@PathVariable Long teacherId) {
        return teacherSalariesService.getSalariesByTeacher(teacherId);
    }

    // Kế toán tạo bản ghi lương mới cho giáo viên vào cuối tháng
    @PostMapping
    public TeacherSalaries addSalaryRecord(@RequestBody TeacherSalaries salary) {
        return teacherSalariesService.addSalaryRecord(salary);
    }
    
    // API generate lương hàng tháng cho toàn bộ giáo viên
    @PostMapping("/generate")
    public org.springframework.http.ResponseEntity<?> generateSalaries(@RequestParam String month) {
        try {
            ((com.englishcenter.backend.service.Impl.TeacherSalariesServiceImpl) teacherSalariesService)
                .generateTeacherSalaries(java.time.LocalDate.parse(month));
            return org.springframework.http.ResponseEntity.ok().build();
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}