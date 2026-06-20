package com.englishcenter.backend.modules.student.service;

import com.englishcenter.backend.core.entity.ParentStudent;
import com.englishcenter.backend.core.entity.Attendances;
import java.util.List;

public interface ParentStudentService {
    List<ParentStudent> getStudentsByParent(Long parentId);
    List<Attendances> getChildAttendance(Long studentId);
    ParentStudent linkParentAndStudent(ParentStudent parentStudent);
}