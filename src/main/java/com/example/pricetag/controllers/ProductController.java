package com.example.pricetag.controllers;

import com.example.pricetag.dto.CategoryDto;
import com.example.pricetag.dto.ProductDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/category/{categoryId}/subcategory/{subCategoryId}/add")
    public ResponseEntity<CommonResponseDto> createProduct(@PathVariable(name = "categoryId") Long categoryId,
            @PathVariable(name = "subCategoryId") Long subCategoryId, @RequestBody ProductDto productDto)
            throws ApplicationException {

        productDto.setCategory(CategoryDto.builder().id(categoryId).build());
        productDto.setSubCategory(SubCategoryDto.builder().id(subCategoryId).build());
        System.out.println(productDto);
        return ResponseEntity.ok(productService.createProduct(productDto));
    }

    @GetMapping("/")
    public ResponseEntity<CommonResponseDto> getAllProducts() throws ApplicationException {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonResponseDto> deleteProductById(@PathVariable(name = "productId") Long productId)
            throws ApplicationException {
        System.out.println(productId);
        return ResponseEntity.ok(productService.deleteProductById(productId));
    }

}
