package com.englishcenter.backend.core.repository;

import com.englishcenter.backend.core.entity.TeacherSalaries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; 
import org.springframework.data.repository.query.Param; 
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherSalariesRepository extends JpaRepository<TeacherSalaries, Long> {
    
    // Tìm lịch sử lương của một giáo viên
    List<TeacherSalaries> findByTeacherId(Long teacherId);

    @Query("SELECT t FROM TeacherSalaries t WHERE MONTH(t.paidAt) = :month AND YEAR(t.paidAt) = :year")
    List<TeacherSalaries> findByMonthAndYear(@Param("month") Integer month, @Param("year") Integer year);
    
}