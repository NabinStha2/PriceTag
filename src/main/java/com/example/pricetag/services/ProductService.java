package com.example.pricetag.services;

import org.springframework.stereotype.Service;

import com.example.pricetag.dto.ProductDto;
import com.example.pricetag.responses.CommonResponseDto;

@Service
public interface ProductService {

  public CommonResponseDto createProduct(ProductDto productDto);

}
