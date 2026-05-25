package com.example.pricetag.features.product.controller;

import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.dto.PaginationDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.features.category.dto.response.CategoryResponseDto;
import com.example.pricetag.features.product.dto.ProductDto;
import com.example.pricetag.features.product.dto.response.ProductResponseDto;
import com.example.pricetag.features.product.service.ProductService;
import com.example.pricetag.utils.ColorLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping("/subcategory/{subCategoryId}")
    public ResponseEntity<CommonResponseDto<List<ProductResponseDto>>> getProductsWithSubCategoryId(
            @PathVariable
            Long subCategoryId,
            @RequestParam(name = "page",
                    defaultValue = "1",
                    required = false
            )
            int page,
            @RequestParam(name = "limit",
                    required = false
            )
            int limit,
            @RequestParam(name = "sortBy",
                    required = false
            )
            String sortBy,
            @RequestParam(name = "order",
                    required = false
            )
            String order) {
        SubCategoryDto subCategoryDto = new SubCategoryDto();
        subCategoryDto.setId(subCategoryId);
        PaginationDto paginationDto = PaginationDto
                .builder()
                .page(page)
                .limit(limit)
                .sortBy(sortBy)
                .order(order)
                .build();
//        ColorLogger.logInfo("paginationDto :: " + paginationDto.toString());
        return ResponseEntity.ok(productService.getProductsWithSubCategoryId(subCategoryDto, paginationDto));
    }

    @PostMapping("/category/{categoryId}/subcategory/{subCategoryId}/add")
    public ResponseEntity<CommonResponseDto> createProduct(
            @PathVariable
            Long categoryId,
            @PathVariable
            Long subCategoryId,
            @RequestBody
            ProductDto productDto) {
        productDto.setCategory(CategoryResponseDto
                                       .builder()
                                       .id(categoryId)
                                       .build());
        productDto.setSubCategory(SubCategoryDto
                                          .builder()
                                          .id(subCategoryId)
                                          .build());
        System.out.println(productDto);
        return ResponseEntity.ok(productService.createProduct(productDto));
    }

    @GetMapping("")
    public ResponseEntity<CommonResponseDto> getAllProducts(
            @RequestParam(name = "page",
                    defaultValue = "1",
                    required = false
            )
            int page,
            @RequestParam(name = "limit",
                    required = false
            )
            int limit,
            @RequestParam(name = "sortBy",
                    required = false
            )
            String sortBy,
            @RequestParam(name = "order",
                    required = false
            )
            String order) {
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

    @GetMapping("/{productId}")
    public ResponseEntity<CommonResponseDto> getSingleProduct(
            @PathVariable
            Long productId
                                                             ) {
        return ResponseEntity.ok(productService.getSingleProduct(productId));
    }

    @GetMapping("/subcategory/{subCategoryId}/search")
    public ResponseEntity<CommonResponseDto<List<ProductResponseDto>>> getSearchProductsWithSubCategoryId(
            @PathVariable
            Long subCategoryId,
            @RequestParam(name = "page",
                    defaultValue = "1",
                    required = false
            )
            int page,
            @RequestParam(name = "limit",
                    required = false
            )
            int limit,
            @RequestParam(name = "sortBy",
                    required = false
            )
            String sortBy,
            @RequestParam(name = "order",
                    required = false
            )
            String order,
            @RequestParam(name = "name",
                    required = false
            )
            String name) {
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
        return ResponseEntity.ok(
                productService.getSearchProductsWithSubCategoryIdAndName(subCategoryDto, paginationDto, name));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonResponseDto> deleteProductById(
            @PathVariable
            Long productId) {
        return ResponseEntity.ok(productService.deleteProductById(productId));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<CommonResponseDto> editProduct(
            @PathVariable
            Long productId,
            @RequestBody
            ProductDto productDto) {
        productDto.setProductId(productId);
        return ResponseEntity.ok(productService.editProduct(productDto));
    }

}
