package com.example.pricetag.utils;

import com.example.pricetag.dto.PaginationDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableBuilder {
    public static Pageable buildPageable(PaginationDto paginationDto) {
        int page = paginationDto == null ? 0 : Math.max(0, paginationDto.getPage() - 1);
        int size = paginationDto == null ? 5 : Math.max(1, paginationDto.getLimit());

        String sortBy = (paginationDto == null || paginationDto.getSortBy() == null || paginationDto
                .getSortBy()
                .isBlank())
                        ? "createdAt"
                        : paginationDto.getSortBy();

        Sort.Direction direction = (paginationDto != null && "desc".equalsIgnoreCase(paginationDto.getOrder()))
                                   ? Sort.Direction.DESC
                                   : Sort.Direction.ASC;

        return PageRequest.of(page, size, direction, sortBy);
    }
}
