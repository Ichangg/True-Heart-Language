package com.englishcenter.backend.core.repository;

import com.englishcenter.backend.core.entity.Attendances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AttendancesRepository extends JpaRepository<Attendances, Long> {
    // Tìm danh sách điểm danh của một buổi học
    List<Attendances> findBySessionId(Long sessionId);
    
    // Tìm danh sách điểm danh của một học sinh
    List<Attendances> findByStudentId(Long studentId);
}