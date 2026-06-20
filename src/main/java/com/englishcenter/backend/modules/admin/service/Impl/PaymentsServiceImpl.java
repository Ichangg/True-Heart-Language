package com.englishcenter.backend.modules.admin.service.Impl;
import com.englishcenter.backend.core.repository.FeeRecordsRepository;
import com.englishcenter.backend.core.entity.FeeRecords;

import com.englishcenter.backend.core.entity.Payments;
import com.englishcenter.backend.core.repository.PaymentsRepository;
import com.englishcenter.backend.modules.admin.service.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentsServiceImpl implements PaymentsService {

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Override
    public List<Payments> getPaymentsByFeeRecord(Long feeRecordId) {
        return paymentsRepository.findByFeeRecordId(feeRecordId);
    }

    @Autowired
    private com.englishcenter.backend.core.repository.FeeRecordsRepository feeRecordsRepository;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Payments processPayment(Payments payment) {
        // 1. Lưu giao dịch thanh toán
        Payments savedPayment = paymentsRepository.save(payment);
        
        // 2. Lấy hồ sơ học phí tương ứng
        com.englishcenter.backend.core.entity.FeeRecords feeRecord = feeRecordsRepository.findById(payment.getFeeRecordId())
            .orElseThrow(() -> new RuntimeException("Fee Record not found"));
            
        // 3. Cộng dồn số tiền đã đóng
        java.math.BigDecimal newPaidAmount = feeRecord.getPaidAmount().add(payment.getAmount());
        feeRecord.setPaidAmount(newPaidAmount);
        
        // 4. Cập nhật trạng thái
        if (newPaidAmount.compareTo(feeRecord.getFeeFinal()) >= 0) {
            feeRecord.setStatus("paid");
        } else {
            feeRecord.setStatus("partial");
        }
        
        // 5. Lưu lại hồ sơ học phí
        feeRecordsRepository.save(feeRecord);
        
        return savedPayment;
    }
}