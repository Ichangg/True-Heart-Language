package com.englishcenter.repository;

import com.englishcenter.entity.ParentStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParentStudentRepository extends JpaRepository<ParentStudent, Long> {
    List<ParentStudent> findByStudentId(Long studentId);
    List<ParentStudent> findByParentId(Long parentId);
    Optional<ParentStudent> findByParentIdAndStudentId(Long parentId, Long studentId);
}
