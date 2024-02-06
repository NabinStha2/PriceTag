package com.example.pricetag.dto;

import com.example.pricetag.entity.Image;
import com.example.pricetag.entity.Variants;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long productId;
    private CategoryDto category;
    private SubCategoryDto subCategory;
    private String name;
    private String description;
    private List<Image> images;
    private List<Variants> variants;
    private Date createdAt;
    private Date updatedAt;

}
