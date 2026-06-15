package com.englishcenter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Paginated API response — tương đương paginatedResponse() trong responseHelper.js
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginatedResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private Pagination pagination;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pagination {
        private long total;
        private int page;
        private int limit;
        private int totalPages;
    }

    public static <T> PaginatedResponse<T> of(T data, long total, int page, int limit) {
        int totalPages = (int) Math.ceil((double) total / limit);
        return PaginatedResponse.<T>builder()
                .success(true)
                .message("Thành công")
                .data(data)
                .pagination(Pagination.builder()
                        .total(total)
                        .page(page)
                        .limit(limit)
                        .totalPages(totalPages)
                        .build())
                .build();
    }
}
