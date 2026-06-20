package com.englishcenter.backend.core.repository;

import com.englishcenter.backend.core.entity.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SessionsRepository extends JpaRepository<Sessions, Long> {
    // Tìm các buổi học của một lớp cụ thể
    List<Sessions> findByClassId(Long classId);
    
    // Tìm các buổi học theo trạng thái (COMPLETED, SCHEDULED, CANCELLED)
    List<Sessions> findByStatus(String status);

    // Tìm các buổi học của một giáo viên cụ thể
    List<Sessions> findByTeacherId(Long teacherId);
}