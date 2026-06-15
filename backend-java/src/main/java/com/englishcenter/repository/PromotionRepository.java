package com.englishcenter.repository;

import com.englishcenter.entity.Promotion;
import com.englishcenter.enums.PromotionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    @Query("SELECT p FROM Promotion p WHERE " +
            "(:type IS NULL OR p.type = :type) AND " +
            "(:isActive IS NULL OR p.isActive = :isActive)")
    Page<Promotion> findWithFilters(
            @Param("type") PromotionType type,
            @Param("isActive") Boolean isActive,
            Pageable pageable);

    @Query("SELECT p FROM Promotion p WHERE p.isActive = true AND " +
            "((p.startDate IS NULL AND p.endDate IS NULL) OR " +
            "(p.startDate <= :now AND p.endDate >= :now) OR " +
            "(p.startDate <= :now AND p.endDate IS NULL)) " +
            "ORDER BY p.displayOrder ASC")
    List<Promotion> findActivePromotions(@Param("now") LocalDate now);
}
