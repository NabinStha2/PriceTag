package com.example.pricetag.services.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.pricetag.dto.CategoryDto;
import com.example.pricetag.entity.Category;
import com.example.pricetag.entity.SubCategory;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.CategoryRepo;
import com.example.pricetag.repository.SubCategoryRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.CategoryService;
import com.example.pricetag.utils.ColorLogger;

@Service
public class CategoryServiceImpl implements CategoryService {

  @Autowired
  private CategoryRepo categoryRepo;
  @Autowired
  private SubCategoryRepo subCategoryRepo;

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
  public Category createCategory(Category category) {

    Category newCategory = new Category();
    newCategory.setCategoryName(category.getCategoryName());
    newCategory.setSubCategories(new ArrayList<>());

    return categoryRepo.save(newCategory);

  }

  @Override
  public SubCategory createSubCategory(CategoryDto categoryDto) throws ApplicationException {

    Optional<Category> existingCategoryOptional = categoryRepo.findById(categoryDto.getId());

    if (existingCategoryOptional.isPresent()) {

      Category existingCategory = existingCategoryOptional.get();
      SubCategory newSubCategory = new SubCategory();
      newSubCategory.setCategory(existingCategory);
      newSubCategory.setSubCategoryName(categoryDto.getCategoryName());

      try {
        SubCategory savedSubCategory = subCategoryRepo.save(newSubCategory);
        existingCategory.getSubCategories().add(savedSubCategory);
        categoryRepo.save(existingCategory);

        return savedSubCategory;
      } catch (DataAccessException ex) {
        throw new ApplicationException("500", "Database error", HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } else {
      throw new ApplicationException("404", "Category not found", HttpStatus.NOT_FOUND);
    }
  }

}
