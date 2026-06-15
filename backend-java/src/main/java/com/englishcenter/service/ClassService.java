package com.englishcenter.service;

import com.englishcenter.entity.ClassEntity;
import com.englishcenter.entity.Enrollment;
import com.englishcenter.entity.Session;
import com.englishcenter.entity.User;
import com.englishcenter.enums.ClassStatus;
import com.englishcenter.enums.EnrollmentStatus;
import com.englishcenter.exception.ResourceNotFoundException;
import com.englishcenter.repository.ClassRepository;
import com.englishcenter.repository.EnrollmentRepository;
import com.englishcenter.repository.SessionRepository;
import com.englishcenter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public ClassEntity createClass(Map<String, Object> data) {
        Integer ageGroup = (Integer) data.get("age_group");
        Integer year = (Integer) data.get("year");

        long existingCount = classRepository.countByAgeGroupAndYear(ageGroup, year);
        int classNumber = data.containsKey("class_number") ? (Integer) data.get("class_number") : (int) (existingCount + 1);
        String name = data.containsKey("name") ? (String) data.get("name") : "Lớp " + ageGroup + "." + classNumber;

        ClassEntity classEntity = ClassEntity.builder()
                .name(name)
                .ageGroup(ageGroup)
                .year(year)
                .classNumber(classNumber)
                .build();

        if (data.containsKey("teacher_id") && data.get("teacher_id") != null) {
            classEntity.setTeacherId(Long.valueOf(data.get("teacher_id").toString()));
        }
        if (data.containsKey("fee_per_session") && data.get("fee_per_session") != null) {
            classEntity.setFeePerSession(new java.math.BigDecimal(data.get("fee_per_session").toString()));
        }
        if (data.containsKey("sessions_per_month")) classEntity.setSessionsPerMonth((Integer) data.get("sessions_per_month"));
        if (data.containsKey("schedule_info")) classEntity.setScheduleInfo((String) data.get("schedule_info"));
        if (data.containsKey("max_students")) classEntity.setMaxStudents((Integer) data.get("max_students"));
        if (data.containsKey("description")) classEntity.setDescription((String) data.get("description"));

        return classRepository.save(classEntity);
    }

    public Map<String, Object> getClasses(Integer year, Integer ageGroup, ClassStatus status, Long teacherId, int page, int limit) {
        Sort sort = Sort.by(Sort.Direction.DESC, "year").and(Sort.by(Sort.Direction.ASC, "ageGroup")).and(Sort.by(Sort.Direction.ASC, "classNumber"));
        PageRequest pageRequest = PageRequest.of(page - 1, limit, sort);

        Page<ClassEntity> result = classRepository.findWithFilters(year, ageGroup, status, teacherId, pageRequest);

        List<Map<String, Object>> classes = result.getContent().stream().map(c -> {
            Map<String, Object> classData = new LinkedHashMap<>();
            classData.put("id", c.getId());
            classData.put("name", c.getName());
            classData.put("age_group", c.getAgeGroup());
            classData.put("year", c.getYear());
            classData.put("class_number", c.getClassNumber());
            classData.put("teacher_id", c.getTeacherId());
            classData.put("fee_per_session", c.getFeePerSession());
            classData.put("sessions_per_month", c.getSessionsPerMonth());
            classData.put("schedule_info", c.getScheduleInfo());
            classData.put("max_students", c.getMaxStudents());
            classData.put("status", c.getStatus());
            classData.put("description", c.getDescription());
            classData.put("created_at", c.getCreatedAt());
            classData.put("updated_at", c.getUpdatedAt());

            // Teacher info
            if (c.getTeacherId() != null) {
                userRepository.findById(c.getTeacherId()).ifPresent(t -> {
                    Map<String, Object> teacherInfo = new LinkedHashMap<>();
                    teacherInfo.put("id", t.getId());
                    teacherInfo.put("full_name", t.getFullName());
                    teacherInfo.put("phone", t.getPhone());
                    teacherInfo.put("email", t.getEmail());
                    classData.put("teacher", teacherInfo);
                });
            }

            // Student count
            long studentCount = enrollmentRepository.countByClassIdAndStatus(c.getId(), EnrollmentStatus.active);
            classData.put("student_count", studentCount);

            return classData;
        }).collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("classes", classes);
        response.put("total", result.getTotalElements());
        return response;
    }

    public ClassEntity getClassById(Long id) {
        return classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lớp không tồn tại."));
    }

    public ClassEntity updateClass(Long id, Map<String, Object> data) {
        ClassEntity classEntity = getClassById(id);

        if (data.containsKey("name")) classEntity.setName((String) data.get("name"));
        if (data.containsKey("teacher_id") && data.get("teacher_id") != null) {
            classEntity.setTeacherId(Long.valueOf(data.get("teacher_id").toString()));
        }
        if (data.containsKey("fee_per_session") && data.get("fee_per_session") != null) {
            classEntity.setFeePerSession(new java.math.BigDecimal(data.get("fee_per_session").toString()));
        }
        if (data.containsKey("sessions_per_month")) classEntity.setSessionsPerMonth((Integer) data.get("sessions_per_month"));
        if (data.containsKey("schedule_info")) classEntity.setScheduleInfo((String) data.get("schedule_info"));
        if (data.containsKey("max_students")) classEntity.setMaxStudents((Integer) data.get("max_students"));
        if (data.containsKey("description")) classEntity.setDescription((String) data.get("description"));

        return classRepository.save(classEntity);
    }

    public ClassEntity closeClass(Long id) {
        ClassEntity classEntity = getClassById(id);
        classEntity.setStatus(ClassStatus.closed);
        return classRepository.save(classEntity);
    }

    public ClassEntity reopenClass(Long id) {
        ClassEntity classEntity = getClassById(id);
        classEntity.setStatus(ClassStatus.active);
        return classRepository.save(classEntity);
    }

    public List<Map<String, Object>> getClassesByTeacher(Long teacherId) {
        List<ClassEntity> classes = classRepository.findByTeacherId(teacherId);
        return classes.stream().map(c -> {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("id", c.getId());
            data.put("name", c.getName());
            data.put("age_group", c.getAgeGroup());
            data.put("year", c.getYear());
            data.put("status", c.getStatus());
            data.put("schedule_info", c.getScheduleInfo());

            long studentCount = enrollmentRepository.countByClassIdAndStatus(c.getId(), EnrollmentStatus.active);
            data.put("student_count", studentCount);

            List<Session> sessions = sessionRepository.findByClassIdOrderBySessionNumberAsc(c.getId());
            data.put("total_sessions", sessions.size());
            data.put("completed_sessions", sessions.stream().filter(s -> s.getStatus() == com.englishcenter.enums.SessionStatus.completed).count());

            return data;
        }).collect(Collectors.toList());
    }
}
