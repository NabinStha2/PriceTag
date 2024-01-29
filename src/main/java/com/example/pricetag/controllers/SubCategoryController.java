package com.example.pricetag.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.entity.SubCategory;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.SubCategoryService;
import com.example.pricetag.utils.ColorLogger;

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

  @GetMapping("/{subCategoryId}/products")
  public ResponseEntity<CommonResponseDto> getProductsWithSubCategoryId(
      @PathVariable(name = "subCategoryId") Long subCategoryId)
      throws ApplicationException {
    SubCategoryDto subCategoryDto = new SubCategoryDto();
    subCategoryDto.setId(subCategoryId);
    return ResponseEntity.ok(subCategoryService.getProductsWithSubCategoryId(subCategoryDto));
  }

}
