package com.englishcenter.backend.service;

import com.englishcenter.backend.entity.CmsBanners;
import java.util.List;

public interface CmsBannerService {
    List<CmsBanners> getActiveBanners();
    CmsBanners createBanner(CmsBanners banner);
    void deleteBanner(Long id);
}