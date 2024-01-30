package com.example.pricetag.dto;

import com.example.pricetag.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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
    private List<Image> images;
    private Date createdAt;
    private Date updatedAt;

}
