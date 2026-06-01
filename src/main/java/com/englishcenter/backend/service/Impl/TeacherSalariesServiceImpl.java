package com.englishcenter.backend.service.Impl;

import com.englishcenter.backend.entity.TeacherSalaries;
import com.englishcenter.backend.repository.TeacherSalariesRepository;
import com.englishcenter.backend.service.TeacherSalariesService;
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
    private com.englishcenter.backend.repository.ClassTeachersRepository classTeachersRepository;
    
    @org.springframework.transaction.annotation.Transactional
    public void generateTeacherSalaries(java.time.LocalDate month) {
        List<com.englishcenter.backend.entity.ClassTeachers> assignments = classTeachersRepository.findAll();
        for (com.englishcenter.backend.entity.ClassTeachers ct : assignments) {
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