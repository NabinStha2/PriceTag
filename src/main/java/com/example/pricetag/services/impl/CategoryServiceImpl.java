package com.example.pricetag.services.impl;

import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.dto.category.CategoryDetailsDto;
import com.example.pricetag.dto.category.CategoryDto;
import com.example.pricetag.entity.Category;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.mapper.CategoryMapper;
import com.example.pricetag.repository.CategoryRepo;
import com.example.pricetag.repository.SubCategoryRepo;
import com.example.pricetag.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private SubCategoryRepo subCategoryRepo;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CommonResponseDto<List<CategoryDto>> getAllCategories() {

        List<Category> categories = categoryRepo.findAllByIsActiveTrue();

        List<CategoryDto> CategoryDtoList = categoryMapper.mapCategoryListToCategoryDtoList(categories);

//        CategoryDtoList = categories.stream()
//                .map(category -> CategoryDto.builder()
//                        .id(category.getId())
//                        .name(category.getCategoryName())
//                        .createdAt(category.getCreatedAt())
//                        .updatedAt(category.getUpdatedAt())
//                        .build()).toList();

        return CommonResponseDto.<List<CategoryDto>>builder()
                .message("Categories fetched successfully")
                .data(CategoryDtoList)
                .success(true)
                .status(HttpStatus.OK.value())
                .build();

    }

    @Override
    public CommonResponseDto<CategoryDetailsDto> getCategoryById(Long id) {

        Category existingCategory = categoryRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("404", "Category not found", HttpStatus.NOT_FOUND));

        CategoryDetailsDto CategoryDetailsDto = categoryMapper.mapCategoryToCategoryDetailsDto(existingCategory);

        return CommonResponseDto.<CategoryDetailsDto>builder()
                .message("Category fetched successfully")
                .status(HttpStatus.OK.value())
                .success(true)
                .data(CategoryDetailsDto)
                .build();

    }

    @Override
    public CommonResponseDto<Void> createCategory(CategoryDto categoryDto) {

        if (categoryRepo.existsByCategoryName(categoryDto.getName())) {
            throw new ApplicationException("409", "Category already exists", HttpStatus.CONFLICT);
        }

        Category newCategory = categoryMapper.mapCategoryDtoToCategory(categoryDto);
        categoryRepo.save(newCategory);

        return CommonResponseDto.<Void>builder()
                .message("Category created successfully")
                .status(HttpStatus.CREATED.value())
                .success(true)
                .build();

    }


    @Override
    public CommonResponseDto<Void> deleteCategory(Long categoryId) {

        if (!categoryRepo.existsById(categoryId)) {
            throw new ApplicationException("404", "Category not found", HttpStatus.NOT_FOUND);
        }

        categoryRepo.deleteById(categoryId);

        return CommonResponseDto.<Void>builder()
                .message("Category deleted successfully")
                .success(true)
                .status(HttpStatus.OK.value())
                .build();
    }

}
