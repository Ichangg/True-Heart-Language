package com.englishcenter.backend.core.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "fee_records", schema = "dbo")
public class FeeRecords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "class_id", nullable = false)
    private Long classId;

    @Column(name = "month", nullable = false)
    private LocalDate month;

    @Column(name = "sessions_attended", nullable = false)
    private Integer sessionsAttended = 0;

    @Column(name = "fee_base", nullable = false, precision = 12, scale = 2)
    private BigDecimal feeBase;

    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "fee_final", nullable = false, precision = 12, scale = 2)
    private BigDecimal feeFinal;

    @Column(name = "paid_amount", precision = 12, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    // Computed Column trong SQL Server - Chỉ đọc dữ liệu từ DB, không insert/update từ Java
    @Column(name = "debt_amount", insertable = false, updatable = false, precision = 12, scale = 2)
    private BigDecimal debtAmount;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "status", length = 20)
    private String status = "unpaid";

    public FeeRecords() {
    }

    public FeeRecords(Long studentId, Long classId, LocalDate month, Integer sessionsAttended, BigDecimal feeBase, BigDecimal discountAmount, BigDecimal feeFinal, BigDecimal paidAmount, String status) {
        this.studentId = studentId;
        this.classId = classId;
        this.month = month;
        this.sessionsAttended = sessionsAttended;
        this.feeBase = feeBase;
        this.discountAmount = discountAmount;
        this.feeFinal = feeFinal;
        this.paidAmount = paidAmount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
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

    public Integer getSessionsAttended() {
        return sessionsAttended;
    }

    public void setSessionsAttended(Integer sessionsAttended) {
        this.sessionsAttended = sessionsAttended;
    }

    public BigDecimal getFeeBase() {
        return feeBase;
    }

    public void setFeeBase(BigDecimal feeBase) {
        this.feeBase = feeBase;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFeeFinal() {
        return feeFinal;
    }

    public void setFeeFinal(BigDecimal feeFinal) {
        this.feeFinal = feeFinal;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getDebtAmount() {
        return debtAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}