package com.englishcenter.backend.modules.common.service.Impl;

import com.englishcenter.backend.core.entity.CmsBanners;
import com.englishcenter.backend.core.repository.CmsBannersRepository;
import com.englishcenter.backend.modules.common.service.CmsBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmsBannerServiceImpl implements CmsBannerService {

    @Autowired
    private CmsBannersRepository cmsBannersRepository;

    @Override
    public List<CmsBanners> getActiveBanners() { return cmsBannersRepository.findByIsActiveTrueOrderByPositionAsc(); }

    @Override
    public CmsBanners createBanner(CmsBanners banner) { return cmsBannersRepository.save(banner); }

    @Override
    public void deleteBanner(Long id) { cmsBannersRepository.deleteById(id); }
}