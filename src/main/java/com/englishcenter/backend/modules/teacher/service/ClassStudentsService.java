package com.englishcenter.backend.modules.teacher.service;

import com.englishcenter.backend.core.entity.ClassStudents;
import java.util.List;

public interface ClassStudentsService {
    List<ClassStudents> getStudentsInClass(Long classId);
    ClassStudents assignStudentToClass(ClassStudents classStudent);
}