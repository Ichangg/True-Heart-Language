package com.englishcenter.backend.controller;

import com.englishcenter.backend.entity.Classes;
import com.englishcenter.backend.service.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassesController {

    @Autowired
    private ClassesService classesService;

    // Lấy tất cả các lớp học hiện có trong hệ thống
    @GetMapping
    public List<Classes> getAllClasses() {
        return classesService.getAllClasses();
    }

    // Tìm các lớp học dựa trên trạng thái (Ví dụ: ACTIVE - Đang mở, COMPLETED - Đã kết thúc)
    @GetMapping("/status/{status}")
    public List<Classes> getClassesByStatus(@PathVariable String status) {
        return classesService.getClassesByStatus(status);
    }

    // Thêm một lớp học mới vào hệ thống cùng với phân bổ GV, HS
    @PostMapping
    public Classes createClass(@RequestBody com.englishcenter.backend.dto.ClassCreationDTO dto) {
        return classesService.createClassWithAllocations(dto);
    }
}