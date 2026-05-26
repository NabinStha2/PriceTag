package com.example.pricetag.features.product.dto.request;

import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.features.category.dto.response.CategoryResponseDto;
import com.example.pricetag.features.image.entity.Image;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequestDto {
    private Long productId;
    private CategoryResponseDto category;
    private SubCategoryDto subCategory;
    private String name;
    private String slug;
    private String description;
    private String shortDescription;
    private String brand;
    private BigDecimal basePrice;
    private BigDecimal discountedPrice;
    private List<Image> images;
    //    private List<Variants> variants;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
