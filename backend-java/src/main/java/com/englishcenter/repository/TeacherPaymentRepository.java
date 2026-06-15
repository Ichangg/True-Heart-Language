package com.englishcenter.repository;

import com.englishcenter.entity.TeacherPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TeacherPaymentRepository extends JpaRepository<TeacherPayment, Long> {

    @Query("SELECT tp FROM TeacherPayment tp WHERE " +
            "(:teacherId IS NULL OR tp.teacherId = :teacherId) AND " +
            "(:month IS NULL OR tp.month = :month) AND " +
            "(:year IS NULL OR tp.year = :year) " +
            "ORDER BY tp.year DESC, tp.month DESC")
    List<TeacherPayment> findWithFilters(
            @Param("teacherId") Long teacherId,
            @Param("month") Integer month,
            @Param("year") Integer year);

    @Query("SELECT COALESCE(SUM(tp.amount), 0) FROM TeacherPayment tp WHERE tp.month = :month AND tp.year = :year")
    BigDecimal sumAmountByMonthAndYear(@Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT COALESCE(SUM(tp.amount), 0) FROM TeacherPayment tp WHERE " +
            "(:month IS NULL OR tp.month = :month) AND " +
            "(:year IS NULL OR tp.year = :year)")
    BigDecimal sumAmountFiltered(@Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT COUNT(tp) FROM TeacherPayment tp WHERE " +
            "(:month IS NULL OR tp.month = :month) AND " +
            "(:year IS NULL OR tp.year = :year)")
    long countFiltered(@Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT COALESCE(SUM(tp.amount), 0) FROM TeacherPayment tp WHERE tp.paidAt BETWEEN :start AND :end")
    BigDecimal sumAmountByPaidAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(tp) FROM TeacherPayment tp WHERE tp.paidAt BETWEEN :start AND :end")
    long countByPaidAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(tp.amount), 0) FROM TeacherPayment tp WHERE tp.month IN :months AND tp.year = :year")
    BigDecimal sumAmountByMonthsAndYear(@Param("months") List<Integer> months, @Param("year") Integer year);

    @Query("SELECT COUNT(tp) FROM TeacherPayment tp WHERE tp.month IN :months AND tp.year = :year")
    long countByMonthsAndYear(@Param("months") List<Integer> months, @Param("year") Integer year);
}
