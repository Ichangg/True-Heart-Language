package com.englishcenter.backend.core.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendances", schema = "dbo")
public class Attendances {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "note", columnDefinition = "NVARCHAR(MAX)")
    private String note;

    @Column(name = "notified_parent")
    private Boolean notifiedParent = false;

    @Column(name = "marked_at", insertable = false, updatable = false)
    private LocalDateTime markedAt;

    public Attendances() {
    }

    public Attendances(Long sessionId, Long studentId, String status, String note, Boolean notifiedParent) {
        this.sessionId = sessionId;
        this.studentId = studentId;
        this.status = status;
        this.note = note;
        this.notifiedParent = notifiedParent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getNotifiedParent() {
        return notifiedParent;
    }

    public void setNotifiedParent(Boolean notifiedParent) {
        this.notifiedParent = notifiedParent;
    }

    public LocalDateTime getMarkedAt() {
        return markedAt;
    }

    public void setMarkedAt(LocalDateTime markedAt) {
        this.markedAt = markedAt;
    }
}