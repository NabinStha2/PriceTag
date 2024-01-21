package com.example.pricetag.services;

import org.springframework.stereotype.Service;

import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.entity.SubCategory;
import com.example.pricetag.responses.CommonResponseDto;

@Service
public interface SubCategoryService {
  public CommonResponseDto getAllSubCategories();

  public SubCategory editSubCategory(SubCategoryDto subCategoryDto);

  public CommonResponseDto getProductsWithSubCategoryId(SubCategoryDto subCategoryDto);
}
