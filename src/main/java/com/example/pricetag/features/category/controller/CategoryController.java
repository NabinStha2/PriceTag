package com.example.pricetag.features.category.controller;

import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.enums.ImageType;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.features.category.dto.request.CreateCategoryRequestDto;
import com.example.pricetag.features.category.dto.request.UpdateCategoryImageRequestDto;
import com.example.pricetag.features.category.dto.response.CategoryResponseDto;
import com.example.pricetag.features.category.dto.response.SingleCategoryDetailsResponseDto;
import com.example.pricetag.features.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public ResponseEntity<CommonResponseDto<List<CategoryResponseDto>>> getAllCategories() throws ApplicationException {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CommonResponseDto<SingleCategoryDetailsResponseDto>> getCategoryById(
            @PathVariable(name = "categoryId") Long categoryId) throws ApplicationException {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @PostMapping("/add")
    public ResponseEntity<CommonResponseDto<Void>> createCategory(CreateCategoryRequestDto createCategoryRequestDto)
            throws ApplicationException {
        return ResponseEntity.ok(categoryService.createCategory(createCategoryRequestDto));
    }


    @DeleteMapping("/{categoryId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteCategory(@PathVariable(name = "categoryId") Long categoryId)
            throws ApplicationException {
        return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
    }

    @PatchMapping(value = "/update/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponseDto<Void>> updateCategoryImage(
            @RequestParam(name = "entityType") String entityType,
            @ModelAttribute UpdateCategoryImageRequestDto updateCategoryImageRequestDto) throws ApplicationException {
        ImageType imageType;
        try {
            imageType = ImageType.valueOf(entityType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApplicationException("400", "Invalid entity type: " + entityType, HttpStatus.BAD_REQUEST);
        }

        updateCategoryImageRequestDto.setImageType(imageType);

        return ResponseEntity.ok(categoryService.updateCategoryImage(updateCategoryImageRequestDto));
    }


}
