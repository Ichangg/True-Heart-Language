package com.englishcenter.repository;

import com.englishcenter.entity.Session;
import com.englishcenter.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByClassIdOrderBySessionNumberAsc(Long classId);
    List<Session> findByClassIdOrderBySessionDateAsc(Long classId);
    Optional<Session> findTopByClassIdOrderBySessionNumberDesc(Long classId);
}
