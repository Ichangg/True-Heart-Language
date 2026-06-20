package com.englishcenter.backend.core.repository;

import com.englishcenter.backend.core.entity.ClassGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassGroupRepository extends JpaRepository<ClassGroups, Long> {
}
