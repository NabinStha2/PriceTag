package com.example.pricetag.features.Variant.service;


import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.features.product.dto.request.VariantRequestDto;
import com.example.pricetag.features.product.dto.response.VariantResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VariantService {
    CommonResponseDto<List<VariantResponseDto>> createVariant(
            List<VariantRequestDto> variantRequestDto,
            Long productId);
}
