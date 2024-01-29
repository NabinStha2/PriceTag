package com.example.pricetag.services;

import com.example.pricetag.dto.ProductDto;
import com.example.pricetag.responses.CommonResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {

    CommonResponseDto createProduct(ProductDto productDto);

    CommonResponseDto getAllProducts();

    CommonResponseDto deleteProductById(Long productId);

}
