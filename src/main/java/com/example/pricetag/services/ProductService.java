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

    CommonResponseDto getProductsWithSubCategoryId(SubCategoryDto subCategoryDto, PaginationDto paginationDto);

    CommonResponseDto deleteProductById(Long productId);

    CommonResponseDto editProduct(ProductDto productDto);

}
