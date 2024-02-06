package com.example.pricetag.services.impl;

import com.example.pricetag.dto.CategoryDto;
import com.example.pricetag.entity.Category;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.CategoryRepo;
import com.example.pricetag.repository.SubCategoryRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.CategoryService;
import com.example.pricetag.utils.ColorLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                .data(Map.of("results", categories))
                .success(true)
                .build();

    }

    @Override
    public Category getByCategoryName() {

        return categoryRepo.findByCategoryName("getByCategoryName");

    }

    @Override
    public Category createCategory(CategoryDto categoryDto) {

        Category existingCategory = categoryRepo.findByCategoryName(categoryDto.getCategoryName());

        if (existingCategory != null) {
            throw new ApplicationException("409", "Category already exists", HttpStatus.CONFLICT);
        }

        Category newCategory = new Category();
        newCategory.setCategoryName(categoryDto.getCategoryName());
        newCategory.setSubCategories(new ArrayList<>());

        return categoryRepo.save(newCategory);

    }


    @Override
    public CommonResponseDto deleteCategory(Long categoryId) {

        try {
            Optional<Category> existingCategory = categoryRepo.findById(categoryId);

            if (existingCategory.isEmpty()) {
                throw new ApplicationException("404", "Category not found", HttpStatus.NOT_FOUND);
            }
            categoryRepo.deleteById(categoryId);

            return CommonResponseDto
                    .builder()
                    .message("Category deleted successfully")
                    .data(Map.of("results", existingCategory))
                    .success(true)
                    .build();

        } catch (DataAccessException ex) {
            ColorLogger.logError(ex.getMessage());
            throw new ApplicationException("500", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
