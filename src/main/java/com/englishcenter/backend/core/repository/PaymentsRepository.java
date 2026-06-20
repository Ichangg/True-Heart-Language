package com.englishcenter.backend.core.repository;

import com.englishcenter.backend.core.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Long> {
    // Tìm các giao dịch thanh toán gắn với một hóa đơn học phí
    List<Payments> findByFeeRecordId(Long feeRecordId);
    
    // Tìm các giao dịch theo phương thức thanh toán (CASH, BANK_TRANSFER, MOMO...)
    List<Payments> findByPaymentMethod(String paymentMethod);
}