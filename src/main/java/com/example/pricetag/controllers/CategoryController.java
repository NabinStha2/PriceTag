package com.example.pricetag.controllers;

import com.example.pricetag.dto.CategoryDto;
import com.example.pricetag.entity.Category;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public ResponseEntity<CommonResponseDto> getAllCategories() throws ApplicationException {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }


    @PostMapping("/add")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto categoryDto) throws ApplicationException {
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }


    @Transactional

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<CommonResponseDto> deleteCategory(@PathVariable(name = "categoryId") Long categoryId)
            throws ApplicationException {
        return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
    }


}
