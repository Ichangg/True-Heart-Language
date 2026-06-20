package com.englishcenter.backend.modules.admin.service.Impl;

import com.englishcenter.backend.core.entity.User;
import com.englishcenter.backend.core.repository.UserRepository;
import com.englishcenter.backend.core.repository.ClassTeachersRepository;
import com.englishcenter.backend.core.repository.ClassStudentsRepository;
import com.englishcenter.backend.core.repository.ParentStudentRepository;
import com.englishcenter.backend.core.repository.ClassesRepository;
import com.englishcenter.backend.core.repository.TeacherSalariesRepository;
import com.englishcenter.backend.modules.admin.service.UserService;
import com.englishcenter.backend.modules.admin.dto.UserCreationDTO;
import com.englishcenter.backend.modules.admin.dto.TeacherProfileDTO;
import com.englishcenter.backend.modules.admin.dto.ParentProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ParentStudentRepository parentStudentRepository;
    
    @Autowired
    private ClassStudentsRepository classStudentsRepository;

    @Autowired
    private ClassTeachersRepository classTeachersRepository;
    
    @Autowired
    private ClassesRepository classesRepository;
    
    @Autowired
    private TeacherSalariesRepository teacherSalariesRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private User mapToUser(UserCreationDTO dto, String role) {
        User u = new User();
        u.setFullname(dto.getFullname());
        u.setEmail(dto.getEmail());
        u.setPhone(dto.getPhone());
        u.setAddress(dto.getAddress());
        u.setRole(role);
        // Default password hash for "123456"
        u.setPasswordHash("$2a$10$6yaDDNe9cMzjiEP0FxybcePOyNeRhUYhaweISjjStQgPTMPLlaQKG"); 
        u.setActive(true);
        u.setCreatedAt(java.time.LocalDateTime.now());
        return u;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public User createTeacher(UserCreationDTO dto) {
        User teacher = mapToUser(dto, "teacher");
        return userRepository.save(teacher);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public User createParent(UserCreationDTO dto) {
        User parent = mapToUser(dto, "parent");
        parent = userRepository.save(parent);
        
        if (dto.getStudentIds() != null) {
            for (Long studentId : dto.getStudentIds()) {
                com.englishcenter.backend.core.entity.ParentStudent ps = new com.englishcenter.backend.core.entity.ParentStudent();
                ps.setParentId(parent.getId());
                ps.setStudentId(studentId);
                ps.setRelationship("parent");
                ps.setIsPrimaryContact(true);
                parentStudentRepository.save(ps);
            }
        }
        return parent;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public User createStudent(UserCreationDTO dto) {
        User student = mapToUser(dto, "student");
        student.setBatch(dto.getBatch());
        student.setAcademicStatus(dto.getAcademicStatus() != null ? dto.getAcademicStatus() : "active");
        student = userRepository.save(student);
        
        if (dto.getClassId() != null) {
            com.englishcenter.backend.core.entity.ClassStudents cs = new com.englishcenter.backend.core.entity.ClassStudents();
            cs.setClassId(dto.getClassId());
            cs.setStudentId(student.getId());
            cs.setDiscountPercent(dto.getDiscountPercent() != null ? dto.getDiscountPercent() : new java.math.BigDecimal("0"));
            classStudentsRepository.save(cs);
        }
        return student;
    }
    
    @Override
    public TeacherProfileDTO getTeacherProfile(Long teacherId) {
        User teacher = userRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        // Find classes by manual filter to avoid missing repo method errors
        List<com.englishcenter.backend.core.entity.ClassTeachers> ctList = classTeachersRepository.findAll().stream()
            .filter(ct -> ct.getTeacherId().equals(teacherId))
            .collect(java.util.stream.Collectors.toList());
            
        List<com.englishcenter.backend.core.entity.Classes> classes = ctList.stream()
            .map(ct -> classesRepository.findById(ct.getClassId()).orElse(null))
            .filter(c -> c != null)
            .collect(java.util.stream.Collectors.toList());
            
        List<com.englishcenter.backend.core.entity.TeacherSalaries> salaries = teacherSalariesRepository.findAll().stream()
            .filter(s -> s.getTeacherId().equals(teacherId))
            .collect(java.util.stream.Collectors.toList());
        
        return new TeacherProfileDTO(teacher, classes, salaries);
    }
    
    @Override
    public List<ParentProfileDTO> getParentsByClass(Long classId) {
        // Find all students in class
        List<com.englishcenter.backend.core.entity.ClassStudents> csList = classStudentsRepository.findAll().stream()
            .filter(cs -> cs.getClassId().equals(classId))
            .collect(java.util.stream.Collectors.toList());
            
        List<Long> studentIds = csList.stream().map(cs -> cs.getStudentId()).collect(java.util.stream.Collectors.toList());
        
        // Find parents for these students
        List<com.englishcenter.backend.core.entity.ParentStudent> psList = parentStudentRepository.findAll().stream()
            .filter(ps -> studentIds.contains(ps.getStudentId()))
            .collect(java.util.stream.Collectors.toList());
            
        // Group by parentId
        java.util.Map<Long, List<Long>> parentToStudentIds = psList.stream()
            .collect(java.util.stream.Collectors.groupingBy(ps -> ps.getParentId(), 
                java.util.stream.Collectors.mapping(ps -> ps.getStudentId(), java.util.stream.Collectors.toList())));
                
        List<ParentProfileDTO> result = new java.util.ArrayList<>();
        for (java.util.Map.Entry<Long, List<Long>> entry : parentToStudentIds.entrySet()) {
            User parent = userRepository.findById(entry.getKey()).orElse(null);
            if (parent != null) {
                List<User> children = entry.getValue().stream()
                    .map(sid -> userRepository.findById(sid).orElse(null))
                    .filter(u -> u != null)
                    .collect(java.util.stream.Collectors.toList());
                result.add(new ParentProfileDTO(parent, children));
            }
        }
        return result;
    }
}
