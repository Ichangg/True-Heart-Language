package com.englishcenter.backend.service;

import com.englishcenter.backend.entity.ParentStudent;
import com.englishcenter.backend.entity.Attendances;
import java.util.List;

public interface ParentStudentService {
    List<ParentStudent> getStudentsByParent(Long parentId);
    List<Attendances> getChildAttendance(Long studentId);
    ParentStudent linkParentAndStudent(ParentStudent parentStudent);
}