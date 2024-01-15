package com.example.pricetag.services;

import org.springframework.stereotype.Service;

import com.example.pricetag.dto.CategoryDto;
import com.example.pricetag.entity.Category;
import com.example.pricetag.entity.SubCategory;
import com.example.pricetag.responses.CommonResponseDto;

@Service
public interface CategoryService {
  public CommonResponseDto getAllCategories();

  public Category getByCategoryName();

  public Category createCategory(Category category);

  public SubCategory createSubCategory(CategoryDto categoryDto);

}
