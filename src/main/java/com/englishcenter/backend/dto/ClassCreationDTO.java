package com.englishcenter.backend.dto;

import com.englishcenter.backend.entity.Classes;
import java.util.List;
import java.math.BigDecimal;

public class ClassCreationDTO {
    private Classes classInfo;
    private List<Long> teacherIds;
    private List<Long> studentIds;
    private BigDecimal defaultTeacherSalary;

    // Getters and setters
    public Classes getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(Classes classInfo) {
        this.classInfo = classInfo;
    }

    public List<Long> getTeacherIds() {
        return teacherIds;
    }

    public void setTeacherIds(List<Long> teacherIds) {
        this.teacherIds = teacherIds;
    }

    public List<Long> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }

    public BigDecimal getDefaultTeacherSalary() {
        return defaultTeacherSalary;
    }

    public void setDefaultTeacherSalary(BigDecimal defaultTeacherSalary) {
        this.defaultTeacherSalary = defaultTeacherSalary;
    }
}
