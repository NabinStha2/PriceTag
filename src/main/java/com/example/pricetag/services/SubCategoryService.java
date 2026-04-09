package com.example.pricetag.services;

import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.features.category.dto.response.CategoryResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface SubCategoryService {
    CommonResponseDto getAllSubCategories();

    CommonResponseDto editSubCategory(SubCategoryDto subCategoryDto);

    CommonResponseDto createSubCategory(Long categoryId, SubCategoryDto subCategoryDto);

    CommonResponseDto getSubCategoriesWithCategoryId(CategoryResponseDto categoryResponseDto);

    CommonResponseDto deleteSubCategory(Long subCategoryId);

}
