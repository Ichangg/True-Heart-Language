package com.englishcenter.backend.core.repository;

import com.englishcenter.backend.core.entity.ClassTeachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassTeachersRepository extends JpaRepository<ClassTeachers, Long> {
    List<ClassTeachers> findByClassId(Long classId);

    List<ClassTeachers> findByTeacherId(Long teacherId);
}
