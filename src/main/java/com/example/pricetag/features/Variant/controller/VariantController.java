package com.example.pricetag.features.Variant.controller;


import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.features.Variant.service.VariantService;
import com.example.pricetag.features.product.dto.request.VariantRequestDto;
import com.example.pricetag.features.product.dto.response.VariantResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/variant")
@RequiredArgsConstructor
public class VariantController {
    private final VariantService variantService;


    @PostMapping("/product/{productId}")
    @Transactional
    public ResponseEntity<CommonResponseDto<List<VariantResponseDto>>> createVariant(
            @PathVariable
            Long productId,
            @RequestBody
            @Valid
            List<VariantRequestDto> variantRequestDto) {
        return ResponseEntity.ok(variantService.createVariant(variantRequestDto, productId));
    }

}
