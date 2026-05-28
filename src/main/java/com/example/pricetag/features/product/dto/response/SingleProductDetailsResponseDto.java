package com.example.pricetag.features.product.dto.response;

import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.features.category.dto.response.CategoryResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingleProductDetailsResponseDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String shortDescription;
    private String imageUrl;                   // main/primary image
    private BigDecimal basePrice;              // MRP/base price
    private BigDecimal discountedPrice;        // final / offer price if available
    private String brand;
    private Double totalRating;
    private Integer totalReview;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private CategoryResponseDto category;
    private SubCategoryDto subCategory;

    private List<VariantDto> variants;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class VariantDto {
        private Long id;
        private String sku;
        private Integer stockQuantity;
        private BigDecimal actualPrice;
        private BigDecimal discountedPrice;
        private Integer weightInGrams;
        private ColorDto color;
        private SizeDto size;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ColorDto {
        private Long id;
        private String name;
        private String hexCode;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SizeDto {
        private Long id;
        private String name;
        private Integer displayOrder;
        private String sizeType;
    }
}
