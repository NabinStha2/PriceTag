package com.example.pricetag.features.Variant.service;


import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.features.product.dto.request.VariantRequestDto;
import com.example.pricetag.features.product.dto.response.VariantResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface VariantService {
    CommonResponseDto<VariantResponseDto> createVariant(VariantRequestDto variantRequestDto);
}
