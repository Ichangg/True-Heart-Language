package com.englishcenter.backend.modules.admin.controller;

import com.englishcenter.backend.modules.admin.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


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