package com.englishcenter.repository;

import com.englishcenter.entity.Attendance;
import com.englishcenter.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByEnrollmentIdAndSessionId(Long enrollmentId, Long sessionId);
    List<Attendance> findBySessionId(Long sessionId);
    List<Attendance> findByEnrollmentId(Long enrollmentId);
    List<Attendance> findByEnrollmentIdAndStatus(Long enrollmentId, AttendanceStatus status);
}
