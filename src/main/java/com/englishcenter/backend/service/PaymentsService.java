package com.englishcenter.backend.service;

import com.englishcenter.backend.entity.Payments;
import java.util.List;

public interface PaymentsService {
    List<Payments> getPaymentsByFeeRecord(Long feeRecordId);
    Payments processPayment(Payments payment);
}