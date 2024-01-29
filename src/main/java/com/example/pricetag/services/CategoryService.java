package com.example.pricetag.services;

import org.springframework.stereotype.Service;

import com.example.pricetag.dto.CategoryDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.entity.Category;
import com.example.pricetag.responses.CommonResponseDto;

@Service
public interface CategoryService {
    CommonResponseDto getAllCategories();

    Category getByCategoryName();

    Category createCategory(CategoryDto categoryDto);

    CommonResponseDto createSubCategory(Long categoryId, SubCategoryDto subCategoryDto);

    CommonResponseDto getSubCategoriesWithCategoryId(CategoryDto categoryDto);

}
