package com.englishcenter.backend.service;

import com.englishcenter.backend.entity.FeeRecords;
import java.util.List;
import java.util.Map;

public interface FeeRecordsService {
    List<FeeRecords> getFeesByStudent(Long studentId);
    Map<String, Object> getStudentDebtSummary(Long studentId);
    FeeRecords createFeeRecord(FeeRecords feeRecord);
}