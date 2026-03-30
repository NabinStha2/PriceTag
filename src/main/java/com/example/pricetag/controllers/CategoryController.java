package com.example.pricetag.controllers;

import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.dto.category.CategoryDetailsDto;
import com.example.pricetag.dto.category.CategoryDto;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public ResponseEntity<CommonResponseDto<List<CategoryDto>>> getAllCategories() throws ApplicationException {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CommonResponseDto<CategoryDetailsDto>> getCategoryById(
            @PathVariable(name = "categoryId") Long categoryId) throws ApplicationException {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @PostMapping("/add")
    public ResponseEntity<CommonResponseDto<Void>> createCategory(@RequestBody CategoryDto categoryDto) throws ApplicationException {
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }


    @Transactional

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteCategory(@PathVariable(name = "categoryId") Long categoryId)
            throws ApplicationException {
        return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
    }


}
