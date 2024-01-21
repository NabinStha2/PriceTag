package com.example.pricetag.dto;

public class ProductDto {
  private Long categoryId;
  private Long subCategoryId;
  private String name;
  private String description;
  private Double actualPrice;
  private Double discountedPrice;

  public ProductDto(Long categoryId, Long subCategoryId, String name, String description,
      Double actualPrice, Double discountedPrice) {
    this.categoryId = categoryId;
    this.subCategoryId = subCategoryId;
    this.name = name;
    this.description = description;
    this.actualPrice = actualPrice;
    this.discountedPrice = discountedPrice;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public Long getSubCategoryId() {
    return subCategoryId;
  }

  public void setSubCategoryId(Long subCategoryId) {
    this.subCategoryId = subCategoryId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getActualPrice() {
    return actualPrice;
  }

  public void setActualPrice(Double actualPrice) {
    this.actualPrice = actualPrice;
  }

  public Double getDiscountedPrice() {
    return discountedPrice;
  }

  public void setDiscountedPrice(Double discountedPrice) {
    this.discountedPrice = discountedPrice;
  }

  @Override
  public String toString() {
    return "ProductDto [categoryId=" + categoryId + ", subCategoryId=" + subCategoryId + ", name=" + name
        + ", description=" + description + ", actualPrice=" + actualPrice + ", discountedPrice=" + discountedPrice
        + "]";
  }
}
