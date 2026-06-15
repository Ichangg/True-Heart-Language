package com.englishcenter.controller;

import com.englishcenter.dto.ApiResponse;
import com.englishcenter.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.success(statisticService.getDashboardStats(), "Thống kê tổng quan."));
    }

    @GetMapping("/student-trend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStudentTrend(@RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(ApiResponse.success(statisticService.getStudentTrend(year), "Thống kê tăng giảm học sinh."));
    }

    @GetMapping("/top-classes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTopClasses(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(ApiResponse.success(statisticService.getTopClasses(limit)));
    }
}
