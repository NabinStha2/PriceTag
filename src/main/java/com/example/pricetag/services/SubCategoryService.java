package com.example.pricetag.services;

import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.dto.category.CategoryDto;
import org.springframework.stereotype.Service;

@Service
public interface SubCategoryService {
    CommonResponseDto getAllSubCategories();

    CommonResponseDto editSubCategory(SubCategoryDto subCategoryDto);

    CommonResponseDto createSubCategory(Long categoryId, SubCategoryDto subCategoryDto);

    CommonResponseDto getSubCategoriesWithCategoryId(CategoryDto categoryDto);

    CommonResponseDto deleteSubCategory(Long subCategoryId);

}
