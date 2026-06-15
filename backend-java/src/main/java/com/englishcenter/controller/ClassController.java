package com.englishcenter.controller;

import com.englishcenter.dto.ApiResponse;
import com.englishcenter.dto.PaginatedResponse;
import com.englishcenter.entity.ClassEntity;
import com.englishcenter.entity.User;
import com.englishcenter.enums.ClassStatus;
import com.englishcenter.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) Integer year,
            @RequestParam(name = "age_group", required = false) Integer ageGroup,
            @RequestParam(required = false) String status,
            @RequestParam(name = "teacher_id", required = false) Long teacherId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        ClassStatus statusEnum = status != null ? ClassStatus.valueOf(status) : null;
        Map<String, Object> result = classService.getClasses(year, ageGroup, statusEnum, teacherId, page, limit);
        return ResponseEntity.ok(PaginatedResponse.of(result.get("classes"), (Long) result.get("total"), page, limit));
    }

    @GetMapping("/my-classes")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMyClasses(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(classService.getClassesByTeacher(user.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassEntity>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(classService.getClassById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClassEntity>> create(@RequestBody Map<String, Object> body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(classService.createClass(body), "Tạo lớp thành công."));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClassEntity>> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(ApiResponse.success(classService.updateClass(id, body), "Cập nhật lớp thành công."));
    }

    @PutMapping("/{id}/close")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClassEntity>> close(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(classService.closeClass(id), "Đã đóng lớp thành công."));
    }

    @PutMapping("/{id}/reopen")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClassEntity>> reopen(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(classService.reopenClass(id), "Đã mở lại lớp."));
    }
}
