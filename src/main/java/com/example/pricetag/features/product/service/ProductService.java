package com.example.pricetag.features.product.service;

import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.dto.PaginationDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.features.product.dto.ProductDto;
import com.example.pricetag.features.product.dto.response.ProductResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    CommonResponseDto createProduct(ProductDto productDto);

    CommonResponseDto getAllProducts(PaginationDto paginationDto);

    CommonResponseDto getSingleProduct(Long productId);

    CommonResponseDto<List<ProductResponseDto>> getProductsWithSubCategoryId(SubCategoryDto subCategoryDto,
                                                                             PaginationDto paginationDto);

    CommonResponseDto getSearchProductsWithSubCategoryIdAndName(SubCategoryDto subCategoryDto,
                                                                PaginationDto paginationDto, String name);

    CommonResponseDto deleteProductById(Long productId);

    CommonResponseDto editProduct(ProductDto productDto);

}
