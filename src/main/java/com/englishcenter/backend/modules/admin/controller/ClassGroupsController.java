package com.englishcenter.backend.modules.admin.controller;

import com.englishcenter.backend.core.entity.ClassGroups;
import com.englishcenter.backend.modules.admin.service.ClassGroupsService; // Corrected import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class-groups")
public class ClassGroupsController {

    @Autowired
    private ClassGroupsService classGroupsService;

     // Tạo mới một khối lớp
    @PostMapping
    public ClassGroups createGroup(@RequestBody ClassGroups group) {
        return classGroupsService.createGroup(group);
    }

     // Xem chi tiết 1 khối
    @GetMapping("/{id}")
    public ClassGroups getGroupById(@PathVariable Long id) {
        return classGroupsService.getGroupById(id).orElse(null);
    }
    
    // Sửa thông tin khối lớp
    @PatchMapping("/{id}")
    public ClassGroups patchGroup(@PathVariable Long id, @RequestBody ClassGroups groupDetails) {
        return classGroupsService.patchGroup(id, groupDetails);
    }

     // Lấy danh sách tất cả các khối lớp (Ví dụ: Khối IELTS, Khối TOEIC, Khối Giao tiếp)
    @GetMapping
    public List<ClassGroups> getAllGroups() {
        return classGroupsService.getAllGroups();
    }

   

   

}