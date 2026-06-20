package com.englishcenter.backend.modules.common.service;

import com.englishcenter.backend.core.entity.CmsBanners;
import java.util.List;

public interface CmsBannerService {
    List<CmsBanners> getActiveBanners();
    CmsBanners createBanner(CmsBanners banner);
    void deleteBanner(Long id);
}