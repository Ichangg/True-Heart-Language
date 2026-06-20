package com.englishcenter.backend.modules.admin.service;

import com.englishcenter.backend.core.entity.TeacherSalaries;

import java.util.List;

public interface TeacherSalariesService {
    List<TeacherSalaries> getSalariesByTeacher(Long teacherId);
    TeacherSalaries addSalaryRecord(TeacherSalaries salary);
    void generateTeacherSalaries(java.time.LocalDate month);
}