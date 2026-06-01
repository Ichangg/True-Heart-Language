package com.englishcenter.backend.controller;

import com.englishcenter.backend.entity.CmsBanners;
import com.englishcenter.backend.service.CmsBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cms/banners")
public class CMSBannerController {

    @Autowired
    private CmsBannerService cmsBannerService;

    // Lấy danh sách banner đang hoạt động để hiển thị lên trang chủ (Slider/Popup)
    @GetMapping("/active")
    public List<CmsBanners> getActiveBanners() {
        return cmsBannerService.getActiveBanners();
    }

    // Admin tạo quảng cáo cho lớp học mới
    @PostMapping
    public CmsBanners createBanner(@RequestBody CmsBanners banner) {
        return cmsBannerService.createBanner(banner);
    }

    @DeleteMapping("/{id}")
    public void deleteBanner(@PathVariable Long id) {
        cmsBannerService.deleteBanner(id);
    }
}