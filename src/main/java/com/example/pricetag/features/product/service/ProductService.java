package com.example.pricetag.features.product.service;

import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.dto.PaginationDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.features.product.dto.request.CreateProductRequestDto;
import com.example.pricetag.features.product.dto.response.ProductResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    CommonResponseDto<ProductResponseDto> createProduct(CreateProductRequestDto createProductRequestDto);

    CommonResponseDto<List<ProductResponseDto>> getAllProducts(PaginationDto paginationDto);

    CommonResponseDto getSingleProduct(Long productId);

    CommonResponseDto<List<ProductResponseDto>> getProductsWithSubCategoryId(SubCategoryDto subCategoryDto,
                                                                             PaginationDto paginationDto);

    CommonResponseDto<List<ProductResponseDto>> getSearchProductsWithSubCategoryIdAndName(SubCategoryDto subCategoryDto,
                                                                                          PaginationDto paginationDto,
                                                                                          String name);

    CommonResponseDto<Void> deleteProductById(Long productId);

    CommonResponseDto<ProductResponseDto> editProduct(CreateProductRequestDto createProductRequestDto);

}
