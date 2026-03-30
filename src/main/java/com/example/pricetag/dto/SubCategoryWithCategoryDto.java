package com.example.pricetag.dto;

import com.example.pricetag.dto.category.CategoryDto;
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
    private CategoryDto categoriesDto;
}
