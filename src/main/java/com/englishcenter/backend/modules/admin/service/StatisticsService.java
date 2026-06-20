package com.englishcenter.backend.modules.admin.service;

import java.time.LocalDate;
import java.util.Map;

public interface StatisticsService {
    Map<String, Object> getFinancialStats(String month);

    Map<String, Object> getStudentGrowth(int year);

    Map<String, Object> getMonthlyFinance(LocalDate month);

    Map<String, Object> getEnrollmentGrowth();
}