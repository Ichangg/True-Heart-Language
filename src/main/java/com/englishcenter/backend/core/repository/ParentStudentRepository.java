package com.englishcenter.backend.core.repository;

import com.englishcenter.backend.core.entity.ParentStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParentStudentRepository extends JpaRepository<ParentStudent, Long> {
    // Tìm tất cả con cái của một phụ huynh
    List<ParentStudent> findByParentId(Long parentId);
    
    // Tìm phụ huynh của một học sinh cụ thể
    List<ParentStudent> findByStudentId(Long studentId);
}