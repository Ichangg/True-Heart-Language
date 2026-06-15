package com.englishcenter.service;

import com.englishcenter.entity.Promotion;
import com.englishcenter.enums.PromotionType;
import com.englishcenter.exception.ResourceNotFoundException;
import com.englishcenter.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public Promotion create(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    public Page<Promotion> getAll(PromotionType type, Boolean isActive, int page, int limit) {
        Sort sort = Sort.by(Sort.Direction.ASC, "displayOrder").and(Sort.by(Sort.Direction.DESC, "createdAt"));
        PageRequest pageRequest = PageRequest.of(page - 1, limit, sort);
        return promotionRepository.findWithFilters(type, isActive, pageRequest);
    }

    public List<Promotion> getActivePromotions() {
        return promotionRepository.findActivePromotions(LocalDate.now());
    }

    public Promotion getById(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy quảng cáo."));
    }

    public Promotion update(Long id, Promotion data) {
        Promotion promotion = getById(id);
        if (data.getTitle() != null) promotion.setTitle(data.getTitle());
        if (data.getDescription() != null) promotion.setDescription(data.getDescription());
        if (data.getImageUrl() != null) promotion.setImageUrl(data.getImageUrl());
        if (data.getType() != null) promotion.setType(data.getType());
        if (data.getLinkUrl() != null) promotion.setLinkUrl(data.getLinkUrl());
        if (data.getIsActive() != null) promotion.setIsActive(data.getIsActive());
        if (data.getStartDate() != null) promotion.setStartDate(data.getStartDate());
        if (data.getEndDate() != null) promotion.setEndDate(data.getEndDate());
        if (data.getDisplayOrder() != null) promotion.setDisplayOrder(data.getDisplayOrder());
        return promotionRepository.save(promotion);
    }

    public void delete(Long id) {
        Promotion promotion = getById(id);
        promotionRepository.delete(promotion);
    }

    public Promotion toggleActive(Long id) {
        Promotion promotion = getById(id);
        promotion.setIsActive(!promotion.getIsActive());
        return promotionRepository.save(promotion);
    }
}
