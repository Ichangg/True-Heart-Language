package com.englishcenter.backend.modules.admin.service.Impl;

import com.englishcenter.backend.core.repository.FeeRecordsRepository;
import com.englishcenter.backend.core.repository.TeacherSalariesRepository;
import com.englishcenter.backend.modules.admin.service.StatisticsService;
import com.englishcenter.backend.core.entity.TeacherSalaries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private FeeRecordsRepository feeRecordsRepository;

    @Autowired
    private TeacherSalariesRepository teacherSalariesRepository;

    @Override
    public Map<String, Object> getFinancialStats(String month) {
        if (month == null || month.isEmpty()) {
            return Map.of("error", "Tham số tháng không được để trống");
        }

        // Nếu truyền vào "yyyy-MM", ta chuyển thành "yyyy-MM-01" để parse được LocalDate
        String dateToParse = month;
        if (month.matches("\\d{4}-\\d{2}")) {
            dateToParse = month + "-01";
        }
        LocalDate targetMonth = LocalDate.parse(dateToParse);
        int m = targetMonth.getMonthValue();
        int y = targetMonth.getYear();

        Double expected = feeRecordsRepository.sumExpectedRevenueByMonth(m, y);
        Double actual = feeRecordsRepository.sumActualRevenueByMonth(m, y);
        
        List<TeacherSalaries> salaries = teacherSalariesRepository.findByMonthAndYear(m, y);
        Double totalSalaries = salaries.stream()
                .filter(s -> s.getPaidAmount() != null)
                .mapToDouble(s -> s.getPaidAmount().doubleValue())
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("month", month);
        stats.put("expectedRevenue", expected != null ? expected : 0.0);
        stats.put("actualRevenue", actual != null ? actual : 0.0);
        stats.put("teacherSalaries", totalSalaries);
        return stats;
    }

    @Override
    public Map<String, Object> getStudentGrowth(int year) {
        Map<String, Object> growthData = new HashMap<>();
        growthData.put("year", year);
        // TODO: Implement actual queries using classStudentsRepository to count new students per month
        growthData.put("data", new int[]{5, 10, 2, -1, 4});
        return growthData;
    }

    @Override
    public Map<String, Object> getMonthlyFinance(LocalDate month) {
        // This method was missing, adding a placeholder implementation
        return getFinancialStats(month.toString());
    }

    @Override
    public Map<String, Object> getEnrollmentGrowth() {
        // This method was missing, adding a placeholder implementation
        return getStudentGrowth(LocalDate.now().getYear());
    }
}