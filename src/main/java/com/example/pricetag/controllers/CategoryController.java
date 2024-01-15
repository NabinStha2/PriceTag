package com.example.pricetag.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.pricetag.dto.CategoryDto;
import com.example.pricetag.entity.Category;
import com.example.pricetag.entity.SubCategory;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.CategoryService;
import com.example.pricetag.utils.ColorLogger;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/category")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @RequestMapping(name = "", method = RequestMethod.GET)
  public ResponseEntity<CommonResponseDto> getAllCategories() throws ApplicationException {
    return ResponseEntity.ok(categoryService.getAllCategories());
  }

  @PostMapping("/add")
  public ResponseEntity<Category> createCategory(@RequestBody Category category) throws ApplicationException {
    return ResponseEntity.ok(categoryService.createCategory(category));
  }

  @PostMapping("/{categoryId}/subCategory/add")
  public ResponseEntity<SubCategory> createSubCategory(@PathVariable(name = "categoryId") Long categoryId,
      @RequestBody CategoryDto categoryDto)
      throws ApplicationException {
    categoryDto.setId(categoryId);
    ColorLogger.logInfo(categoryDto.toString());
    return ResponseEntity.ok(categoryService.createSubCategory(categoryDto));
  }

}
