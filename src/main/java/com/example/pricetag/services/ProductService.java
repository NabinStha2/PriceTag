package com.example.pricetag.services;

import com.example.pricetag.dto.PaginationDto;
import com.example.pricetag.dto.ProductDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.responses.CommonResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {

    CommonResponseDto createProduct(ProductDto productDto);

    CommonResponseDto getAllProducts(PaginationDto paginationDto);

    CommonResponseDto getSingleProduct(Long productId);

    CommonResponseDto getProductsWithSubCategoryId(SubCategoryDto subCategoryDto, PaginationDto paginationDto);

    CommonResponseDto getSearchProductsWithSubCategoryIdAndName(SubCategoryDto subCategoryDto, PaginationDto paginationDto, String name);

    CommonResponseDto deleteProductById(Long productId);

    CommonResponseDto editProduct(ProductDto productDto);

}
