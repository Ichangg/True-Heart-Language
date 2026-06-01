package com.englishcenter.backend.service;

import com.englishcenter.backend.entity.ClassStudents;
import java.util.List;

public interface ClassStudentsService {
    List<ClassStudents> getStudentsInClass(Long classId);
    ClassStudents assignStudentToClass(ClassStudents classStudent);
}