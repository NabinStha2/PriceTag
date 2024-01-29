package com.example.pricetag.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pricetag.dto.CategoryDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.entity.Category;
import com.example.pricetag.entity.SubCategory;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.CategoryService;
import com.example.pricetag.utils.ColorLogger;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public ResponseEntity<CommonResponseDto> getAllCategories() throws ApplicationException {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{categoryId}/subCategories")
    public ResponseEntity<CommonResponseDto> getSubCategoriesWithCategoryId(
            @PathVariable(name = "categoryId") Long categoryId) throws ApplicationException {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryId);
        return ResponseEntity.ok(categoryService.getSubCategoriesWithCategoryId(categoryDto));
    }

    @PostMapping("/add")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto categoryDto) throws ApplicationException {
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }

    @PostMapping("/{categoryId}/subCategory/add")
    public ResponseEntity<CommonResponseDto> createSubCategory(@PathVariable(name = "categoryId") Long categoryId,
            @RequestBody SubCategoryDto subCategoryDto)
            throws ApplicationException {
        ColorLogger.logInfo(subCategoryDto.toString());
        return ResponseEntity.ok(categoryService.createSubCategory(categoryId, subCategoryDto));
    }

}
