package com.example.pricetag.services;

import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.dto.category.CategoryDetailsDto;
import com.example.pricetag.dto.category.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    CommonResponseDto<List<CategoryDto>> getAllCategories();

    CommonResponseDto<CategoryDetailsDto> getCategoryById(Long id);

    CommonResponseDto<Void> createCategory(CategoryDto CategoryDto);

    CommonResponseDto<Void> deleteCategory(Long categoryId);

}
