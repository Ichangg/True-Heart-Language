package com.englishcenter.backend.modules.student.controller;

import com.englishcenter.backend.core.entity.ParentStudent;
import com.englishcenter.backend.core.entity.Attendances;
import com.englishcenter.backend.modules.student.service.ParentStudentService; // Corrected import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parent-student")
public class ParentStudentController {

    @Autowired
    private ParentStudentService parentStudentService;

    // Lấy danh sách các học sinh (con cái) của một phụ huynh cụ thể
    // Dùng để phụ huynh đăng nhập vào App và xem thông tin của các con
    @GetMapping("/parent/{parentId}")
    public List<ParentStudent> getStudentsByParent(@PathVariable Long parentId) {
        return parentStudentService.getStudentsByParent(parentId);
    }

    // Phụ huynh xem lịch sử điểm danh của con
    @GetMapping("/student/{studentId}/attendance")
    public List<Attendances> getChildAttendance(@PathVariable Long studentId) {
        return parentStudentService.getChildAttendance(studentId);
    }

    // Liên kết tài khoản phụ huynh với tài khoản học sinh
    @PostMapping
    public ParentStudent linkParentAndStudent(@RequestBody ParentStudent parentStudent) {
        return parentStudentService.linkParentAndStudent(parentStudent);
    }
}