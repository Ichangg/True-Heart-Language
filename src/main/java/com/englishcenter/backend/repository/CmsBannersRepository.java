package com.englishcenter.backend.repository;

import com.englishcenter.backend.entity.CmsBanners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CmsBannersRepository extends JpaRepository<CmsBanners, Long> {
    // Tìm các banner đang hiển thị (Active)
    List<CmsBanners> findByIsActiveTrue();

    // Tìm banner theo vị trí hiển thị (ví dụ: "HOME", "SIDEBAR")
    List<CmsBanners> findByPosition(Integer position);

    // Tìm banner theo trạng thái (Active/Inactive) và vị trí
    List<CmsBanners> findByIsActiveTrueOrderByPositionAsc();
}