package com.englishcenter.backend.modules.admin.dto;

import java.util.List;

public class UserCreationDTO {
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String password;
    
    // For Student
    private String batch;
    private String academicStatus;
    private Long classId; // To enroll immediately
    private java.math.BigDecimal discountPercent;

    // For Parent
    private List<Long> studentIds;

    // For Teacher
    private java.math.BigDecimal defaultSalaryPerSession;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getAcademicStatus() {
        return academicStatus;
    }

    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public java.math.BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(java.math.BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public List<Long> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }

    public java.math.BigDecimal getDefaultSalaryPerSession() {
        return defaultSalaryPerSession;
    }

    public void setDefaultSalaryPerSession(java.math.BigDecimal defaultSalaryPerSession) {
        this.defaultSalaryPerSession = defaultSalaryPerSession;
    }
}
