package com.example.pricetag.features.category.service;

import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.features.category.dto.request.CreateCategoryRequestDto;
import com.example.pricetag.features.category.dto.request.UpdateCategoryImageRequestDto;
import com.example.pricetag.features.category.dto.request.UpdateCategoryRequestDto;
import com.example.pricetag.features.category.dto.response.CategoryResponseDto;
import com.example.pricetag.features.category.dto.response.SingleCategoryDetailsResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    CommonResponseDto<List<CategoryResponseDto>> getAllCategories();

    CommonResponseDto<SingleCategoryDetailsResponseDto> getCategoryByIdWithSubCategories(Long id);

    CommonResponseDto<Void> createCategory(CreateCategoryRequestDto createCategoryRequestDto);

    CommonResponseDto<Void> updateCategory(UpdateCategoryRequestDto updateCategoryRequestDto);

    CommonResponseDto<Void> deleteCategory(Long categoryId);

    CommonResponseDto<Void> updateCategoryImage(
            UpdateCategoryImageRequestDto updateCategoryImageRequestDto);

}
