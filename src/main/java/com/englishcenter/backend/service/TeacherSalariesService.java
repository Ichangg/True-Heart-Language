package com.englishcenter.backend.service;

import com.englishcenter.backend.entity.TeacherSalaries;

import java.util.List;
import java.util.Optional;

public interface TeacherSalariesService {
    List<TeacherSalaries> getSalariesByTeacher(Long teacherId);
    TeacherSalaries addSalaryRecord(TeacherSalaries salary);
}