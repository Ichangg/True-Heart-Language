package com.englishcenter.controller;

import com.englishcenter.dto.ApiResponse;
import com.englishcenter.dto.PaginatedResponse;
import com.englishcenter.entity.Promotion;
import com.englishcenter.entity.User;
import com.englishcenter.enums.PromotionType;
import com.englishcenter.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<Promotion>>> getActive() {
        return ResponseEntity.ok(ApiResponse.success(promotionService.getActivePromotions()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String type,
            @RequestParam(name = "is_active", required = false) Boolean isActive,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PromotionType typeEnum = type != null ? PromotionType.valueOf(type) : null;
        Page<Promotion> result = promotionService.getAll(typeEnum, isActive, page, limit);
        return ResponseEntity.ok(PaginatedResponse.of(result.getContent(), result.getTotalElements(), page, limit));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Promotion>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(promotionService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Promotion>> create(@RequestBody Promotion promotion, @AuthenticationPrincipal User user) {
        promotion.setCreatedBy(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(promotionService.create(promotion), "Tạo quảng cáo thành công."));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Promotion>> update(@PathVariable Long id, @RequestBody Promotion promotion) {
        return ResponseEntity.ok(ApiResponse.success(promotionService.update(id, promotion), "Cập nhật quảng cáo thành công."));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, String>>> delete(@PathVariable Long id) {
        promotionService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "Đã xóa quảng cáo.")));
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Promotion>> toggleActive(@PathVariable Long id) {
        Promotion promotion = promotionService.toggleActive(id);
        String msg = promotion.getIsActive() ? "Đã bật quảng cáo." : "Đã tắt quảng cáo.";
        return ResponseEntity.ok(ApiResponse.success(promotion, msg));
    }
}
