package com.example.pricetag.controllers;

import com.example.pricetag.dto.CategoryDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.SubCategoryService;
import com.example.pricetag.utils.ColorLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subcategory")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @GetMapping("")
    public ResponseEntity<CommonResponseDto> getAllSubCategories() throws ApplicationException {
        return ResponseEntity.ok(subCategoryService.getAllSubCategories());
    }

    @PatchMapping("/edit")
    public ResponseEntity<CommonResponseDto> editSubCategory(
            @RequestBody SubCategoryDto subCategoryDto)
            throws ApplicationException {
        ColorLogger.logInfo(subCategoryDto.toString());
        return ResponseEntity.ok(subCategoryService.editSubCategory(subCategoryDto));
    }

    @PostMapping("/{categoryId}/add")
    public ResponseEntity<CommonResponseDto> createSubCategory(@PathVariable(name = "categoryId") Long categoryId,
                                                               @RequestBody SubCategoryDto subCategoryDto)
            throws ApplicationException {
        ColorLogger.logInfo(subCategoryDto.toString());
        return ResponseEntity.ok(subCategoryService.createSubCategory(categoryId, subCategoryDto));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CommonResponseDto> getSubCategoriesWithCategoryId(
            @PathVariable(name = "categoryId") Long categoryId) throws ApplicationException {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryId);
        return ResponseEntity.ok(subCategoryService.getSubCategoriesWithCategoryId(categoryDto));
    }

    @DeleteMapping("/{subCategoryId}")
    public ResponseEntity<CommonResponseDto> deleteCategory(@PathVariable(name = "subCategoryId") Long subCategoryId)
            throws ApplicationException {
        return ResponseEntity.ok(subCategoryService.deleteSubCategory(subCategoryId));
    }


}
