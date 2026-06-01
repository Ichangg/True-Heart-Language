package com.englishcenter.backend.service.Impl;

import com.englishcenter.backend.entity.Attendances;
import com.englishcenter.backend.entity.ParentStudent;
import com.englishcenter.backend.repository.AttendancesRepository;
import com.englishcenter.backend.repository.ParentStudentRepository;
import com.englishcenter.backend.service.ParentStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParentStudentServiceImpl implements ParentStudentService {

    @Autowired
    private ParentStudentRepository parentStudentRepository;
    @Autowired
    private AttendancesRepository attendancesRepository;

    @Override
    public List<ParentStudent> getStudentsByParent(Long parentId) { return parentStudentRepository.findByParentId(parentId); }

    @Override
    public List<Attendances> getChildAttendance(Long studentId) { return attendancesRepository.findByStudentId(studentId); }

    @Override
    public ParentStudent linkParentAndStudent(ParentStudent parentStudent) { return parentStudentRepository.save(parentStudent); }
}