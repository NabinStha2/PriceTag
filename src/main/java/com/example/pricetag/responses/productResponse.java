package com.example.pricetag.responses;

import com.example.pricetag.entity.Category;
import com.example.pricetag.entity.SubCategory;

import lombok.Data;

@Data
public class productResponse {
  private Long id;

  private String description;

  private Category category;

  private SubCategory subCategory;

  private Double actualPrice;

  private Double discountedPrice;
}
