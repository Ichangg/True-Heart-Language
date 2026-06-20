package com.englishcenter.backend.core.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "payments", schema = "dbo")
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fee_record_id", nullable = false)
    private Long feeRecordId;

    @Column(name = "collected_by", nullable = false)
    private Long collectedBy;

    @Column(name = "amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false, length = 20)
    private String paymentMethod;

    @Column(name = "note", columnDefinition = "NVARCHAR(MAX)")
    private String note;

    @Column(name = "paid_at", insertable = false, updatable = false)
    private LocalDateTime paidAt;

    public Payments() {
    }

    public Payments(Long feeRecordId, Long collectedBy, BigDecimal amount, String paymentMethod, String note) {
        this.feeRecordId = feeRecordId;
        this.collectedBy = collectedBy;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFeeRecordId() {
        return feeRecordId;
    }

    public void setFeeRecordId(Long feeRecordId) {
        this.feeRecordId = feeRecordId;
    }

    public Long getCollectedBy() {
        return collectedBy;
    }

    public void setCollectedBy(Long collectedBy) {
        this.collectedBy = collectedBy;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}