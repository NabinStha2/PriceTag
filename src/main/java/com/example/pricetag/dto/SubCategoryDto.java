package com.example.pricetag.dto;

import lombok.Data;

@Data
public class SubCategoryDto {
  private Long id;
  private String subCategoryName;
  private CategoryDto category;
}
