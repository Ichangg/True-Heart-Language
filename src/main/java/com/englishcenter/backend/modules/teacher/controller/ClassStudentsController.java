package com.englishcenter.backend.modules.teacher.controller;

import com.englishcenter.backend.core.entity.ClassStudents;
import com.englishcenter.backend.modules.teacher.service.ClassStudentsService; // Corrected import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class-students")
public class ClassStudentsController {

    @Autowired
    private ClassStudentsService classStudentsService;

    // Lấy danh sách toàn bộ học sinh đang nằm trong một lớp học (Sĩ số lớp)
    @GetMapping("/class/{classId}")
    public List<ClassStudents> getStudentsInClass(@PathVariable Long classId) {
        return classStudentsService.getStudentsInClass(classId);
    }

    // Thêm một học sinh vào lớp học (Quá trình xếp lớp)
    @PostMapping
    public ClassStudents assignStudentToClass(@RequestBody ClassStudents classStudent) {
        return classStudentsService.assignStudentToClass(classStudent);
    }
}