package com.example.pricetag.dto;

import com.example.pricetag.features.category.dto.response.CategoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryWithCategoryDto {
    private Long id;
    private String subCategoryName;
    private CategoryResponseDto categoriesDto;
}
