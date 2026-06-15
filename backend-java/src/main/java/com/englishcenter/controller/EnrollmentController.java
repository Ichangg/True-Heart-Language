package com.englishcenter.controller;

import com.englishcenter.dto.ApiResponse;
import com.englishcenter.entity.Enrollment;
import com.englishcenter.entity.User;
import com.englishcenter.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Enrollment>> enroll(@RequestBody Map<String, Object> body) {
        Long studentId = Long.valueOf(body.get("student_id").toString());
        Long classId = Long.valueOf(body.get("class_id").toString());
        BigDecimal discountPercent = body.containsKey("discount_percent") ? new BigDecimal(body.get("discount_percent").toString()) : null;
        String discountReason = (String) body.get("discount_reason");
        BigDecimal customFee = body.containsKey("custom_fee") && body.get("custom_fee") != null ? new BigDecimal(body.get("custom_fee").toString()) : null;
        String note = (String) body.get("note");

        Enrollment enrollment = enrollmentService.enrollStudent(studentId, classId, discountPercent, discountReason, customFee, note);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(enrollment, "Đăng ký học sinh vào lớp thành công."));
    }

    @PutMapping("/{id}/discount")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Enrollment>> updateDiscount(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        BigDecimal discountPercent = body.containsKey("discount_percent") ? new BigDecimal(body.get("discount_percent").toString()) : null;
        String discountReason = (String) body.get("discount_reason");
        BigDecimal customFee = body.containsKey("custom_fee") && body.get("custom_fee") != null ? new BigDecimal(body.get("custom_fee").toString()) : null;
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.updateDiscount(id, discountPercent, discountReason, customFee), "Cập nhật giảm giá thành công."));
    }

    @GetMapping("/class/{classId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.getEnrollmentsByClass(classId)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.getEnrollmentsByStudent(studentId)));
    }

    @GetMapping("/my-classes")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getMyClasses(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.getEnrollmentsByStudent(user.getId())));
    }

    @PutMapping("/{id}/withdraw")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Enrollment>> withdraw(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.withdrawStudent(id), "Đã rút học sinh khỏi lớp."));
    }
}
