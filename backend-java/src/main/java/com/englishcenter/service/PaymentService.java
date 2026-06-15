package com.englishcenter.service;

import com.englishcenter.entity.*;
import com.englishcenter.enums.AttendanceStatus;
import com.englishcenter.enums.Role;
import com.englishcenter.exception.ResourceNotFoundException;
import com.englishcenter.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TeacherPaymentRepository teacherPaymentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    public Payment recordPayment(Long enrollmentId, BigDecimal amount, Integer month, Integer year, Long receivedBy, String note) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Đăng ký không tồn tại."));

        ClassEntity classEntity = classRepository.findById(enrollment.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Lớp không tồn tại."));

        // Tính expected amount
        BigDecimal baseFee;
        if (enrollment.getCustomFee() != null) {
            baseFee = enrollment.getCustomFee();
        } else {
            baseFee = classEntity.getFeePerSession().multiply(BigDecimal.valueOf(classEntity.getSessionsPerMonth()));
        }
        BigDecimal discount = baseFee.multiply(enrollment.getDiscountPercent()).divide(BigDecimal.valueOf(100));
        BigDecimal expectedAmount = baseFee.subtract(discount);

        Payment payment = Payment.builder()
                .enrollmentId(enrollmentId)
                .amount(amount)
                .expectedAmount(expectedAmount)
                .month(month)
                .year(year)
                .receivedBy(receivedBy)
                .note(note)
                .build();

        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsByEnrollment(Long enrollmentId) {
        return paymentRepository.findByEnrollmentIdOrderByYearDescMonthDesc(enrollmentId);
    }

    public Map<String, Object> getFinanceReport(String period, Integer month, Integer quarter, Integer year, String startDate, String endDate) {
        BigDecimal totalCollected, totalExpected;
        long paymentCount;
        BigDecimal teacherTotalPaid;
        long teacherPaymentCount;

        if ("custom".equals(period) && startDate != null && endDate != null) {
            LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
            LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
            totalCollected = paymentRepository.sumAmountByPaidAtBetween(start, end);
            totalExpected = paymentRepository.sumExpectedAmountByPaidAtBetween(start, end);
            paymentCount = paymentRepository.countByPaidAtBetween(start, end);
            teacherTotalPaid = teacherPaymentRepository.sumAmountByPaidAtBetween(start, end);
            teacherPaymentCount = teacherPaymentRepository.countByPaidAtBetween(start, end);
        } else if ("quarter".equals(period) && quarter != null) {
            Map<Integer, List<Integer>> quarterMonths = Map.of(1, List.of(1,2,3), 2, List.of(4,5,6), 3, List.of(7,8,9), 4, List.of(10,11,12));
            List<Integer> months = quarterMonths.get(quarter);
            totalCollected = paymentRepository.sumAmountByMonthsAndYear(months, year);
            totalExpected = paymentRepository.sumExpectedAmountByMonthsAndYear(months, year);
            paymentCount = paymentRepository.countByMonthsAndYear(months, year);
            teacherTotalPaid = teacherPaymentRepository.sumAmountByMonthsAndYear(months, year);
            teacherPaymentCount = teacherPaymentRepository.countByMonthsAndYear(months, year);
        } else {
            // month or year
            totalCollected = paymentRepository.sumAmountFiltered(month, year);
            totalExpected = paymentRepository.sumExpectedAmountFiltered(month, year);
            paymentCount = paymentRepository.countFiltered(month, year);
            teacherTotalPaid = teacherPaymentRepository.sumAmountFiltered(month, year);
            teacherPaymentCount = teacherPaymentRepository.countFiltered(month, year);
        }

        BigDecimal outstanding = totalExpected.subtract(totalCollected);
        BigDecimal netIncome = totalCollected.subtract(teacherTotalPaid);

        Map<String, Object> result = new LinkedHashMap<>();
        Map<String, Object> studentRevenue = new LinkedHashMap<>();
        studentRevenue.put("total_collected", totalCollected);
        studentRevenue.put("total_expected", totalExpected);
        studentRevenue.put("outstanding", outstanding);
        studentRevenue.put("payment_count", paymentCount);

        Map<String, Object> teacherExpenses = new LinkedHashMap<>();
        teacherExpenses.put("total_paid", teacherTotalPaid);
        teacherExpenses.put("payment_count", teacherPaymentCount);

        result.put("student_revenue", studentRevenue);
        result.put("teacher_expenses", teacherExpenses);
        result.put("net_income", netIncome);

        return result;
    }

    public TeacherPayment recordTeacherPayment(Long teacherId, BigDecimal amount, Integer month, Integer year, Long classId, Integer sessionsCount, String note) {
        userRepository.findById(teacherId)
                .filter(u -> u.getRole() == Role.teacher)
                .orElseThrow(() -> new ResourceNotFoundException("Giáo viên không tồn tại."));

        TeacherPayment payment = TeacherPayment.builder()
                .teacherId(teacherId)
                .amount(amount)
                .month(month)
                .year(year)
                .classId(classId)
                .sessionsCount(sessionsCount)
                .note(note)
                .build();

        return teacherPaymentRepository.save(payment);
    }

    public List<TeacherPayment> getTeacherPayments(Long teacherId, Integer month, Integer year) {
        return teacherPaymentRepository.findWithFilters(teacherId, month, year);
    }

    public Map<String, Object> getOutstandingBalance(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
        if (enrollment == null) return null;

        ClassEntity classEntity = classRepository.findById(enrollment.getClassId()).orElse(null);
        if (classEntity == null) return null;

        BigDecimal baseFeePerSession;
        if (enrollment.getCustomFee() != null) {
            baseFeePerSession = enrollment.getCustomFee().divide(BigDecimal.valueOf(classEntity.getSessionsPerMonth()), BigDecimal.ROUND_HALF_UP);
        } else {
            baseFeePerSession = classEntity.getFeePerSession();
        }

        List<Attendance> records = attendanceRepository.findByEnrollmentId(enrollment.getId());
        long attendedSessions = records.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.present || a.getStatus() == AttendanceStatus.late)
                .count();

        BigDecimal totalExpected = baseFeePerSession.multiply(BigDecimal.valueOf(attendedSessions));
        BigDecimal discountAmount = totalExpected.multiply(enrollment.getDiscountPercent()).divide(BigDecimal.valueOf(100));
        BigDecimal totalAfterDiscount = totalExpected.subtract(discountAmount);

        List<Payment> payments = paymentRepository.findByEnrollmentIdOrderByYearDescMonthDesc(enrollment.getId());
        BigDecimal totalPaid = payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total_sessions", attendedSessions);
        result.put("base_fee_per_session", baseFeePerSession);
        result.put("total_expected", totalExpected);
        result.put("discount_percent", enrollment.getDiscountPercent());
        result.put("discount_amount", discountAmount);
        result.put("total_after_discount", totalAfterDiscount);
        result.put("total_paid", totalPaid);
        result.put("outstanding", totalAfterDiscount.subtract(totalPaid));

        return result;
    }
}
