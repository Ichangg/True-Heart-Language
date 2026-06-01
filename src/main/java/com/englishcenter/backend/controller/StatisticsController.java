package com.englishcenter.backend.controller;

import com.englishcenter.backend.service.StatisticsService; // Corrected import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    // Thống kê tài chính: Tiền trả GV, tiền học sinh đóng (Dự kiến & Thực tế)
    @GetMapping("/finance")
    public Map<String, Object> getFinancialStats(@RequestParam String month) {
        return statisticsService.getFinancialStats(month);
    }

    // Thống kê lượng học sinh tăng giảm theo tháng (Requirement)
    @GetMapping("/student-growth")
    public Map<String, Object> getStudentGrowth(@RequestParam int year) {
        return statisticsService.getStudentGrowth(year);
    }
}