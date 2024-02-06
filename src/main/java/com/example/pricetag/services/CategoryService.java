package com.example.pricetag.services;

import com.example.pricetag.dto.CategoryDto;
import com.example.pricetag.entity.Category;
import com.example.pricetag.responses.CommonResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    CommonResponseDto getAllCategories();

    Category getByCategoryName();

    Category createCategory(CategoryDto categoryDto);


    CommonResponseDto deleteCategory(Long categoryId);

}
