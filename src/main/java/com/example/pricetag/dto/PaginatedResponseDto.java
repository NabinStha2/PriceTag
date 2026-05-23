package com.example.pricetag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponseDto {
    private Integer currentPage;       // current page
    private Integer pageSize;   // items per page
    private Long totalItems;    // total items in DB
    private Integer totalPages; // total pages
    private boolean hasNext;
    private boolean hasPrevious;

    public static PaginatedResponseDto from(Page<?> pageData) {
        return PaginatedResponseDto
                .builder()
                .currentPage(pageData.getNumber() + 1)
                .pageSize(pageData.getSize())
                .totalItems(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .hasNext(pageData.hasNext())
                .hasPrevious(pageData.hasPrevious())
                .build();
    }
}