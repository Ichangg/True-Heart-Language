package com.englishcenter.backend.modules.admin.service;

import com.englishcenter.backend.core.entity.FeeRecords;
import java.util.List;
import java.util.Map;

public interface FeeRecordsService {
    List<FeeRecords> getFeesByStudent(Long studentId);
    Map<String, Object> getStudentDebtSummary(Long studentId);
    FeeRecords createFeeRecord(FeeRecords feeRecord);
    void generateMonthlyFees(Long classId, java.time.LocalDate month);
}