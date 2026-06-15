package com.englishcenter.repository;

import com.englishcenter.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByEnrollmentIdOrderByYearDescMonthDesc(Long enrollmentId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.month = :month AND p.year = :year")
    BigDecimal sumAmountByMonthAndYear(@Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE " +
            "(:month IS NULL OR p.month = :month) AND " +
            "(:year IS NULL OR p.year = :year)")
    BigDecimal sumAmountFiltered(@Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT COALESCE(SUM(p.expectedAmount), 0) FROM Payment p WHERE " +
            "(:month IS NULL OR p.month = :month) AND " +
            "(:year IS NULL OR p.year = :year)")
    BigDecimal sumExpectedAmountFiltered(@Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT COUNT(p) FROM Payment p WHERE " +
            "(:month IS NULL OR p.month = :month) AND " +
            "(:year IS NULL OR p.year = :year)")
    long countFiltered(@Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.paidAt BETWEEN :start AND :end")
    BigDecimal sumAmountByPaidAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(p.expectedAmount), 0) FROM Payment p WHERE p.paidAt BETWEEN :start AND :end")
    BigDecimal sumExpectedAmountByPaidAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paidAt BETWEEN :start AND :end")
    long countByPaidAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.month IN :months AND p.year = :year")
    BigDecimal sumAmountByMonthsAndYear(@Param("months") List<Integer> months, @Param("year") Integer year);

    @Query("SELECT COALESCE(SUM(p.expectedAmount), 0) FROM Payment p WHERE p.month IN :months AND p.year = :year")
    BigDecimal sumExpectedAmountByMonthsAndYear(@Param("months") List<Integer> months, @Param("year") Integer year);

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.month IN :months AND p.year = :year")
    long countByMonthsAndYear(@Param("months") List<Integer> months, @Param("year") Integer year);
}
