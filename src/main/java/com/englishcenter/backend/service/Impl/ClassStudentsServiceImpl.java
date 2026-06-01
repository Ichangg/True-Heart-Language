package com.englishcenter.backend.service.Impl;

import com.englishcenter.backend.entity.ClassStudents;
import com.englishcenter.backend.repository.ClassStudentsRepository;
import com.englishcenter.backend.service.ClassStudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassStudentsServiceImpl implements ClassStudentsService {

    @Autowired
    private ClassStudentsRepository classStudentsRepository;

    @Override
    public List<ClassStudents> getStudentsInClass(Long classId) {
        return classStudentsRepository.findByClassId(classId);
    }

    @Override
    public ClassStudents assignStudentToClass(ClassStudents classStudent) { return classStudentsRepository.save(classStudent); }
}