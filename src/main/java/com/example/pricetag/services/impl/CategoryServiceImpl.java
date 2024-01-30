package com.example.pricetag.services.impl;

import com.example.pricetag.dto.CategoryDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.entity.Category;
import com.example.pricetag.entity.SubCategory;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.CategoryRepo;
import com.example.pricetag.repository.SubCategoryRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.CategoryService;
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
    public CommonResponseDto createSubCategory(Long categoryId, SubCategoryDto subCategoryDto)
            throws ApplicationException {

        Optional<Category> existingCategoryOptional = categoryRepo.findById(categoryId);

        if (existingCategoryOptional.isPresent()) {
            Category existingCategory = existingCategoryOptional.get();
            Optional<SubCategory> subCategoryFilteredData = existingCategory.getSubCategories().stream()
                    .filter(data -> {
                        return data.getSubCategoryName().equals(subCategoryDto.getSubCategoryName());
                    }).findFirst();

            if (subCategoryFilteredData.isPresent()) {
                throw new ApplicationException("409",
                        "SubCategory already exists with this name inside this " + existingCategory.getCategoryName(),
                        HttpStatus.CONFLICT);
            }
            SubCategory newSubCategory = SubCategory
                    .builder()
                    .category(existingCategory)
                    .subCategoryName(subCategoryDto.getSubCategoryName())
                    .build();
            try {
                if (newSubCategory != null) {
                    SubCategory savedSubCategory = subCategoryRepo.save(newSubCategory);
                    existingCategory.getSubCategories().add(savedSubCategory);
                    categoryRepo.save(existingCategory);

                    return CommonResponseDto
                            .builder()
                            .message("SubCategory created successfully")
                            .success(true)
                            .data(Map.of("results", savedSubCategory))
                            .build();
                } else {
                    throw new ApplicationException("400", "SubCategory not created", HttpStatus.BAD_REQUEST);
                }
            } catch (DataAccessException ex) {
                throw new ApplicationException("500", "Database error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new ApplicationException("404", "Category not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public CommonResponseDto getSubCategoriesWithCategoryId(CategoryDto categoryDto) {

        Optional<Category> existingCategoryOptional = categoryRepo.findById(categoryDto.getId());

        if (existingCategoryOptional.isPresent()) {
            Category existingCategory = existingCategoryOptional.get();

            List<SubCategory> subCategories = existingCategory.getSubCategories();
            List<SubCategoryDto> subCategoryDtoList = new ArrayList<>();

            subCategories.forEach(subCategory -> {
                CategoryDto newCategoryDto = new CategoryDto();
                newCategoryDto.setId(subCategory.getCategory().getId());
                newCategoryDto.setCategoryName(subCategory.getCategory().getCategoryName());

                subCategoryDtoList.add(SubCategoryDto
                        .builder()
                        .id(subCategory.getId())
                        .subCategoryName(subCategory.getSubCategoryName())
                        .createdAt(subCategory.getCreatedAt())
                        .updatedAt(subCategory.getUpdatedAt())
                        .build());
            });

            return CommonResponseDto
                    .builder()
                    .message("Success")
                    .data(Map.of("results", subCategoryDtoList, "categoryName", existingCategory.getCategoryName()))
                    .success(true)
                    .build();

        } else {
            throw new ApplicationException("404", "Category not found", HttpStatus.NOT_FOUND);
        }

    }

}
