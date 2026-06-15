package com.englishcenter.repository;

import com.englishcenter.entity.ClassEntity;
import com.englishcenter.enums.ClassStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {

    long countByAgeGroupAndYear(Integer ageGroup, Integer year);

    long countByStatus(ClassStatus status);

    List<ClassEntity> findByTeacherId(Long teacherId);

    @Query("SELECT c FROM ClassEntity c WHERE " +
            "(:year IS NULL OR c.year = :year) AND " +
            "(:ageGroup IS NULL OR c.ageGroup = :ageGroup) AND " +
            "(:status IS NULL OR c.status = :status) AND " +
            "(:teacherId IS NULL OR c.teacherId = :teacherId)")
    Page<ClassEntity> findWithFilters(
            @Param("year") Integer year,
            @Param("ageGroup") Integer ageGroup,
            @Param("status") ClassStatus status,
            @Param("teacherId") Long teacherId,
            Pageable pageable);
}
