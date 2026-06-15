package com.englishcenter.service;

import com.englishcenter.enums.*;
import com.englishcenter.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;
    private final TeacherPaymentRepository teacherPaymentRepository;

    public Map<String, Object> getDashboardStats() {
        long totalStudents = userRepository.countByRoleAndStatus(Role.student, UserStatus.active);
        long totalTeachers = userRepository.countByRoleAndStatus(Role.teacher, UserStatus.active);
        long totalParents = userRepository.countByRoleAndStatus(Role.parent, UserStatus.active);
        long activeClasses = classRepository.countByStatus(ClassStatus.active);
        long closedClasses = classRepository.countByStatus(ClassStatus.closed);

        int currentMonth = LocalDateTime.now().getMonthValue();
        int currentYear = LocalDateTime.now().getYear();

        BigDecimal monthlyRevenue = paymentRepository.sumAmountByMonthAndYear(currentMonth, currentYear);
        BigDecimal monthlyTeacherCost = teacherPaymentRepository.sumAmountByMonthAndYear(currentMonth, currentYear);

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("total_students", totalStudents);
        stats.put("total_teachers", totalTeachers);
        stats.put("total_parents", totalParents);
        stats.put("active_classes", activeClasses);
        stats.put("closed_classes", closedClasses);
        stats.put("monthly_revenue", monthlyRevenue);
        stats.put("monthly_teacher_cost", monthlyTeacherCost);
        stats.put("monthly_profit", monthlyRevenue.subtract(monthlyTeacherCost));

        return stats;
    }

    public List<Map<String, Object>> getStudentTrend(Integer year) {
        int targetYear = year != null ? year : LocalDateTime.now().getYear();
        List<Map<String, Object>> monthlyData = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            LocalDateTime startDate = LocalDateTime.of(targetYear, month, 1, 0, 0, 0);
            LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1);

            long newEnrollments = enrollmentRepository.countByEnrolledAtBetween(startDate, endDate);
            long totalActive = enrollmentRepository.countByStatusAndEnrolledAtLessThanEqual(EnrollmentStatus.active, endDate);
            long withdrawn = enrollmentRepository.countByStatusAndUpdatedAtBetween(EnrollmentStatus.withdrawn, startDate, endDate);

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("month", month);
            data.put("year", targetYear);
            data.put("new_students", newEnrollments);
            data.put("total_active", totalActive);
            data.put("withdrawn", withdrawn);
            data.put("net_change", newEnrollments - withdrawn);

            monthlyData.add(data);
        }

        return monthlyData;
    }

    public List<Map<String, Object>> getTopClasses(int limit) {
        // Simple approach: get active classes and sort by enrollment count
        var classes = classRepository.findAll().stream()
                .filter(c -> c.getStatus() == ClassStatus.active)
                .map(c -> {
                    long count = enrollmentRepository.countByClassIdAndStatus(c.getId(), EnrollmentStatus.active);
                    Map<String, Object> data = new LinkedHashMap<>();
                    data.put("id", c.getId());
                    data.put("name", c.getName());
                    data.put("year", c.getYear());
                    data.put("student_count", count);
                    if (c.getTeacherId() != null) {
                        userRepository.findById(c.getTeacherId()).ifPresent(t -> {
                            Map<String, Object> teacherInfo = new LinkedHashMap<>();
                            teacherInfo.put("id", t.getId());
                            teacherInfo.put("full_name", t.getFullName());
                            data.put("teacher", teacherInfo);
                        });
                    }
                    return data;
                })
                .sorted((a, b) -> Long.compare((Long) b.get("student_count"), (Long) a.get("student_count")))
                .limit(limit)
                .collect(java.util.stream.Collectors.toList());

        return classes;
    }
}
