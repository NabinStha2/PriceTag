package com.example.pricetag.features.category.service.impl;

import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.enums.ImageType;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.features.category.dto.request.CreateCategoryRequestDto;
import com.example.pricetag.features.category.dto.request.UpdateCategoryImageRequestDto;
import com.example.pricetag.features.category.dto.request.UpdateCategoryRequestDto;
import com.example.pricetag.features.category.dto.response.CategoryResponseDto;
import com.example.pricetag.features.category.dto.response.SingleCategoryDetailsResponseDto;
import com.example.pricetag.features.category.entity.Category;
import com.example.pricetag.features.category.mapper.CategoryMapper;
import com.example.pricetag.features.category.repository.CategoryRepo;
import com.example.pricetag.features.category.service.CategoryService;
import com.example.pricetag.features.image.dto.response.ImageResponseDto;
import com.example.pricetag.features.image.service.ImageService;
import com.example.pricetag.utils.ColorLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ImageService imageService;

    @Override
    public CommonResponseDto<List<CategoryResponseDto>> getAllCategories() {

        List<Category> categories = categoryRepo.findAllByIsActiveTrue();

        List<CategoryResponseDto> categoryResponseDtoList = categoryMapper.mapCategoryListToCategoryResponseDtoList(
                categories);

//        CategoryDtoList = categories.stream()
//                .map(category -> CategoryDto.builder()
//                        .id(category.getId())
//                        .name(category.getCategoryName())
//                        .createdAt(category.getCreatedAt())
//                        .updatedAt(category.getUpdatedAt())
//                        .build()).toList();

        return CommonResponseDto
                .<List<CategoryResponseDto>>builder()
                .message("Categories fetched successfully")
                .data(categoryResponseDtoList)
                .success(true)
                .status(HttpStatus.OK.value())
                .build();

    }

    @Override
    public CommonResponseDto<SingleCategoryDetailsResponseDto> getCategoryByIdWithSubCategories(Long id) {

        Category existingCategory = categoryRepo
                .findWithSubCategoriesById(id)
                .orElseThrow(() -> new ApplicationException("404", "Category not found", HttpStatus.NOT_FOUND));

        SingleCategoryDetailsResponseDto SingleCategoryDetailsResponseDto = categoryMapper.mapCategoryToSingleCategoryDetailsResponseDto(
                existingCategory);

        return CommonResponseDto
                .<SingleCategoryDetailsResponseDto>builder()
                .message("Category fetched successfully")
                .status(HttpStatus.OK.value())
                .success(true)
                .data(SingleCategoryDetailsResponseDto)
                .build();

    }

    @Transactional
    @Override
    public CommonResponseDto<Void> createCategory(CreateCategoryRequestDto createCategoryRequestDto) {

        if (categoryRepo.existsByCategoryName(createCategoryRequestDto.getName())) {
            throw new ApplicationException("409", "Category already exists", HttpStatus.CONFLICT);
        }

        Category newCategory = categoryMapper.mapCreateCategoryRequestDtoToCategory(createCategoryRequestDto);

        Category newSavedCategory = categoryRepo.save(newCategory);

        if (createCategoryRequestDto.getFile() != null && !createCategoryRequestDto
                .getFile()
                .isEmpty()) {

            ImageResponseDto imageResponseDto = imageService.saveSingleImage(newSavedCategory.getId(),
                                                                             ImageType.CATEGORY,
                                                                             createCategoryRequestDto.getFile(),
                                                                             createCategoryRequestDto.getName());

            newSavedCategory.setImageUrl(imageResponseDto.getUrl());
        }


        return CommonResponseDto
                .<Void>builder()
                .message("Category created successfully")
                .status(HttpStatus.CREATED.value())
                .success(true)
                .build();

    }

    @Override
    public CommonResponseDto<Void> updateCategory(UpdateCategoryRequestDto updateCategoryRequestDto) {
        Category existingCategory = categoryRepo
                .findByIdAndIsActiveTrue(updateCategoryRequestDto.getId())
                .orElseThrow(() -> new ApplicationException("404", "Category not found", HttpStatus.NOT_FOUND));

        if (updateCategoryRequestDto.getCategoryName() != null) {
            existingCategory.setCategoryName(updateCategoryRequestDto.getCategoryName());
        } else {
            ColorLogger.logError("Category name is empty");
        }

        categoryRepo.save(existingCategory);

        return CommonResponseDto
                .<Void>builder()
                .message("Category updated successfully")
                .status(HttpStatus.OK.value())
                .success(true)
                .build();
    }


    @Override
    public CommonResponseDto<Void> deleteCategory(Long categoryId) {

        if (!categoryRepo.existsById(categoryId)) {
            throw new ApplicationException("404", "Category not found", HttpStatus.NOT_FOUND);
        }

        categoryRepo.deleteById(categoryId);

        return CommonResponseDto
                .<Void>builder()
                .message("Category deleted successfully")
                .success(true)
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    @Transactional
    public CommonResponseDto<Void> updateCategoryImage(UpdateCategoryImageRequestDto updateCategoryImageRequestDto) {
        Category existingCategory = categoryRepo
                .findByIdAndIsActiveTrue(updateCategoryImageRequestDto.getEntityId())
                .orElseThrow(() -> new ApplicationException("404", "Category not found", HttpStatus.NOT_FOUND));

        imageService.deleteImages(updateCategoryImageRequestDto.getEntityId(),
                                  updateCategoryImageRequestDto.getImageType());

        ImageResponseDto imageResponseDto = imageService.saveSingleImage(updateCategoryImageRequestDto.getEntityId(),
                                                                         updateCategoryImageRequestDto.getImageType(),
                                                                         updateCategoryImageRequestDto.getFile(),
                                                                         existingCategory.getCategoryName());

        existingCategory.setImageUrl(imageResponseDto.getUrl());
        categoryRepo.save(existingCategory); //Updating the existing category with new image_url

        return CommonResponseDto
                .<Void>builder()
                .message("Category image updated successfully")
                .success(true)
                .status(HttpStatus.OK.value())
                .build();
    }

}
