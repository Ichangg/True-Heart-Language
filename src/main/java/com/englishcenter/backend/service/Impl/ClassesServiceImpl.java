package com.englishcenter.backend.service.Impl;

import com.englishcenter.backend.entity.Classes;
import com.englishcenter.backend.repository.ClassesRepository;
import com.englishcenter.backend.service.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassesServiceImpl implements ClassesService {

    @Autowired
    private ClassesRepository classesRepository;

    @Override
    public List<Classes> getAllClasses() { return classesRepository.findAll(); }

    @Override
    public List<Classes> getClassesByStatus(String status) { return classesRepository.findByStatus(status); }

    @Override
    public Classes createClass(Classes classes) { return classesRepository.save(classes); }
    
    @Autowired
    private com.englishcenter.backend.repository.ClassTeachersRepository classTeachersRepository;
    
    @Autowired
    private com.englishcenter.backend.repository.ClassStudentsRepository classStudentsRepository;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Classes createClassWithAllocations(com.englishcenter.backend.dto.ClassCreationDTO dto) {
        // 1. Lưu thông tin cơ bản của lớp học
        Classes savedClass = classesRepository.save(dto.getClassInfo());
        
        // 2. Gán giáo viên (ClassTeachers)
        if (dto.getTeacherIds() != null) {
            for (int i = 0; i < dto.getTeacherIds().size(); i++) {
                Long teacherId = dto.getTeacherIds().get(i);
                com.englishcenter.backend.entity.ClassTeachers ct = new com.englishcenter.backend.entity.ClassTeachers();
                ct.setClassId(savedClass.getId());
                ct.setTeacherId(teacherId);
                ct.setIsPrimary(i == 0); // GV đầu tiên là chính
                ct.setSalaryPerSession(dto.getDefaultTeacherSalary());
                classTeachersRepository.save(ct);
            }
        }
        
        // 3. Thêm học sinh (ClassStudents)
        if (dto.getStudentIds() != null) {
            for (Long studentId : dto.getStudentIds()) {
                com.englishcenter.backend.entity.ClassStudents cs = new com.englishcenter.backend.entity.ClassStudents();
                cs.setClassId(savedClass.getId());
                cs.setStudentId(studentId);
                cs.setDiscountPercent(new java.math.BigDecimal("0.00")); // Mặc định 0% giảm giá
                classStudentsRepository.save(cs);
            }
        }
        
        return savedClass;
    }
}