package com.englishcenter.backend.controller;

import com.englishcenter.backend.entity.Payments;
import com.englishcenter.backend.service.PaymentsService; // Corrected import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;

    // Xem chi tiết các lần thanh toán của một Hóa đơn học phí
    // (Vì học sinh có thể đóng học phí chia làm nhiều đợt)
    @GetMapping("/fee-record/{feeRecordId}")
    public List<Payments> getPaymentsByFeeRecord(@PathVariable Long feeRecordId) {
        return paymentsService.getPaymentsByFeeRecord(feeRecordId);
    }

    // Ghi nhận một giao dịch thanh toán mới (Chuyển khoản, tiền mặt...)
    @PostMapping
    public Payments processPayment(@RequestBody Payments payment) {
        return paymentsService.processPayment(payment);
    }
}