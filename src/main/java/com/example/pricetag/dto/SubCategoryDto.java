package com.example.pricetag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDto {
  private Long id;
  private String subCategoryName;
  // private CategoryDto category;
}