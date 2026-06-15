package com.englishcenter.controller;

import com.englishcenter.dto.ApiResponse;
import com.englishcenter.entity.Payment;
import com.englishcenter.entity.TeacherPayment;
import com.englishcenter.entity.User;
import com.englishcenter.service.PaymentService;
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
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Payment>> recordPayment(@RequestBody Map<String, Object> body, @AuthenticationPrincipal User user) {
        Long enrollmentId = Long.valueOf(body.get("enrollment_id").toString());
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        Integer month = (Integer) body.get("month");
        Integer year = (Integer) body.get("year");
        String note = (String) body.get("note");
        Payment payment = paymentService.recordPayment(enrollmentId, amount, month, year, user.getId(), note);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(payment, "Ghi nhận thanh toán thành công."));
    }

    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentsByEnrollment(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.getPaymentsByEnrollment(enrollmentId)));
    }

    @GetMapping("/balance/{enrollmentId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOutstandingBalance(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.getOutstandingBalance(enrollmentId), "Thông tin công nợ."));
    }

    @GetMapping("/report")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFinanceReport(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer quarter,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.getFinanceReport(period, month, quarter, year, startDate, endDate), "Báo cáo tài chính."));
    }

    @PostMapping("/teacher")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherPayment>> recordTeacherPayment(@RequestBody Map<String, Object> body) {
        Long teacherId = Long.valueOf(body.get("teacher_id").toString());
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        Integer month = (Integer) body.get("month");
        Integer year = (Integer) body.get("year");
        Long classId = body.containsKey("class_id") && body.get("class_id") != null ? Long.valueOf(body.get("class_id").toString()) : null;
        Integer sessionsCount = body.containsKey("sessions_count") ? (Integer) body.get("sessions_count") : null;
        String note = (String) body.get("note");
        TeacherPayment payment = paymentService.recordTeacherPayment(teacherId, amount, month, year, classId, sessionsCount, note);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(payment, "Ghi nhận lương giáo viên thành công."));
    }

    @GetMapping("/teacher")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<TeacherPayment>>> getTeacherPayments(
            @RequestParam(name = "teacher_id", required = false) Long teacherId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.getTeacherPayments(teacherId, month, year)));
    }
}
