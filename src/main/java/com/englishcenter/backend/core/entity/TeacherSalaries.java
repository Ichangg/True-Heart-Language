package com.englishcenter.backend.core.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "teacher_salaries", schema = "dbo")
public class TeacherSalaries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Column(name = "class_id", nullable = false)
    private Long classId;

    @Column(name = "month", nullable = false)
    private LocalDate month;

    @Column(name = "sessions_taught", nullable = false)
    private Integer sessionsTaught = 0;

    @Column(name = "total_salary", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalSalary;

    @Column(name = "paid_amount", precision = 18, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "status", length = 20)
    private String status = "pending";

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public TeacherSalaries() {
    }

    public TeacherSalaries(Long teacherId, Long classId, LocalDate month, Integer sessionsTaught, BigDecimal totalSalary, BigDecimal paidAmount, String status, LocalDateTime paidAt) {
        this.teacherId = teacherId;
        this.classId = classId;
        this.month = month;
        this.sessionsTaught = sessionsTaught;
        this.totalSalary = totalSalary;
        this.paidAmount = paidAmount;
        this.status = status;
        this.paidAt = paidAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public LocalDate getMonth() {
        return month;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public Integer getSessionsTaught() {
        return sessionsTaught;
    }

    public void setSessionsTaught(Integer sessionsTaught) {
        this.sessionsTaught = sessionsTaught;
    }

    public BigDecimal getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(BigDecimal totalSalary) {
        this.totalSalary = totalSalary;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}