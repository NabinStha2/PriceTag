package com.example.pricetag.controllers;

import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.SubCategoryService;
import com.example.pricetag.utils.ColorLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subcategory")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @GetMapping("/")
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


}
