package com.englishcenter.backend.modules.admin.service.Impl;
import com.englishcenter.backend.core.repository.ClassTeachersRepository;
import com.englishcenter.backend.core.entity.ClassTeachers;

import com.englishcenter.backend.core.entity.TeacherSalaries;
import com.englishcenter.backend.core.repository.TeacherSalariesRepository;
import com.englishcenter.backend.modules.admin.service.TeacherSalariesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherSalariesServiceImpl implements TeacherSalariesService {

    @Autowired
    private TeacherSalariesRepository teacherSalariesRepository;

    @Override
    public List<TeacherSalaries> getSalariesByTeacher(Long teacherId) {
        return teacherSalariesRepository.findByTeacherId(teacherId);
    }

    @Override
    public TeacherSalaries addSalaryRecord(TeacherSalaries salary) {
        return teacherSalariesRepository.save(salary);
    }
    
    @Autowired
    private com.englishcenter.backend.core.repository.ClassTeachersRepository classTeachersRepository;
    
    @org.springframework.transaction.annotation.Transactional
    public void generateTeacherSalaries(java.time.LocalDate month) {
        List<com.englishcenter.backend.core.entity.ClassTeachers> assignments = classTeachersRepository.findAll();
        for (com.englishcenter.backend.core.entity.ClassTeachers ct : assignments) {
            TeacherSalaries salary = new TeacherSalaries();
            salary.setTeacherId(ct.getTeacherId());
            salary.setClassId(ct.getClassId());
            salary.setMonth(month);
            
            // Giả lập tính 8 buổi dạy một tháng. Thực tế cần query số buổi điểm danh 'done' từ bảng sessions/attendances
            int sessionsTaught = 8;
            salary.setSessionsTaught(sessionsTaught);
            
            java.math.BigDecimal salaryPerSession = ct.getSalaryPerSession() != null ? ct.getSalaryPerSession() : java.math.BigDecimal.ZERO;
            salary.setTotalSalary(salaryPerSession.multiply(new java.math.BigDecimal(sessionsTaught)));
            salary.setPaidAmount(java.math.BigDecimal.ZERO);
            salary.setStatus("pending");
            
            teacherSalariesRepository.save(salary);
        }
    }
}