package com.example.pricetag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long productId;
    private CategoryDto category;
    private SubCategoryDto subCategory;
    private String name;
    private String description;
    private Double actualPrice;
    private Double discountedPrice;

}
