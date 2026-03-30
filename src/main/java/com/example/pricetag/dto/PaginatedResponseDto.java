package com.example.pricetag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponseDto<T> {
    private List<T> items;
    private Integer page;       // current page
    private Integer pageSize;   // items per page
    private Long totalItems;    // total items in DB
    private Integer totalPages; // total pages
}