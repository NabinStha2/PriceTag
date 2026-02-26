package com.example.pricetag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDto {
    private Long id;
    private String subCategoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long categoryId;
    // private CategoryDto category;
}