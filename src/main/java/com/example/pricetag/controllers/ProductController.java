package com.example.pricetag.controllers;

import com.example.pricetag.dto.CategoryDto;
import com.example.pricetag.dto.PaginationDto;
import com.example.pricetag.dto.ProductDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.ProductService;
import com.example.pricetag.utils.ColorLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/category/{categoryId}/subcategory/{subCategoryId}/add")
    public ResponseEntity<CommonResponseDto> createProduct(
            @PathVariable(name = "categoryId") Long categoryId,
            @PathVariable(name = "subCategoryId") Long subCategoryId,
            @RequestBody ProductDto productDto)
            throws ApplicationException {
        productDto.setCategory(CategoryDto.builder().id(categoryId).build());
        productDto.setSubCategory(SubCategoryDto.builder().id(subCategoryId).build());
        System.out.println(productDto);
        return ResponseEntity.ok(productService.createProduct(productDto));
    }

    @GetMapping("")
    public ResponseEntity<CommonResponseDto> getAllProducts(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "limit", defaultValue = "5", required = false) int limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "order", required = false) String order)
            throws ApplicationException {
        PaginationDto paginationDto = PaginationDto
                .builder()
                .page(page)
                .limit(limit)
                .sortBy(sortBy)
                .order(order)
                .build();
        ColorLogger.logInfo("paginationDto :: " + paginationDto.toString());
        return ResponseEntity.ok(productService.getAllProducts(paginationDto));
    }

    @GetMapping("/subcategory/{subCategoryId}")
    public ResponseEntity<CommonResponseDto> getProductsWithSubCategoryId(
            @PathVariable(name = "subCategoryId") Long subCategoryId,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "limit", defaultValue = "5", required = false) int limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "order", required = false) String order)
            throws ApplicationException {
        SubCategoryDto subCategoryDto = new SubCategoryDto();
        subCategoryDto.setId(subCategoryId);
        PaginationDto paginationDto = PaginationDto
                .builder()
                .page(page)
                .limit(limit)
                .sortBy(sortBy)
                .order(order)
                .build();
        ColorLogger.logInfo("paginationDto :: " + paginationDto.toString());
        return ResponseEntity.ok(productService.getProductsWithSubCategoryId(subCategoryDto, paginationDto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonResponseDto> deleteProductById(@PathVariable(name = "productId") Long productId)
            throws ApplicationException {
        return ResponseEntity.ok(productService.deleteProductById(productId));
    }

}
