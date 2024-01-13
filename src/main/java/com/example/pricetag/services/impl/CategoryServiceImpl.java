package com.example.pricetag.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pricetag.entity.Category;
import com.example.pricetag.repository.CategoryRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

  @Autowired
  private CategoryRepo categoryRepo;

  @Override
  public CommonResponseDto getAllCategories() {

    List<Category> categories = categoryRepo.findAll();

    return CommonResponseDto
        .builder()
        .message("Success")
        .data(Map.of("categories", categories))
        .statusCode("200")
        .build();

  }

  @Override
  public Category getByCategoryName() {

    return categoryRepo.findByCategoryName("getByCategoryName");

  }

  @Override
  public Category createCategory(Category categoryName) {

    Category newCategory = new Category();
    newCategory.setCategoryName(categoryName.getCategoryName());
    newCategory.setSubCategory(new ArrayList<>());

    return categoryRepo.save(newCategory);

  }

}
