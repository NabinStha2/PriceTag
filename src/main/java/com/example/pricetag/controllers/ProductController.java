package com.example.pricetag.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pricetag.dto.ProductDto;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private ProductService productService;

  @PostMapping("/category/{categoryId}/subcategory/{subCategoryId}/add")
  public ResponseEntity<CommonResponseDto> createProduct(@PathVariable(name = "categoryId") Long categoryId,
      @PathVariable(name = "subCategoryId") Long subCategoryId, @RequestBody ProductDto productDto)
      throws ApplicationException {
    productDto.setCategoryId(categoryId);
    productDto.setSubCategoryId(subCategoryId);
    System.out.println(productDto);
    return ResponseEntity.ok(productService.createProduct(productDto));
  }

  @GetMapping("")
  public ResponseEntity<CommonResponseDto> getAllProducts(@PathVariable(name = "categoryId") Long categoryId,
      @PathVariable(name = "subCategoryId") Long subCategoryId, @RequestBody ProductDto productDto)
      throws ApplicationException {
    productDto.setCategoryId(categoryId);
    productDto.setSubCategoryId(subCategoryId);
    System.out.println(productDto);
    return ResponseEntity.ok(productService.createProduct(productDto));
  }

}
