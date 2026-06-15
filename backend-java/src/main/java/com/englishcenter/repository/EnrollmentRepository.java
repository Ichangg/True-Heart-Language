package com.englishcenter.repository;

import com.englishcenter.entity.Enrollment;
import com.englishcenter.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByStudentIdAndClassId(Long studentId, Long classId);

    List<Enrollment> findByClassId(Long classId);

    List<Enrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status);

    long countByClassIdAndStatus(Long classId, EnrollmentStatus status);

    long countByEnrolledAtBetween(LocalDateTime start, LocalDateTime end);

    long countByStatusAndEnrolledAtLessThanEqual(EnrollmentStatus status, LocalDateTime endDate);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.status = :status AND e.updatedAt BETWEEN :start AND :end")
    long countByStatusAndUpdatedAtBetween(
            @Param("status") EnrollmentStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
