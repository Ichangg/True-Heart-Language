package com.englishcenter.backend.controller;

import com.englishcenter.backend.entity.FeeRecords;
import com.englishcenter.backend.service.FeeRecordsService;
import org.springframework.beans.factory.annotation.Autowired; // Corrected import
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fee-records")
public class FeeRecordsController {

    @Autowired
    private FeeRecordsService feeRecordsService;

    // Lấy danh sách tất cả các hóa đơn học phí của 1 học sinh cụ thể
    // Dùng cho màn hình Tài chính của phụ huynh/học sinh
    @GetMapping("/student/{studentId}")
    public List<FeeRecords> getFeesByStudent(@PathVariable Long studentId) {
        return feeRecordsService.getFeesByStudent(studentId);
    }

    // API quan trọng: Tính tổng tiền chưa đóng (Requirement của đề bài)
    @GetMapping("/student/{studentId}/debt-summary")
    public Map<String, Object> getStudentDebt(@PathVariable Long studentId) {
        return feeRecordsService.getStudentDebtSummary(studentId);
    }

    // Tạo một hóa đơn học phí mới (khi học sinh bắt đầu đăng ký khóa học)
    @PostMapping
    public FeeRecords createFeeRecord(@RequestBody FeeRecords feeRecord) {
        return feeRecordsService.createFeeRecord(feeRecord);
    }
    
    // API generate học phí hàng tháng cho cả lớp
    @PostMapping("/generate/{classId}")
    public org.springframework.http.ResponseEntity<?> generateFees(@PathVariable Long classId, @RequestParam String month) {
        // Assume month format is YYYY-MM-DD for simplicity
        try {
            ((com.englishcenter.backend.service.Impl.FeeRecordsServiceImpl) feeRecordsService)
                .generateMonthlyFees(classId, java.time.LocalDate.parse(month));
            return org.springframework.http.ResponseEntity.ok().build();
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}