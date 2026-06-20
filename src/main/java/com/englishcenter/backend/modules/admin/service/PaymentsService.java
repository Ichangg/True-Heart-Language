package com.englishcenter.backend.modules.admin.service;

import com.englishcenter.backend.core.entity.Payments;
import java.util.List;

public interface PaymentsService {
    List<Payments> getPaymentsByFeeRecord(Long feeRecordId);
    Payments processPayment(Payments payment);
}