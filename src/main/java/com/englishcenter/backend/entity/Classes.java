package com.englishcenter.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
@Entity
@Table(name = "classes", schema = "dbo")
    public class Classes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_group_id", nullable = false)
    private Long classGroupId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "fee_per_session", nullable = false, precision = 18, scale = 2)
    private BigDecimal feePerSession;

    @Column(name = "max_students", nullable = false)
    private Integer maxStudents;

    @Column(name = "schedule", columnDefinition = "NVARCHAR(MAX)")
    private String schedule;

    @Column(name = "status", length = 20)
    private String status = "active";

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
    // Constructors, getters, and setters

    public Classes() {
    }

    public Classes(Long classGroupId, String name, Integer year, BigDecimal feePerSession, Integer maxStudents, String schedule) {
        this.classGroupId = classGroupId;
        this.name = name;
        this.year = year;
        this.feePerSession = feePerSession;
        this.maxStudents = maxStudents;
        this.schedule = schedule;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClassGroupId() {
        return classGroupId;
    }

    public void setClassGroupId(Long classGroupId) {
        this.classGroupId = classGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getFeePerSession() {
        return feePerSession;
    }

    public void setFeePerSession(BigDecimal feePerSession) {
        this.feePerSession = feePerSession;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
        
    }

    
}

