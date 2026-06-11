package com.example.pricetag.features.Variant.service.impl;


import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.entity.Variants;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.features.Variant.mapper.VariantMapper;
import com.example.pricetag.features.Variant.repository.VariantRepo;
import com.example.pricetag.features.Variant.service.VariantService;
import com.example.pricetag.features.product.dto.request.VariantRequestDto;
import com.example.pricetag.features.product.dto.response.VariantResponseDto;
import com.example.pricetag.features.product.entity.Product;
import com.example.pricetag.features.product.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class VariantServiceImpl implements VariantService {
    private final VariantRepo variantRepo;
    private final ProductRepo productRepo;
    private final VariantMapper variantMapper;

    @Override
    public CommonResponseDto<VariantResponseDto> createVariant(
            VariantRequestDto variantRequestDto) {
        Product existingProduct = productRepo
                .findByIdAndIsActiveTrue(variantRequestDto.getProductId())
                .orElseThrow(() -> new ApplicationException("404", "Product not found",
                                                            HttpStatus.NOT_FOUND));

        if (variantRepo.existsBySkuAndIsActiveTrue(variantRequestDto.getSku())) {
            throw new ApplicationException("400", "Variant with the same SKU already exists",
                                           HttpStatus.BAD_REQUEST);
        }

        Variants newVariant = variantRepo.save(
                variantMapper.mapVariantRequestDtoToVariant(variantRequestDto));

        return CommonResponseDto
                .<VariantResponseDto>builder()
                .data(variantMapper.mapVariantToVariantResponseDto(newVariant))
                .status(401)
                .success(true)
                .message("Variant created successfully")
                .build();
    }
}
