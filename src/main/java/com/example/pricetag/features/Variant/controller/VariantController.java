package com.example.pricetag.features.Variant.controller;


import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.features.Variant.service.VariantService;
import com.example.pricetag.features.product.dto.request.VariantRequestDto;
import com.example.pricetag.features.product.dto.response.VariantResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/variant")
@RequiredArgsConstructor
public class VariantController {
    private final VariantService variantService;


    @PostMapping("/create/product/{productId}")
    public ResponseEntity<CommonResponseDto<VariantResponseDto>> createVariant(
            @PathVariable
            Long productId,
            @RequestBody
            VariantRequestDto variantRequestDto) {
        variantRequestDto.setProductId(productId);
        return ResponseEntity.ok(variantService.createVariant(variantRequestDto));
    }

}
